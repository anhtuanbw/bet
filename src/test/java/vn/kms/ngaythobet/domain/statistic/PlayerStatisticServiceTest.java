package vn.kms.ngaythobet.domain.statistic;

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
import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.betting.BettingMatchRepository;
import vn.kms.ngaythobet.domain.betting.BettingPlayer;
import vn.kms.ngaythobet.domain.betting.BettingPlayerRepository;
import vn.kms.ngaythobet.domain.betting.BettingPlayerService;
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
import vn.kms.ngaythobet.web.dto.PlayerStatisticInfo;

public class PlayerStatisticServiceTest extends BaseTest {
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
    
    @Autowired
    private PlayerStatisticService playerStatisticService;

    private Tournament tournamentTemp;
    private Competitor competitorTemp1;
    private Competitor competitorTemp2;
    private Competitor competitorTemp3;
    private Competitor competitorTemp4;
    private Round roundTemp;
    private Match matchTemp;
    private Match matchTemp2;
    private Match matchTemp3;
    private Match matchTemp4;
    private User userTemp1;
    private User userTemp2;
    private Group groupTemp;
    private BettingMatch bettingMatchTemp;
    private BettingMatch bettingMatchTemp2;
    private BettingMatch bettingMatchTemp3;
    private BettingMatch bettingMatchTemp4;
    private BettingPlayer bettingPlayerTemp;
    private BettingPlayer bettingPlayerTemp2;
    private BettingPlayer bettingPlayerTemp3;

    private User createUser(boolean active, String email, String name, String password, String username) {
        User user = new User();
        user.setActivated(active);
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        user.setRole(Role.USER);
        user.setUsername(username);
        return user;
    }

    private PlayerStatisticInfo createPlayerStatisticInfo() {
        PlayerStatisticInfo playerStatisticInfo = new PlayerStatisticInfo();
        playerStatisticInfo.setGroupId(groupTemp.getId());
        return playerStatisticInfo;
    }

    private BettingMatch createBettingMatch(boolean active, Match match, Group group, LocalDateTime expiredTime,BigDecimal balance1,BigDecimal balance2,BigDecimal betAmount) {
        BettingMatch bettingMatch1 = new BettingMatch();
        bettingMatch1.setActivated(active);
        bettingMatch1.setMatch(match);
        bettingMatch1.setGroup(group);
        bettingMatch1.setExpiredTime(expiredTime);
        bettingMatch1.setBalance1(balance1);
        bettingMatch1.setBalance2(balance2);
        bettingMatch1.setBetAmount(betAmount);
        return bettingMatch1;
    }

    private BettingPlayer createBettingPlayer(BettingMatch bettingMatch, User user, Competitor competitor) {
        BettingPlayer bettingPlayer = new BettingPlayer();
        bettingPlayer.setBetCompetitor(competitor);
        bettingPlayer.setPlayer(user);
        bettingPlayer.setBettingMatch(bettingMatch);
        return bettingPlayer;
    }

    private Match createMatch(Competitor competitor1, Competitor competitor2, String location, LocalDateTime matchTime,
            Round round,Long score1 ,Long score2) {
        Match match = new Match();
        match.setCompetitor1(competitor1);
        match.setCompetitor2(competitor2);
        match.setLocation(location);
        match.setMatchTime(matchTime);
        match.setRound(round);
        match.setScore1(score1);
        match.setScore2(score2);
        return match;
    }

