// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.nullValue;
import static vn.kms.ngaythobet.domain.core.ChangeLog.Action.DELETE;
import static vn.kms.ngaythobet.domain.core.ChangeLog.Action.INSERT;
import static vn.kms.ngaythobet.domain.core.ChangeLog.Action.UPDATE;
import static vn.kms.ngaythobet.domain.core.User.Role.ADMIN;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import vn.kms.ngaythobet.BaseTest;
import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.betting.BettingMatchRepository;
import vn.kms.ngaythobet.domain.betting.BettingMatchService;
import vn.kms.ngaythobet.domain.betting.BettingPlayer;
import vn.kms.ngaythobet.domain.betting.BettingPlayerRepository;
import vn.kms.ngaythobet.domain.core.ChangeLog.Action;
import vn.kms.ngaythobet.domain.core.ChangeLog.Change;
import vn.kms.ngaythobet.domain.core.User.Role;
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
import vn.kms.ngaythobet.web.dto.CommentInfo;

public class ChangeLogTest extends BaseTest {

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

    @Autowired
    private BettingPlayerRepository bettingPlayerRepo;

    @Autowired
    private ChangeLogService changeLogService;

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
    public void startUp() {
        mockLoginUser("admin");

        // create 2 users
        User user1 = createUser(true, "a@abc.com", "a", "123467", Role.USER, "a");
        User user2 = createUser(true, "b@abc.com", "b", "123467", Role.USER, "b");
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

    @Override
    public void tearDown() {
        userRepo.deleteAll();
        changeLogRepo.deleteAll();
    }

    @Test
    public void testAuditFieldsCorrect() throws InterruptedException {
        User user = makeUser("tester");
        user = userRepo.save(user);
        Thread.sleep(1);
        assertThat(user.getCreatedAt(), lessThan(LocalDateTime.now()));
        assertThat(user.getCreatedBy(), equalTo("admin"));
        assertThat(user.getModifiedAt(), nullValue());
        assertThat(user.getModifiedBy(), nullValue());

        user.setName("tester2");
        user = userRepo.save(user);
        Thread.sleep(1);
        assertThat(user.getCreatedAt(), lessThan(user.getModifiedAt()));
        assertThat(user.getCreatedBy(), equalTo("admin"));
        assertThat(user.getModifiedAt(), lessThan(LocalDateTime.now()));
        assertThat(user.getModifiedBy(), equalTo("admin"));
    }

    @Test
    public void testCUDChangeLogs() throws InterruptedException {
        // scenario: create, update twice and then delete user
        User user = makeUser("tester");

        mockLoginUser("user1");
        userRepo.save(user);
        Thread.sleep(1);

        mockLoginUser("user2");
        user.setName("Tester2 User");
        user.setRole(ADMIN);
        user.setActivationKey("abcdef");
        userRepo.save(user);
        Thread.sleep(1);

        mockLoginUser("user3");
        user.setActivationKey("1234567890");
        user.setPassword("tester2@123");
        userRepo.save(user);
        Thread.sleep(1);

        mockLoginUser("user4");
        userRepo.delete(user);
        Thread.sleep(1);

        // verify the change logs
        List<ChangeLog> logs = changeLogRepo.findAll(new Sort("timestamp"));
        Map<String, Change> expectedChanges;
        assertThat(logs.size(), equalTo(12));

        ChangeLog createLog = logs.get(8);
        expectedChanges = null;
        assertLogIsCorrect(createLog, "user1", INSERT, user.getClass(), user.getId(), expectedChanges);

        ChangeLog update1Log = logs.get(9);
        expectedChanges = new HashMap<>();
        expectedChanges.put("name", new Change("tester User", "Tester2 User"));
        expectedChanges.put("role", new Change("USER", "ADMIN"));
        expectedChanges.put("activationKey", new Change(null, "abcdef"));
        assertLogIsCorrect(update1Log, "user2", UPDATE, user.getClass(), user.getId(), expectedChanges);

        ChangeLog update2Log = logs.get(10);
        expectedChanges = new HashMap<>();
        expectedChanges.put("activationKey", new Change("abcdef", "1234567890"));
        expectedChanges.put("password", new Change("Tester@123", "tester2@123"));
        assertLogIsCorrect(update2Log, "user3", UPDATE, user.getClass(), user.getId(), expectedChanges);

        ChangeLog deleteLog = logs.get(11);
        expectedChanges = null;
        assertLogIsCorrect(deleteLog, "user4", DELETE, user.getClass(), user.getId(), expectedChanges);
    }

    @Test
    public void testGetCommentCount() {
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

        BettingPlayer bettingPlayer = new BettingPlayer();
        bettingPlayer.setPlayer(userTemp1);
        bettingPlayer.setBettingMatch(bettingMatchTemp);
        bettingPlayer.setBetCompetitor(competitorTemp1);

        bettingPlayerTemp = bettingPlayerRepo.save(bettingPlayer);

        List<ChangeLog> changelogs = changeLogRepo.findByEntityTypeAndEntityId(BettingMatch.class.getCanonicalName(),
                bettingMatchTemp.getId());
        List<ChangeLog> changelogsHistoryBetting = changeLogRepo.findByEntityType(BettingPlayer.class
                .getCanonicalName());

        int size = 0;
        for (ChangeLog c : changelogsHistoryBetting) {
            if (bettingPlayerRepo.findOne(c.getEntityId()).getBettingMatch().getId().equals(bettingMatchTemp.getId())) {
                size++;
            }
        }

        assertThat(changelogs.size() + size, equalTo(2));
    }

    @Test
    public void testGetComments() {
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
        
        bettingMatchTemp.setComment("abc");
        bettingMatchRepo.save(bettingMatchTemp);

        BettingPlayer bettingPlayer = new BettingPlayer();
        bettingPlayer.setPlayer(userTemp1);
        bettingPlayer.setBettingMatch(bettingMatchTemp);
        bettingPlayer.setBetCompetitor(competitorTemp1);
        bettingPlayerTemp = bettingPlayerRepo.save(bettingPlayer);

        List<CommentInfo> comments = changeLogService.getComments(bettingMatchTemp.getId(), 0);
        assertThat(comments.size(), equalTo(2));
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

    private void assertLogIsCorrect(ChangeLog actualLog, String expectedUsername, Action expectedAction,
            Class<?> expectedType, long expectedEntityId, Map<String, Change> expectedChanges) {
        assertThat(actualLog.getUsername(), equalTo(expectedUsername));
        assertThat(actualLog.getAction(), equalTo(expectedAction));
        assertThat(actualLog.getEntityType(), equalTo(expectedType.getName()));
        assertThat(actualLog.getEntityId(), equalTo(expectedEntityId));

        if (expectedChanges == null) {
            assertThat(actualLog.getEntityChanges(), nullValue());
        } else {
            Map<String, Change> actualChanges = actualLog.getEntityChanges();
            assertThat(actualChanges.size(), equalTo(expectedChanges.size()));
            expectedChanges.entrySet().forEach(
                    expectedEntry -> assertThat(actualChanges.get(expectedEntry.getKey()),
                            equalTo(expectedEntry.getValue())));
        }
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
