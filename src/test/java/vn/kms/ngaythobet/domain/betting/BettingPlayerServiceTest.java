package vn.kms.ngaythobet.domain.betting;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import vn.kms.ngaythobet.BaseTest;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.User.Role;
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
import vn.kms.ngaythobet.web.dto.PlayerBettingMatchInfo;
import vn.kms.ngaythobet.web.dto.UpdatePlayerBettingMatchInfo;

public class BettingPlayerServiceTest extends BaseTest {

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
    private Round roundTemp;
    private Match matchTemp;
    private User userTemp1;
    private User userTemp2;
    private Group groupTemp;
    private BettingMatch bettingMatchTemp;
    private BettingPlayer bettingPlayerTemp;

    @Override
    protected void doStartUp() {
        bettingPlayerService = new BettingPlayerService(bettingMatchRepo, bettingPlayerRepo, competitorRepo, userRepo);
        // add 2 users
        User user1 = new User();
        user1.setActivated(true);
        user1.setEmail("email@yahoo.com");
        user1.setName("user1");
        user1.setPassword("123467");
        user1.setRole(Role.USER);
        user1.setUsername("user1");
        User user2 = new User();
        user2.setActivated(true);
        user2.setEmail("email2@yahoo.com");
        user2.setName("user2");
        user2.setPassword("123467");
        user2.setRole(Role.USER);
        user2.setUsername("user2");
        userTemp1 = userRepo.save(user1);
        userTemp2 = userRepo.save(user2);
        // add tournament
        Tournament tournament = new Tournament();
        tournament.setActivated(false);
        tournament.setName("Euro");
        tournamentTemp = tournamentRepo.save(tournament);
        // add round
        Round round = new Round();
        round.setTournament(tournamentTemp);
        round.setName("round 1");
        roundTemp = roundRepo.save(round);
        // add 2 competitors
        Competitor competitor1 = new Competitor(tournamentTemp, "England");
        Competitor competitor2 = new Competitor(tournamentTemp, "France");
        List<Round> rounds = new ArrayList<>();
        rounds.add(roundTemp);
        competitor1.setRounds(rounds);
        competitor2.setRounds(rounds);
        competitorTemp1 = competitorRepo.save(competitor1);
        competitorTemp2 = competitorRepo.save(competitor2);
        // add match
        Match match = new Match();
        match.setCompetitor1(competitorTemp1);
        match.setCompetitor2(competitorTemp2);
        match.setLocation("location test");
        match.setMatchTime(LocalDateTime.now());
        match.setRound(roundTemp);
        matchTemp = matchRepo.save(match);
        // add group
        Group group = new Group();
        List<User> members = new ArrayList<>();
        members.add(userTemp1);
        members.add(userTemp2);
        group.setMembers(members);
        group.setModerator(userTemp1);
        group.setName("testGroup");
        group.setTournament(tournamentTemp);
        groupTemp = groupRepo.save(group);
        // add betting match
        BettingMatch bettingMatch = new BettingMatch();
        bettingMatch.setActivated(true);
        bettingMatch.setMatch(matchTemp);
        bettingMatch.setGroup(groupTemp);
        bettingMatch.setExpiredTime(LocalDateTime.now().plusDays(30));
        bettingMatchTemp = bettingMatchRepo.save(bettingMatch);
        // add player betting match
        BettingPlayer bettingPlayer = new BettingPlayer();
        bettingPlayer.setBetCompetitor(competitorTemp2);
        bettingPlayer.setPlayer(userTemp2);
        bettingPlayer.setBettingMatch(bettingMatchTemp);
        bettingPlayerTemp = bettingPlayerRepo.save(bettingPlayer);
    }

    @Test
    public void testPlayBet() {
        mockLoginUser("user1");
        PlayerBettingMatchInfo playerBettingMatchInfo = new PlayerBettingMatchInfo();
        playerBettingMatchInfo.setComment("test");
        playerBettingMatchInfo.setCompetitorId(competitorTemp1.getId());
        playerBettingMatchInfo.setBettingMatchId(bettingMatchTemp.getId());
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
