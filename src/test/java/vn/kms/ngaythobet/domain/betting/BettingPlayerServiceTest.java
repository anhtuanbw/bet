package vn.kms.ngaythobet.domain.betting;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import vn.kms.ngaythobet.BaseTest;
import vn.kms.ngaythobet.CreateData;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.UserRepository;
import vn.kms.ngaythobet.domain.tournament.Competitor;
import vn.kms.ngaythobet.domain.tournament.CompetitorRepository;
import vn.kms.ngaythobet.domain.tournament.Group;
import vn.kms.ngaythobet.domain.tournament.GroupRepository;
import vn.kms.ngaythobet.domain.tournament.Match;
import vn.kms.ngaythobet.domain.tournament.MatchRepository;
import vn.kms.ngaythobet.domain.tournament.Round;
import vn.kms.ngaythobet.domain.tournament.RoundRepository;
import vn.kms.ngaythobet.domain.tournament.Tournament;
import vn.kms.ngaythobet.domain.tournament.TournamentRepository;
import vn.kms.ngaythobet.web.dto.AddCommentInfo;
import vn.kms.ngaythobet.web.dto.BettingMatchStatisticsInfo;
import vn.kms.ngaythobet.web.dto.PlayerBettingMatchInfo;
import vn.kms.ngaythobet.web.dto.UpdatePlayerBettingMatchInfo;

