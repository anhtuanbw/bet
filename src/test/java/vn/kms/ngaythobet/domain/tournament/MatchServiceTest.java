// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

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
import vn.kms.ngaythobet.domain.core.UserRepository;
import vn.kms.ngaythobet.domain.core.UserService;
import vn.kms.ngaythobet.web.dto.CreateMatchInfo;
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

    private Tournament tournamentTemp;
    private Tournament tournamentTemp2;
    private Round roundTemp;
    private List<Competitor> competitors;
    private Competitor competitorTemp1;
    private Competitor competitorTemp2;
    private Competitor competitorTemp3;
    private Competitor competitorTemp4;
    private Competitor competitorTemp5;
    private Match matchTemp;

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

        groupRepo.save(group);

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
        assertThat(matchRepo.findAll().size(), equalTo(2));
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
        assertThat(matchRepo.findAll().size(), equalTo(2));
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

    @After
    public void clearData() {
        matchRepo.deleteAll();
        roundRepo.deleteAll();
        groupRepo.deleteAll();
        userRepo.deleteAll();
        competitorRepo.deleteAll();
        tournamentRepo.deleteAll();
    }
}
