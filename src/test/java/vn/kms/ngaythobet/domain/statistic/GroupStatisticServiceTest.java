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
import vn.kms.ngaythobet.CreateData;
import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.betting.BettingMatchRepository;
import vn.kms.ngaythobet.domain.betting.BettingPlayer;
import vn.kms.ngaythobet.domain.betting.BettingPlayerRepository;
import vn.kms.ngaythobet.domain.betting.BettingPlayerService;
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

public class GroupStatisticServiceTest extends BaseTest {
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

    @Autowired
    private GroupStatisticService groupStatisticService;

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
        // add 2 users
        User user1 = CreateData.createUser(true, "email@yahoo.com", "user1", "123467", "user1");
        User user2 = CreateData.createUser(true, "email2@yahoo.com", "user2", "123467", "user2");
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
        List<Round> rounds = new ArrayList<>();
        rounds.add(roundTemp);
        competitor1.setRounds(rounds);
        competitor2.setRounds(rounds);
        competitorTemp1 = competitorRepo.save(competitor1);
        competitorTemp2 = competitorRepo.save(competitor2);
        // add match
        Match match = CreateData.createMatch(competitorTemp1, competitorTemp2, "location test", LocalDateTime.now(),
                roundTemp);
        matchTemp = matchRepo.save(match);
        // add group
        List<User> members = new ArrayList<>();
        members.add(userTemp1);
        members.add(userTemp2);
        Group group = CreateData.createGroup(userTemp1, "testGroup", tournamentTemp, members);
        groupTemp = groupRepo.save(group);
        // add betting match
        BettingMatch bettingMatch = createBettingMatch(true, matchTemp, groupTemp, LocalDateTime.now().plusDays(30),
                new BigDecimal(1), new BigDecimal(0), new BigDecimal(100));
        bettingMatchTemp = bettingMatchRepo.save(bettingMatch);
        // add player betting match
        BettingPlayer bettingPlayer = CreateData.createBettingPlayer(bettingMatchTemp, userTemp1, competitorTemp2);
        bettingPlayerTemp = bettingPlayerRepo.save(bettingPlayer);
        List<BettingPlayer> bettingPlayers = new ArrayList<>();
        bettingPlayers.add(bettingPlayerTemp);
        bettingMatch.setBettingPlayers(bettingPlayers);
        bettingMatchTemp = bettingMatchRepo.save(bettingMatch);
    }

    @Test
    public void testGetGroupStatistic() {
        User user = makeUser("user1");
        mockLoginUser(user);
        List<GroupStatistic> groupStatistics = groupStatisticService.getGroupStatistic(groupTemp.getId());
        assertThat(groupStatistics.size(), equalTo(2));
        assertThat(groupStatistics.get(0).getNotbetCount(), equalTo(0));
    }

    private BettingMatch createBettingMatch(boolean active, Match match, Group group, LocalDateTime expiredTime,
            BigDecimal balance1, BigDecimal balance2, BigDecimal betAmount) {
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
