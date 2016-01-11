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

    private Tournament tournamentTemp1;
    private Tournament tournamentTemp2;
    private Competitor competitorTemp1;
    private Competitor competitorTemp2;
    private Round roundTemp;
    private Match matchTemp;
    private User userTemp1;
    private User userTemp2;
    private Group groupTemp1;
    private Group groupTemp2;
    private BettingMatch bettingMatchTemp;
    private BettingMatch bettingMatchTemp2;

    @Override
    protected void doStartUp() {

        // create 2 users
        User user1 = createUser(true, "hieu1@abc.com", "hieu1", "123467", Role.USER, "hieu1");
        User user2 = createUser(true, "hieu2@abc.com", "hieu2", "123467", Role.USER, "hieu2");
        userTemp1 = userRepo.save(user1);
        userTemp2 = userRepo.save(user2);
        // create tournament
        Tournament tournament1 = new Tournament();
        tournament1.setActivated(false);
        tournament1.setName("worldcup");
        tournamentTemp1 = tournamentRepo.save(tournament1);
        // create tournament2
        Tournament tournament2 = new Tournament();
        tournament2.setActivated(false);
        tournament2.setName("worldcup");
        tournamentTemp2 = tournamentRepo.save(tournament2);
        // create 2 competitors
        Competitor competitor1 = new Competitor(tournamentTemp1, "Spain");
        Competitor competitor2 = new Competitor(tournamentTemp1, "Italia");
        List<Round> rounds = new ArrayList<>();
        rounds.add(roundTemp);
        competitor1.setRounds(rounds);
        competitor2.setRounds(rounds);
        competitorTemp1 = competitorRepo.save(competitor1);
        competitorTemp2 = competitorRepo.save(competitor2);
        // create round
        Round round = new Round();
        round.setTournament(tournamentTemp1);
        round.setName("final round");
        roundTemp = roundRepo.save(round);
        // create match
        Match match = new Match();
        match.setCompetitor1(competitorTemp1);
        match.setCompetitor2(competitorTemp2);
        match.setLocation("location test");
        match.setMatchTime(LocalDateTime.now().plusDays(30));
        match.setRound(roundTemp);
        matchTemp = matchRepo.save(match);

        List<User> members = new ArrayList<>();
        members.add(userTemp1);
        members.add(userTemp2);
        List<User> members1 = new ArrayList<>();
        members1.add(userTemp1);
        // create group1
        Group group1 = createGroup(members, userTemp1, "testGroup", tournamentTemp1);
        groupTemp1 = groupRepo.save(group1);
        // create group2
        Group group2 = createGroup(members1, userTemp1, "testGroup1", tournamentTemp2);
        groupTemp2 = groupRepo.save(group2);
    }

    private Group createGroup(List<User> members, User moderator, String name, Tournament tournament) {
        Group group = new Group();
        group.setMembers(members);
        group.setModerator(moderator);
        group.setName(name);
        group.setTournament(tournament);
        return group;
    }

    private User createUser(boolean active, String email, String name, String password, Role role, String userName) {
        User user = new User();
        user.setActivated(active);
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        user.setRole(role);
        user.setUsername(userName);
        return user;
    }

    private BettingMatch createBettingMatch() {
        BettingMatch bettingMatch = new BettingMatch();
        bettingMatch.setActivated(false);
        bettingMatch.setBalance1(new BigDecimal("0"));
        bettingMatch.setBalance2(new BigDecimal("0"));
        bettingMatch.setBetAmount(new BigDecimal("0"));
        bettingMatch.setExpiredTime(LocalDateTime.now());
        bettingMatch.setGroup(groupTemp1);
        bettingMatch.setMatch(matchTemp);
        bettingMatch.setDescription("test");
        bettingMatchTemp2 = bettingMatchRepo.save(bettingMatch);
        return bettingMatchTemp2;
    }

    private BettingMatch createBettingMatchActived() {
        BettingMatch bettingMatch = new BettingMatch();
        bettingMatch.setActivated(true);
        bettingMatch.setBalance1(new BigDecimal("0"));
        bettingMatch.setBalance2(new BigDecimal("0"));
        bettingMatch.setBetAmount(new BigDecimal("0"));
        bettingMatch.setExpiredTime(LocalDateTime.now());
        bettingMatch.setGroup(groupTemp1);
        bettingMatch.setMatch(matchTemp);
        bettingMatch.setDescription("test");
        bettingMatchTemp2 = bettingMatchRepo.save(bettingMatch);
        return bettingMatchTemp2;
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
        createBettingMatchInfo.setGroupId(groupTemp1.getId());
        createBettingMatchInfo.setMatchId(matchTemp.getId());
        bettingMatchService.createBettingMatch(createBettingMatchInfo);
        assertThat(bettingMatchRepo.findAll().size(), equalTo(1));

    }

    @Test
    public void testCreateBettingMatchWithInvalidMatchAndGroup() {
        CreateBettingMatchInfo createBettingMatchInfo = new CreateBettingMatchInfo();
        createBettingMatchInfo.setActivated(true);
        createBettingMatchInfo.setBalance1(new BigDecimal("0"));
        createBettingMatchInfo.setBalance2(new BigDecimal("0.5"));
        createBettingMatchInfo.setBetAmount(new BigDecimal("1000"));
        createBettingMatchInfo.setDecription("test");
        createBettingMatchInfo.setExpiredTime(LocalDateTime.now());
        createBettingMatchInfo.setGroupId(groupTemp2.getId());
        createBettingMatchInfo.setMatchId(matchTemp.getId());
        exception.expectMessage("{exception.match.group.invalid}");
        bettingMatchService.createBettingMatch(createBettingMatchInfo);
        ;
        assertThat(bettingMatchRepo.findAll().size(), equalTo(1));
    }

    @Test
    public void testCreateBettingMatchWithInvalidBalance() {
        CreateBettingMatchInfo createBettingMatchInfo = new CreateBettingMatchInfo();
        createBettingMatchInfo.setActivated(true);
        createBettingMatchInfo.setBalance1(new BigDecimal("0.111"));
        createBettingMatchInfo.setBalance2(new BigDecimal("0.1111"));
        createBettingMatchInfo.setBetAmount(new BigDecimal("1000"));
        createBettingMatchInfo.setDecription("test");
        createBettingMatchInfo.setExpiredTime(LocalDateTime.now());
        createBettingMatchInfo.setGroupId(groupTemp2.getId());
        createBettingMatchInfo.setMatchId(matchTemp.getId());
        exception.expectMessage("{exception.balance.in.valid}");
        bettingMatchService.createBettingMatch(createBettingMatchInfo);
        ;
        assertThat(bettingMatchRepo.findAll().size(), equalTo(1));
    }

    @Test
    public void testCreateBettingMatchWithBettingMatchIsExisted() {
        CreateBettingMatchInfo createBettingMatchInfo = new CreateBettingMatchInfo();
        BettingMatch bettingMatch = createBettingMatch();
        createBettingMatchInfo.setActivated(true);
        createBettingMatchInfo.setBalance1(new BigDecimal("0"));
        createBettingMatchInfo.setBalance2(new BigDecimal("0"));
        createBettingMatchInfo.setBetAmount(new BigDecimal("1000"));
        createBettingMatchInfo.setDecription("test");
        createBettingMatchInfo.setExpiredTime(LocalDateTime.now());
        createBettingMatchInfo.setGroupId(bettingMatch.getGroup().getId());
        createBettingMatchInfo.setMatchId(bettingMatch.getMatch().getId());
        exception.expectMessage("{exception.bettingMatch.is.existed}");
        bettingMatchService.createBettingMatch(createBettingMatchInfo);
        ;
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
        updateBettingMatchInfo.setGroupId(groupTemp1.getId());
        updateBettingMatchInfo.setMatchId(matchTemp.getId());
        bettingMatchService.updateBettingMatch(updateBettingMatchInfo);
        assertThat(bettingMatchRepo.findAll().size(), equalTo(1));
        assertThat(bettingMatchRepo.findOne(bettingMatch.getId()).getDescription(), equalTo("test again"));
    }

    @Test
    public void testUpdateBettingMatchWithInvalidMatchAndGroup() {
        BettingMatch bettingMatch = createBettingMatch();

        UpdateBettingMatchInfo updateBettingMatchInfo = new UpdateBettingMatchInfo();
        updateBettingMatchInfo.setBalance1(new BigDecimal(1));
        updateBettingMatchInfo.setBalance2(new BigDecimal(0));
        updateBettingMatchInfo.setActivated(false);
        updateBettingMatchInfo.setBetAmount(new BigDecimal("999999"));
        updateBettingMatchInfo.setBettingMatchId(bettingMatch.getId());
        updateBettingMatchInfo.setDecription("test again");
        updateBettingMatchInfo.setExpiredTime(LocalDateTime.now().plusDays(1));
        updateBettingMatchInfo.setGroupId(groupTemp2.getId());
        updateBettingMatchInfo.setMatchId(matchTemp.getId());
        exception.expectMessage("{exception.match.group.invalid}");
        bettingMatchService.updateBettingMatch(updateBettingMatchInfo);
        assertThat(bettingMatchRepo.findAll().size(), equalTo(1));
    }

    @Test
    public void testUpdateBettingMatchWithInvalidBalance() {
        BettingMatch bettingMatch = createBettingMatch();

        UpdateBettingMatchInfo updateBettingMatchInfo = new UpdateBettingMatchInfo();
        updateBettingMatchInfo.setBalance1(new BigDecimal(1.111111));
        updateBettingMatchInfo.setBalance2(new BigDecimal(0));
        updateBettingMatchInfo.setActivated(false);
        updateBettingMatchInfo.setBetAmount(new BigDecimal("999999"));
        updateBettingMatchInfo.setBettingMatchId(bettingMatch.getId());
        updateBettingMatchInfo.setDecription("test again");
        updateBettingMatchInfo.setExpiredTime(LocalDateTime.now().plusDays(1));
        updateBettingMatchInfo.setGroupId(groupTemp2.getId());
        updateBettingMatchInfo.setMatchId(matchTemp.getId());
        exception.expectMessage("{exception.balance.in.valid}");
        bettingMatchService.updateBettingMatch(updateBettingMatchInfo);
        assertThat(bettingMatchRepo.findAll().size(), equalTo(1));
    }

    @Test
    public void testUpdateBettingMatchWithBetIsActived() {
        BettingMatch bettingMatch = createBettingMatchActived();
        bettingMatch.setActivated(true);

        UpdateBettingMatchInfo updateBettingMatchInfo = new UpdateBettingMatchInfo();
        updateBettingMatchInfo.setBalance1(new BigDecimal(1));
        updateBettingMatchInfo.setBalance2(new BigDecimal(0));
        updateBettingMatchInfo.setActivated(false);
        updateBettingMatchInfo.setBetAmount(new BigDecimal("999999"));
        updateBettingMatchInfo.setBettingMatchId(bettingMatch.getId());
        updateBettingMatchInfo.setDecription("test again");
        updateBettingMatchInfo.setExpiredTime(LocalDateTime.now().plusHours(1));
        updateBettingMatchInfo.setGroupId(groupTemp1.getId());
        updateBettingMatchInfo.setMatchId(matchTemp.getId());
        exception.expectMessage("{exception.betting.match.is.actived}");
        bettingMatchService.updateBettingMatch(updateBettingMatchInfo);
        assertThat(bettingMatchRepo.findAll().size(), equalTo(1));
    }

    @Test
    public void testGetBettingMatchById() {
        BettingMatch bettingMatch = createBettingMatch();
        assertThat(bettingMatchService.getBettingMatchById(bettingMatch.getId()).getBetAmount().doubleValue(),
                equalTo(0.0));
    }

    @Test
    public void testActiveBettingMatch() {
        BettingMatch bettingMatch = createBettingMatch();
        ActiveBettingMatchInfo activeBettingMatchInfo = new ActiveBettingMatchInfo();
        activeBettingMatchInfo.setBettingMatchId(bettingMatch.getId());
        activeBettingMatchInfo.setGroupId(groupTemp1.getId());
        bettingMatchService.activeBettingMatch(activeBettingMatchInfo);
        assertThat(bettingMatchRepo.findOne(bettingMatch.getId()).isActivated(), equalTo(true));
    }

    @Test
    public void testGetBettingMatchesByGroupAndRoundId() {
        BettingMatch bettingMatch = createBettingMatch();
        GetBettingMatchesByRoundAndGroupIdInfo getBettingMatchesByRoundAndGroupIdInfo = new GetBettingMatchesByRoundAndGroupIdInfo();
        getBettingMatchesByRoundAndGroupIdInfo.setGroupId(groupTemp1.getId());
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
