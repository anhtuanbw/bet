// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

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
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.UserRepository;
import vn.kms.ngaythobet.domain.core.UserService;
import vn.kms.ngaythobet.web.dto.CreateMatchInfo;
import vn.kms.ngaythobet.web.dto.MatchNotCreateBetInfo;
import vn.kms.ngaythobet.web.dto.UpdateScoreInfo;

;

public class MatchServiceTest extends BaseTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private TournamentRepository tournamentRepo;

    @Autowired
    private CompetitorRepository competitorRepo;

    @Autowired
    private RoundRepository roundRepo;

    @Autowired
    private MatchRepository matchRepo;

    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MatchService matchService;

    @Autowired
    private UserService userService;

    @Autowired
    private BettingMatchRepository bettingMatchRepo;

    private Tournament tournamentTemp;
    private Tournament tournamentTemp2;
    private Round roundTemp;
    private Group groupTemp;
    private List<Competitor> competitors;
    private Competitor competitorTemp1;
    private Competitor competitorTemp2;
    private Competitor competitorTemp3;
    private Competitor competitorTemp4;
    private Competitor competitorTemp5;
    private Match matchTemp;
    private Match matchTemp2;
    private BettingMatch bettingMatchTemp;
    private BettingMatch bettingMatchTemp2;

    @Override
    protected void doStartUp() {
        // Create 2 tournaments
        Tournament tournament = new Tournament();
        tournament.setActivated(true);
        tournament.setNumOfCompetitors(11L);
        tournament.setName("Euro");
        tournamentTemp = tournamentRepo.save(tournament);

        Tournament tournament2 = new Tournament();
        tournament.setActivated(true);
        tournament.setNumOfCompetitors(11L);
        tournament.setName("Champion League");
        tournamentTemp2 = tournamentRepo.save(tournament2);

        // Create a round belongs to above tournament
        Round round = new Round();
        round.setName("round 1");
        round.setTournament(tournamentTemp);
        roundTemp = roundRepo.save(round);
        List<Round> rounds = new ArrayList<Round>();
        rounds.add(roundTemp);
        // Create competitors
        competitorTemp1 = competitorRepo.save(createCompetitor("Brazil", tournamentTemp, rounds));
        competitorTemp2 = competitorRepo.save(createCompetitor("France", tournamentTemp, rounds));
        competitorTemp3 = competitorRepo.save(createCompetitor("England", tournamentTemp, rounds));
        competitorTemp4 = competitorRepo.save(createCompetitor("Germany", tournamentTemp, rounds));
        Competitor competitor = new Competitor(tournamentTemp2, "Real Madrid");
        competitorTemp5 = competitorRepo.save(competitor);

        competitors = competitorRepo.findAll();

        // Add competitors into round
        round.setCompetitors(competitors);
        roundTemp = roundRepo.save(round);

        User defaultUser = getDefaultUser();
        String username = defaultUser.getUsername();
        mockLoginUser(username);

        User user = userService.getUserInfo();

        // Create a group join above tournament
        Group group = new Group();
        group.setName("group 1");
        group.setTournament(tournamentTemp);
        group.setModerator(user);

        groupTemp = groupRepo.save(group);

        // Create a match
        Match match = new Match();
        match.setComment("abc");
        match.setCompetitor1(competitorTemp3);
        match.setCompetitor2(competitorTemp4);
        match.setLocation("HCM");
        match.setMatchTime(LocalDateTime.now());
        match.setRound(roundTemp);
        match.setScore1(1L);
        match.setScore2(2L);
        matchTemp = matchRepo.save(match);

        Match match2 = new Match();
        match2.setComment("match 2");
        match2.setCompetitor1(competitorTemp4);
        match2.setCompetitor2(competitorTemp3);
        match2.setLocation("Ha noi");
        match2.setMatchTime(LocalDateTime.now());
        match2.setRound(roundTemp);
        match2.setScore1(0L);
        match2.setScore2(0L);
        matchTemp2 = matchRepo.save(match2);
        //create betting Match
        BettingMatch bettingMatch = new BettingMatch();
        bettingMatch.setActivated(true);
        bettingMatch.setBalance1(new BigDecimal(0));
        bettingMatch.setBalance2(new BigDecimal(0.5));
        bettingMatch.setBetAmount(new BigDecimal(50));
        bettingMatch.setComment("betting match 1");
        bettingMatch.setGroup(group);
        bettingMatch.setExpiredTime(LocalDateTime.now());
        bettingMatch.setMatch(matchTemp);
        bettingMatch.setDescription("test 1 ");
        bettingMatchTemp = bettingMatchRepo.save(bettingMatch);

        BettingMatch bettingMatch2 = new BettingMatch();
        bettingMatch2.setActivated(true);
        bettingMatch2.setBalance1(new BigDecimal(0));
        bettingMatch2.setBalance2(new BigDecimal(0));
        bettingMatch2.setBetAmount(new BigDecimal(150));
        bettingMatch2.setComment("betting match 2");
        bettingMatch2.setGroup(group);
        bettingMatch2.setExpiredTime(LocalDateTime.now());
        bettingMatch2.setMatch(match2);
        bettingMatch2.setDescription("test 2 ");
        bettingMatchTemp2 = bettingMatchRepo.save(bettingMatch2);
    }

    private Competitor createCompetitor(String name, Tournament tournament, List<Round> rounds) {
        Competitor competitor = new Competitor();
        competitor.setName(name);
        competitor.setTournament(tournament);
        competitor.setRounds(rounds);
        return competitor;
    }

    @Test
    public void testCreateMatch() {
        CreateMatchInfo createMatchInfo = new CreateMatchInfo();
        createMatchInfo.setRound(roundTemp.getId());
        createMatchInfo.setCompetitor1(competitorTemp1.getId());
        createMatchInfo.setCompetitor2(competitorTemp2.getId());
        createMatchInfo.setTime(LocalDateTime.now());
        createMatchInfo.setLocation("HCM");
        createMatchInfo.setComment("abcdef");
        matchService.createMatch(createMatchInfo);
        assertThat(matchRepo.findAll().size(), equalTo(3));
    }

    @Test
    public void testCreateMatchWithEmptyRound() {
        CreateMatchInfo createMatchInfo = new CreateMatchInfo();
        createMatchInfo.setCompetitor1(competitorTemp1.getId());
        createMatchInfo.setCompetitor2(competitorTemp2.getId());
        createMatchInfo.setTime(LocalDateTime.now());
        createMatchInfo.setLocation("HCM");
        createMatchInfo.setComment("abcdef");
        matchService.createMatch(createMatchInfo);
        assertThat(matchRepo.findAll().size(), equalTo(3));
    }

    @Test
    public void testCreateMatchWithInvalidCompetitor() {
        CreateMatchInfo createMatchInfo = new CreateMatchInfo();
        createMatchInfo.setRound(roundTemp.getId());
        createMatchInfo.setCompetitor1(competitorTemp1.getId());
        createMatchInfo.setCompetitor2(competitorTemp5.getId());
        createMatchInfo.setTime(LocalDateTime.now());
        createMatchInfo.setLocation("HCM");
        createMatchInfo.setComment("abcdef");
        exception.expectMessage("exception.competitor.not-exist-tournament");
        matchService.createMatch(createMatchInfo);
        assertThat(matchRepo.findAll().size(), equalTo(1));
    }

    @Test
    public void testCreateMatchWithNotExistRound() {
        CreateMatchInfo createMatchInfo = new CreateMatchInfo();
        createMatchInfo.setRound((long) 100);
        createMatchInfo.setCompetitor1(competitorTemp1.getId());
        createMatchInfo.setCompetitor2(competitorTemp2.getId());
        createMatchInfo.setTime(LocalDateTime.now());
        createMatchInfo.setLocation("HCM");
        createMatchInfo.setComment("abcdef");
        exception.expectMessage("exception.round.not-existed.message");
        matchService.createMatch(createMatchInfo);
        assertThat(matchRepo.findAll().size(), equalTo(1));
    }

    @Test
    public void testGetMatchById() {
        assertThat(matchService.getMatch(matchTemp.getId()), notNullValue());
    }

    @Test
    public void testUpdateScore() {
        long score1 = 22;
        long score2 = 22;

        UpdateScoreInfo updateScoreInfo = new UpdateScoreInfo();
        updateScoreInfo.setMatchId(matchTemp.getId());
        updateScoreInfo.setCompetitor1Score(score1);
        updateScoreInfo.setCompetitor2Score(score2);
        matchService.updateScore(updateScoreInfo);

        Match match = matchRepo.findOne(matchTemp.getId());
        assertThat(match.getScore1(), equalTo(score1));
        assertThat(match.getScore2(), equalTo(score2));
    }

    @Test
    public void testUpdateScore2() {
        long score1 = 22;
        long score2 = 22;

        UpdateScoreInfo updateScoreInfo = new UpdateScoreInfo();
        updateScoreInfo.setMatchId(matchTemp.getId());
        updateScoreInfo.setCompetitor1Score(score1);
        updateScoreInfo.setCompetitor2Score(score2);
        matchService.updateScore(updateScoreInfo);

        Match match = matchRepo.findOne(matchTemp.getId());
        assertThat(match.getScore1(), equalTo(score1));
        assertThat(match.getScore2(), equalTo(score2));
    }

    @Test
    public void testGetMatchNotCreatedBettingMatch() {
        List<MatchNotCreateBetInfo> matchesNotBet = matchService.getMatchNotCreatedBettingMatch(tournamentTemp.getId(),
                groupTemp.getId());
        assertThat(matchesNotBet.size(), equalTo(0));
        Match match = new Match();
        match.setComment("test 3");
        match.setCompetitor1(competitorTemp3);
        match.setCompetitor2(competitorTemp4);
        match.setLocation("HCM");
        match.setMatchTime(LocalDateTime.now());
        match.setRound(roundTemp);
        match.setScore1(1L);
        match.setScore2(2L);
        matchRepo.save(match);
        matchesNotBet = matchService.getMatchNotCreatedBettingMatch(tournamentTemp.getId(),
                groupTemp.getId());
        assertThat(matchesNotBet.size(), equalTo(1));
    }

    @Test
    public void testcheckRounds() {
        boolean isCircle = matchService.checkRounds(tournamentTemp.getId());
        assertThat(isCircle, equalTo(true));
    }

    @After
    public void clearData() {
        bettingMatchRepo.deleteAll();
        matchRepo.deleteAll();
        roundRepo.deleteAll();
        groupRepo.deleteAll();
        userRepo.deleteAll();
        competitorRepo.deleteAll();
        tournamentRepo.deleteAll();
    }
}
