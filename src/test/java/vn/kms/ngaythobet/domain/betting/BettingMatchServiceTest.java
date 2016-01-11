package vn.kms.ngaythobet.domain.betting;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import vn.kms.ngaythobet.web.dto.ActiveBettingMatchInfo;
import vn.kms.ngaythobet.web.dto.CreateBettingMatchInfo;
import vn.kms.ngaythobet.web.dto.GetBettingMatchesByRoundAndGroupIdInfo;
import vn.kms.ngaythobet.web.dto.UpdateBettingMatchInfo;

public class BettingMatchServiceTest extends BaseTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private TournamentRepository tournamentRepo;

    @Autowired
    private BettingMatchRepository bettingMatchRepo;

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

    @Autowired
    private BettingMatchService bettingMatchService;

    private Tournament tournamentTemp;
    private Competitor competitorTemp1;
    private Competitor competitorTemp2;
    private Round roundTemp;
    private Match matchTemp;
    private User userTemp1;
    private User userTemp2;
    private Group groupTemp;
    private BettingMatch bettingMatchTemp;

    @Override
    protected void doStartUp() {

        // create 2 users
        User user1 = CreateData.createUser(true, "hieu1@abc.com", "hieu1", "123467", "hieu1");
        User user2 = CreateData.createUser(true, "hieu2@abc.com", "hieu2", "123467", "hieu2");
        userTemp1 = userRepo.save(user1);
        userTemp2 = userRepo.save(user2);
        // create tournament
        Tournament tournament = CreateData.createTournament(false, "worldcup");
        tournamentTemp = tournamentRepo.save(tournament);
        // create 2 competitors
        Competitor competitor1 = new Competitor(tournamentTemp, "Spain");
        Competitor competitor2 = new Competitor(tournamentTemp, "Italia");
        List<Round> rounds = new ArrayList<>();
        rounds.add(roundTemp);
        competitor1.setRounds(rounds);
        competitor2.setRounds(rounds);
        competitorTemp1 = competitorRepo.save(competitor1);
        competitorTemp2 = competitorRepo.save(competitor2);
        // create round
        Round round = CreateData.createRound(tournamentTemp, "final round");
        roundTemp = roundRepo.save(round);
        // create match
        Match match = CreateData.createMatch(competitorTemp1, competitorTemp2, "location test",
                LocalDateTime.now().plusDays(30), roundTemp);
        matchTemp = matchRepo.save(match);
        // create group
        List<User> members = new ArrayList<>();
        members.add(userTemp1);
        members.add(userTemp2);
        Group group = CreateData.createGroup(userTemp1, "testGroup", tournamentTemp, members);
        groupTemp = groupRepo.save(group);

    }

    private BettingMatch createBettingMatch() {
        BettingMatch bettingMatch = new BettingMatch();
        bettingMatch.setActivated(false);
        bettingMatch.setBalance1(new BigDecimal("0"));
        bettingMatch.setBalance2(new BigDecimal("0"));
        bettingMatch.setBetAmount(new BigDecimal("0"));
        bettingMatch.setExpiredTime(LocalDateTime.now());
        bettingMatch.setGroup(groupTemp);
        bettingMatch.setMatch(matchTemp);
        bettingMatch.setDescription("test");
        bettingMatchTemp = bettingMatchRepo.save(bettingMatch);
        return bettingMatchTemp;
    }

    @Test
    public void testCreateBettingMatch() {
        CreateBettingMatchInfo createBettingMatchInfo = new CreateBettingMatchInfo();
        createBettingMatchInfo.setActivated(true);
        createBettingMatchInfo.setBalance1(new BigDecimal("0"));
        createBettingMatchInfo.setBalance2(new BigDecimal("0.5"));
        createBettingMatchInfo.setBetAmount(new BigDecimal("1000"));
        createBettingMatchInfo.setDecription("test");
        createBettingMatchInfo.setExpiredTime(LocalDateTime.now());
        createBettingMatchInfo.setGroupId(groupTemp.getId());
        createBettingMatchInfo.setMatchId(matchTemp.getId());
        bettingMatchService.createBettingMatch(createBettingMatchInfo);
        assertThat(bettingMatchRepo.findAll().size(), equalTo(1));

    }

    @Test
    public void testUpdateBettingMatch() {
        // create betting match
        BettingMatch bettingMatch = createBettingMatch();

        UpdateBettingMatchInfo updateBettingMatchInfo = new UpdateBettingMatchInfo();
        updateBettingMatchInfo.setBalance1(new BigDecimal(1));
        updateBettingMatchInfo.setBalance2(new BigDecimal(0));
        updateBettingMatchInfo.setActivated(false);
        updateBettingMatchInfo.setBetAmount(new BigDecimal("999999"));
        updateBettingMatchInfo.setBettingMatchId(bettingMatch.getId());
        updateBettingMatchInfo.setDecription("test again");
        updateBettingMatchInfo.setExpiredTime(LocalDateTime.now().plusDays(1));
        updateBettingMatchInfo.setGroupId(groupTemp.getId());
        updateBettingMatchInfo.setMatchId(matchTemp.getId());
        bettingMatchService.updateBettingMatch(updateBettingMatchInfo);
        assertThat(bettingMatchRepo.findAll().size(), equalTo(1));
        assertThat(bettingMatchRepo.findOne(bettingMatch.getId()).getDescription(), equalTo("test again"));
    }

    @Test
    public void testActiveBettingMatch() {
        BettingMatch bettingMatch = createBettingMatch();
        ActiveBettingMatchInfo activeBettingMatchInfo = new ActiveBettingMatchInfo();
        activeBettingMatchInfo.setBettingMatchId(bettingMatch.getId());
        activeBettingMatchInfo.setGroupId(groupTemp.getId());
        bettingMatchService.activeBettingMatch(activeBettingMatchInfo);
        assertThat(bettingMatchRepo.findOne(bettingMatch.getId()).isActivated(), equalTo(true));
    }

    @Test
    public void testGetBettingMatchesByGroupAndRoundId() {
        BettingMatch bettingMatch = createBettingMatch();
        GetBettingMatchesByRoundAndGroupIdInfo getBettingMatchesByRoundAndGroupIdInfo = new GetBettingMatchesByRoundAndGroupIdInfo();
        getBettingMatchesByRoundAndGroupIdInfo.setGroupId(groupTemp.getId());
        getBettingMatchesByRoundAndGroupIdInfo.setRoundId(roundTemp.getId());
        List<BettingMatch> result = bettingMatchService
                .getBettingMatchesByRoundAndGroupId(getBettingMatchesByRoundAndGroupIdInfo);
        assertThat(result.size(), equalTo(1));
    }

    @Test
    public void testGetBettingMatchesByMatch() {
        List<BettingMatch> result = bettingMatchService.getBettingMatchesByMatch(matchTemp.getId());
        assertThat(result.size(), equalTo(0));

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
