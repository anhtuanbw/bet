package vn.kms.ngaythobet.domain.tournament;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import vn.kms.ngaythobet.BaseTest;
import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.betting.BettingMatchRepository;
import vn.kms.ngaythobet.domain.betting.BettingMatchService;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.User.Role;
import vn.kms.ngaythobet.domain.core.UserRepository;
import vn.kms.ngaythobet.web.dto.CreateBettingMatchInfo;
import vn.kms.ngaythobet.web.dto.UpdateBettingMatchInfo;

public class BettingMatchServiceTest extends BaseTest {

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
        User user1 = new User();
        user1.setActivated(true);
        user1.setEmail("hieu1@abc.com");
        user1.setName("hieu1");
        user1.setPassword("123467");
        user1.setRole(Role.USER);
        user1.setUsername("hieu1");
        User user2 = new User();
        user2.setActivated(true);
        user2.setEmail("hieu2@abc.com");
        user2.setName("hieu2");
        user2.setPassword("123467");
        user2.setRole(Role.USER);
        user2.setUsername("hieu2");
        userTemp1 = userRepo.save(user1);
        userTemp2 = userRepo.save(user2);
        // create tournament
        Tournament tournament = new Tournament();
        tournament.setActivated(false);
        tournament.setName("worldcup");
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
        Round round = new Round();
        round.setTournament(tournamentTemp);
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
        // create group
        Group group = new Group();
        List<User> members = new ArrayList<>();
        members.add(userTemp1);
        members.add(userTemp2);
        group.setMembers(members);
        group.setModerator(userTemp1);
        group.setName("testGroup");
        group.setTournament(tournamentTemp);
        groupTemp = groupRepo.save(group);

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
        BettingMatch bettingMatch = new BettingMatch();
        bettingMatch.setActivated(true);
        bettingMatch.setBalance1(new BigDecimal("0"));
        bettingMatch.setBalance2(new BigDecimal("0"));
        bettingMatch.setBetAmount(new BigDecimal("0"));
        bettingMatch.setExpiredTime(LocalDateTime.now());
        bettingMatch.setGroup(groupTemp);
        bettingMatch.setMatch(matchTemp);
        bettingMatch.setDescription("test");
        bettingMatchTemp = bettingMatchRepo.save(bettingMatch);

        UpdateBettingMatchInfo updateBettingMatchInfo = new UpdateBettingMatchInfo();
        updateBettingMatchInfo.setBalance1(new BigDecimal(1));
        updateBettingMatchInfo.setBalance2(new BigDecimal(0));
        updateBettingMatchInfo.setActivated(false);
        updateBettingMatchInfo.setBetAmount(new BigDecimal("999999"));
        updateBettingMatchInfo.setBettingMatchId(bettingMatchTemp.getId());
        updateBettingMatchInfo.setDecription("test again");
        updateBettingMatchInfo.setExpiredTime(LocalDateTime.now());
        updateBettingMatchInfo.setGroupId(groupTemp.getId());
        updateBettingMatchInfo.setMatchId(matchTemp.getId());
        bettingMatchService.updateBettingMatch(updateBettingMatchInfo);
        assertThat(bettingMatchRepo.findAll().size(), equalTo(1));
        assertThat(bettingMatchRepo.findOne(bettingMatchTemp.getId()).getDescription(), equalTo("test again"));
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