public class BettingPlayerServiceTest extends BaseTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private TournamentRepository tournamentRepo;

    @Autowired
    private BettingMatchRepository bettingMatchRepo;

    @Autowired
    private BettingPlayerRepository bettingPlayerRepo;
    @Autowired
    private CompetitorRepository competitorRepo;

    @Autowired
    private RoundRepository roundRepo;

    @Autowired
    private MatchRepository matchRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private GroupRepository groupRepo;

    private BettingPlayerService bettingPlayerService;

    private Tournament tournamentTemp;
    private Competitor competitorTemp1;
    private Competitor competitorTemp2;
    private Competitor competitorTemp3;
    private Competitor competitorTemp4;
    private Round roundTemp;
    private Match matchTemp;
    private Match matchTemp2;
    private Match matchTemp3;
    private User userTemp1;
    private User userTemp2;
    private Group groupTemp;
    private BettingMatch bettingMatchTemp;
    private BettingMatch notActiveBettingMatch;
    private BettingMatch expiredBettingMatch;
    private BettingPlayer bettingPlayerTemp;

    @Override
    protected void doStartUp() {
        bettingPlayerService = new BettingPlayerService(bettingMatchRepo, bettingPlayerRepo, competitorRepo, userRepo);
        // add 2 users
        User user1 = CreateData.createUser(true, "email@yahoo.com", "user1", "123467", "user1");
        User user2 = CreateData.createUser(true, "email2@yahoo.com", "user2", "123467", "user2");
        userTemp1 = userRepo.save(user1);
        userTemp2 = userRepo.save(user2);
        // add tournament
        Tournament tournament = CreateData.createTournament(false, "Euro");
        tournamentTemp = tournamentRepo.save(tournament);
        // add round
        Round round = new Round();
        round.setTournament(tournamentTemp);
        round.setName("round 1");
        roundTemp = roundRepo.save(round);
        // add 4 competitors
        Competitor competitor1 = new Competitor(tournamentTemp, "England");
        Competitor competitor2 = new Competitor(tournamentTemp, "France");
        Competitor competitor3 = new Competitor(tournamentTemp, "Germany");
        Competitor competitor4 = new Competitor(tournamentTemp, "Portulgal");
        List<Round> rounds = new ArrayList<>();
        rounds.add(roundTemp);
        competitor1.setRounds(rounds);
        competitor2.setRounds(rounds);
        competitor3.setRounds(rounds);
        competitor4.setRounds(rounds);
        competitorTemp1 = competitorRepo.save(competitor1);
        competitorTemp2 = competitorRepo.save(competitor2);
        competitorTemp3 = competitorRepo.save(competitor3);
        competitorTemp4 = competitorRepo.save(competitor4);
        // add 3 matches
        Match match = CreateData.createMatch(competitorTemp1, competitorTemp2, "location test", LocalDateTime.now(),
                roundTemp);
        matchTemp = matchRepo.save(match);
        Match match2 = CreateData.createMatch(competitorTemp3, competitorTemp4, "location test 2", LocalDateTime.now(),
                roundTemp);
        matchTemp2 = matchRepo.save(match2);
        Match match3 = CreateData.createMatch(competitorTemp1, competitorTemp3, "location test 3", LocalDateTime.now(),
                roundTemp);
        matchTemp3 = matchRepo.save(match3);
        // add group
        List<User> members = new ArrayList<>();
        members.add(userTemp1);
        members.add(userTemp2);
        Group group = CreateData.createGroup(userTemp1, "testGroup", tournamentTemp, members);
        groupTemp = groupRepo.save(group);
        // add valid betting match
        BettingMatch bettingMatch = CreateData.createBettingMatch(true, matchTemp, groupTemp,
                LocalDateTime.now().plusDays(30));
        bettingMatchTemp = bettingMatchRepo.save(bettingMatch);
        // add betting Match is expired
        BettingMatch bettingMatch2 = CreateData.createBettingMatch(true, matchTemp3, groupTemp,
                LocalDateTime.now().minusDays(30));
        expiredBettingMatch = bettingMatchRepo.save(bettingMatch2);
        // add betting Match is not active
        BettingMatch bettingMatch3 = CreateData.createBettingMatch(false, matchTemp2, groupTemp,
                LocalDateTime.now().plusDays(30));
        notActiveBettingMatch = bettingMatchRepo.save(bettingMatch3);
        // add player betting match
        BettingPlayer bettingPlayer = CreateData.createBettingPlayer(bettingMatchTemp, userTemp2, competitorTemp2);
        bettingPlayerTemp = bettingPlayerRepo.save(bettingPlayer);
    }

    @Test
    public void testPlayBet() {
        mockLoginUser(userTemp1);
        PlayerBettingMatchInfo playerBettingMatchInfo = new PlayerBettingMatchInfo();
        playerBettingMatchInfo.setComment("test");
        playerBettingMatchInfo.setCompetitorId(competitorTemp1.getId());
        playerBettingMatchInfo.setBettingMatchId(bettingMatchTemp.getId());
        bettingPlayerService.playBet(playerBettingMatchInfo);
        assertThat(bettingPlayerRepo.findAll().size(), equalTo(2));
        assertThat(bettingMatchRepo.findOne(bettingMatchTemp.getId()).getComment(), equalTo("test"));
    }

    @Test
    public void testPlayBetWithNotActiveBettingMatch() {
        mockLoginUser("user1");
        PlayerBettingMatchInfo playerBettingMatchInfo = new PlayerBettingMatchInfo();
        playerBettingMatchInfo.setComment("test");
        playerBettingMatchInfo.setCompetitorId(competitorTemp3.getId());
        playerBettingMatchInfo.setBettingMatchId(notActiveBettingMatch.getId());
        exception.expectMessage("{exception.bettingPlayer.service.bettingMatch-not-active}");
        bettingPlayerService.playBet(playerBettingMatchInfo);
        assertThat(bettingPlayerRepo.findAll().size(), equalTo(1));

    }

    @Test
    public void testPlayBetWithExpiredMatch() {
        mockLoginUser("user1");
        PlayerBettingMatchInfo playerBettingMatchInfo = new PlayerBettingMatchInfo();
        playerBettingMatchInfo.setCompetitorId(competitorTemp1.getId());
        playerBettingMatchInfo.setBettingMatchId(expiredBettingMatch.getId());
        exception.expectMessage("{exception.bettingPlayer.service.bettingMatch-is-expired}");
        bettingPlayerService.playBet(playerBettingMatchInfo);
        assertThat(bettingPlayerRepo.findAll().size(), equalTo(1));
    }

    @Test
    public void testPlayBetWithInvalidCompetitor() {
        mockLoginUser("user1");
        PlayerBettingMatchInfo playerBettingMatchInfo = new PlayerBettingMatchInfo();
        playerBettingMatchInfo.setComment("test");
        playerBettingMatchInfo.setCompetitorId(competitorTemp4.getId());
        playerBettingMatchInfo.setBettingMatchId(bettingMatchTemp.getId());
        exception.expectMessage("{exception.bettingPlayer.service.competitor-belong-match}");
        bettingPlayerService.playBet(playerBettingMatchInfo);
        assertThat(bettingPlayerRepo.findAll().size(), equalTo(1));
    }

    @Test
    public void testPlayBetWithBetMatch() {
        mockLoginUser(userTemp1);
        PlayerBettingMatchInfo playerBettingMatchInfo = new PlayerBettingMatchInfo();
        playerBettingMatchInfo.setComment("test");
        playerBettingMatchInfo.setCompetitorId(competitorTemp1.getId());
        playerBettingMatchInfo.setBettingMatchId(bettingMatchTemp.getId());
        bettingPlayerService.playBet(playerBettingMatchInfo);
        exception.expectMessage("{exception.bettingPlayer.service.already-bet}");
        bettingPlayerService.playBet(playerBettingMatchInfo);
        assertThat(bettingPlayerRepo.findAll().size(), equalTo(2));
    }

    @Test
    public void testUpdatePlayBet() {
        mockLoginUser("user2");
        UpdatePlayerBettingMatchInfo updateInfo = new UpdatePlayerBettingMatchInfo();
        updateInfo.setBettingPlayerId(bettingPlayerTemp.getId());
        updateInfo.setCompetitorId(competitorTemp1.getId());
        bettingPlayerService.updatePlayBet(updateInfo);
        assertThat(bettingPlayerRepo.findOne(bettingPlayerTemp.getId()).getBetCompetitor().getName(),
                equalTo("England"));
    }

    @Test
    public void testUpdatePlayBetWithInvalidCompetitor() {
        mockLoginUser("user2");
        UpdatePlayerBettingMatchInfo updateInfo = new UpdatePlayerBettingMatchInfo();
        updateInfo.setBettingPlayerId(bettingPlayerTemp.getId());
        updateInfo.setCompetitorId(competitorTemp4.getId());
        exception.expectMessage("{exception.bettingPlayer.service.competitor-belong-match}");
        bettingPlayerService.updatePlayBet(updateInfo);
        assertThat(bettingPlayerRepo.findOne(bettingPlayerTemp.getId()).getBetCompetitor().getName(),
                equalTo("France"));
    }

    @Test
    public void testUpdatePlayBetWithInvalidAuthor() {
        mockLoginUser("user1");
        UpdatePlayerBettingMatchInfo updateInfo = new UpdatePlayerBettingMatchInfo();
        updateInfo.setBettingPlayerId(bettingPlayerTemp.getId());
        updateInfo.setCompetitorId(competitorTemp2.getId());
        exception.expectMessage("{exception.unauthorized}");
        bettingPlayerService.updatePlayBet(updateInfo);
        assertThat(bettingPlayerRepo.findOne(bettingPlayerTemp.getId()).getBetCompetitor().getName(),
                equalTo("France"));
    }

    @Test
    public void testGetBettingMatchStatistics() {
        mockLoginUser("user");
        BettingMatchStatisticsInfo info = bettingPlayerService.getBettingMatchStatistics(bettingMatchTemp.getId());
        assertThat(info.getBettingPlayersChooseCompetitor1().size(), equalTo(0)); 
        assertThat(info.getBettingPlayersChooseCompetitor2().size(), equalTo(1));
        assertThat(info.getUserNotBet().size(), equalTo(1));
    }

    @Test
    public void testGetBettingPlayerOfCurrentUserByBettingMatchId() {
        mockLoginUser(userTemp2);
        Optional<User> userOptional = userRepo.findOneByUsername("user2");
        BettingPlayer bettingPlayer = bettingPlayerService
                .getBettingPlayerOfCurrentUserByBettingMatchId(bettingMatchTemp.getId());
        BettingPlayer currentBettingPlayer = bettingPlayerRepo.findByPlayerAndBettingMatch(userOptional.get(),
                bettingMatchTemp);
        assertThat(bettingPlayer.getId(), equalTo(currentBettingPlayer.getId()));
    }

    @Test
    public void testAddComment() {
        mockLoginUser("user");
        AddCommentInfo addCommentInfo = new AddCommentInfo();
        addCommentInfo.setComment("Test Comment Betting Match");
        addCommentInfo.setBettingMatchId(bettingMatchTemp.getId());
        bettingPlayerService.addComment(addCommentInfo);
        assertThat(bettingMatchRepo.findOne(bettingMatchTemp.getId()).getComment(),
                equalTo("Test Comment Betting Match"));
    }

    @After
    public void clearData() {
        bettingMatchRepo.deleteAll();
        matchRepo.deleteAll();
        roundRepo.deleteAll();
        groupRepo.deleteAll();
        competitorRepo.deleteAll();
        tournamentRepo.deleteAll();
        userRepo.deleteAll();
    }

}
