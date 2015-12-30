// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import vn.kms.ngaythobet.BaseTest;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.UserRepository;
import vn.kms.ngaythobet.domain.core.UserService;
import vn.kms.ngaythobet.web.dto.CreateMatchInfo;
import vn.kms.ngaythobet.web.dto.UpdateScoreInfo;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MatchService matchService;

    @Autowired
    private RoundService roundService;

    @Autowired
    private UserService userService;

    private Tournament tournamentTemp;
    private Round roundTemp;
    private List<Competitor> competitors;
    private Competitor competitorTemp;
    private Competitor competitorTemp1;
    private Competitor competitorTemp2;
    private Competitor competitorTemp3;
    private Competitor competitorTemp4;
    private Match matchTemp;

    @Override
    protected void doStartUp() {
        // Create a tournament
        Tournament tournament = new Tournament();
        tournament.setActivated(true);
        tournament.setNumOfCompetitors(11L);
        tournament.setName("Euro");
        tournamentTemp = tournamentRepo.save(tournament);

        // Create a round belongs to above tournament
        Round round = new Round();
        round.setName("round 1");
        round.setTournament(tournamentTemp);
        round.setCompetitors(competitors);
        roundTemp = roundRepo.save(round);
        List<Round> rounds = new ArrayList<Round>();
        rounds.add(roundTemp);
        // Create competitors
        competitorTemp = createCompetitor("Brazil", tournamentTemp, rounds);
        competitorTemp1 = competitorRepo.save(competitorTemp);
        competitorTemp = createCompetitor("France", tournamentTemp, rounds);
        competitorTemp2 = competitorRepo.save(competitorTemp);
        competitorTemp = createCompetitor("England", tournamentTemp, rounds);
        competitorTemp3 = competitorRepo.save(competitorTemp);
        competitorTemp = createCompetitor("Germany", tournamentTemp, rounds);
        competitorTemp4 = competitorRepo.save(competitorTemp);

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

    private Competitor createCompetitor(String name, Tournament tournament,
            List<Round> rounds) {
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