    @Override
    protected void doStartUp() {
        bettingPlayerService = new BettingPlayerService(bettingMatchRepo, bettingPlayerRepo, competitorRepo, userRepo);
        // add 2 users
        User user1 = createUser(true, "email@yahoo.com", "user1", "123467", "user1");
        User user2 = createUser(true, "email2@yahoo.com", "user2", "123467", "user2");
        userTemp1 = userRepo.save(user1);
        userTemp2 = userRepo.save(user2);
        // add tournament
        Tournament tournament = new Tournament();
        tournament.setActivated(true);
        tournament.setName("Euro");
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
        // add 4 matches
        Match match = createMatch(competitorTemp1, competitorTemp2, "location test", LocalDateTime.now(), roundTemp,1l, 1l);
        matchTemp = matchRepo.save(match);
        Match match2 = createMatch(competitorTemp3, competitorTemp4, "location test 2", LocalDateTime.now(), roundTemp,null,null);
        matchTemp2 = matchRepo.save(match2);
        Match match3 = createMatch(competitorTemp1, competitorTemp3, "location test 3", LocalDateTime.now(), roundTemp,0l,1l);
        matchTemp3 = matchRepo.save(match3);
        Match match4 = createMatch(competitorTemp1, competitorTemp4, "location test 3", LocalDateTime.now(), roundTemp,1l,0l);
        matchTemp4 = matchRepo.save(match4);
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
        // add 4 valid betting match
        BettingMatch bettingMatch = createBettingMatch(true, matchTemp, groupTemp, LocalDateTime.now().plusDays(30),new BigDecimal(1), new BigDecimal(0), new BigDecimal(100));
        bettingMatchTemp = bettingMatchRepo.save(bettingMatch);
        BettingMatch bettingMatch2 = createBettingMatch(true, matchTemp2, groupTemp, LocalDateTime.now().plusDays(30),new BigDecimal(1), new BigDecimal(0), new BigDecimal(100));
        bettingMatchTemp2 = bettingMatchRepo.save(bettingMatch2);
        BettingMatch bettingMatch3 = createBettingMatch(true, matchTemp3, groupTemp, LocalDateTime.now().plusDays(30),new BigDecimal(1), new BigDecimal(0), new BigDecimal(100));
        bettingMatchTemp3 = bettingMatchRepo.save(bettingMatch3);
        BettingMatch bettingMatch4 = createBettingMatch(true, matchTemp4, groupTemp, LocalDateTime.now().minusDays(30),new BigDecimal(0), new BigDecimal(1), new BigDecimal(100));
        bettingMatchTemp4 = bettingMatchRepo.save(bettingMatch4);
        // add 4 players betting match
        BettingPlayer bettingPlayer = createBettingPlayer(bettingMatchTemp, userTemp1, competitorTemp2);
        bettingPlayerTemp = bettingPlayerRepo.save(bettingPlayer);
        BettingPlayer bettingPlayer2 = createBettingPlayer(bettingMatchTemp2, userTemp1, competitorTemp2);
        bettingPlayerTemp2 = bettingPlayerRepo.save(bettingPlayer2);
        BettingPlayer bettingPlayer3 = createBettingPlayer(bettingMatchTemp3, userTemp1, competitorTemp1);
        bettingPlayerTemp3 = bettingPlayerRepo.save(bettingPlayer3);
        List<BettingPlayer> bettingPlayers = new ArrayList<>();
        bettingPlayers.add(bettingPlayerTemp);
        bettingPlayers.add(bettingPlayerTemp2);
        bettingPlayers.add(bettingPlayerTemp3);
        bettingMatch.setBettingPlayers(bettingPlayers);
        bettingMatchTemp = bettingMatchRepo.save(bettingMatch);
    }

    @Test
    public void testPlayerStatistc() {
        mockLoginUser("user1");
        PlayerStatisticInfo playerStatisticInfo = createPlayerStatisticInfo();
        TotalPlayerStatistic totalPlayerStatistic = playerStatisticService.playerStatistic(playerStatisticInfo);
        assertThat(totalPlayerStatistic.getTotalLossAmount(), equalTo(250.0));
    }
    
    @Test
    public void testGetLostAmountByUser(){
        mockLoginUser("user1");
        assertThat(playerStatisticService.getLostAmountByUser(bettingMatchTemp.getId()), equalTo(0.0));
        assertThat(playerStatisticService.getLostAmountByUser(bettingMatchTemp4.getId()), equalTo(100.0));
    }
    
    @Test
    public void testgetLossAmountByUserWithInvalidBettingMatch(){
        mockLoginUser("user1");
        exception.expectMessage("{exception.existMatchEntity.message}");
        double lossAmount = playerStatisticService.getLostAmountByUser(bettingMatchTemp.getId()+ 5);
        
    }
    
    @Test
    public void testLossAmountbyUserWithNullScore(){
        mockLoginUser("user1");
        assertThat(playerStatisticService.getLostAmountByUser(bettingMatchTemp2.getId()),equalTo(0.0));
        
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
