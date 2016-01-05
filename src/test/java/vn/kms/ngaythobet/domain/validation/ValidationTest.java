// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static vn.kms.ngaythobet.domain.util.Constants.WHITE_SPACE_REGEX;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import vn.kms.ngaythobet.BaseTest;
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

public class ValidationTest extends BaseTest {
    @Autowired
    private Validator validator;

    @Autowired
    private TournamentRepository tournamentRepo;

    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MatchRepository matchRepo;

    @Autowired
    private CompetitorRepository competitorRepo;

    @Autowired
    private RoundRepository roundRepo;

    @Test
    public void testEntityExistValidation() {
        UserData data = new UserData("test", "test@test.local", "Test@123", "Test@123", -1);
        Set<ConstraintViolation<UserData>> violations = validator.validate(data);
        assertThat(violations.size(), equalTo(1));

        ConstraintViolation<UserData> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString(), equalTo("manager"));
        assertThat(violation.getMessage(), equalTo("is not existed"));

        data.manager = getDefaultUser().getId();
        violations = validator.validate(data);
        assertThat(violations.size(), equalTo(0));
    }

    @Test
    public void testFieldMatchValidation() {
        UserData data = new UserData("test", "test@test.local", "Test@123", "Test@12", getDefaultUser().getId());
        Set<ConstraintViolation<UserData>> violations = validator.validate(data);
        assertThat(violations.size(), equalTo(1));

        ConstraintViolation<UserData> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString(), equalTo("confirmPassword"));
        assertThat(violation.getMessage(), equalTo("is not matched"));

        data.confirmPassword = "Test@123";
        violations = validator.validate(data);
        assertThat(violations.size(), equalTo(0));
    }

    @Test
    public void testFieldUniqueValidation() {
        User defaultUser = getDefaultUser();

        UserData data = new UserData(defaultUser.getUsername(), "test@test.local", "Test@123", "Test@123",
                defaultUser.getId());
        Set<ConstraintViolation<UserData>> violations = validator.validate(data);
        assertThat(violations.size(), equalTo(1));

        ConstraintViolation<UserData> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString(), equalTo("username"));
        assertThat(violation.getMessage(), equalTo("'tester' is already existed"));

        data.username = defaultUser.getUsername() + "123";
        violations = validator.validate(data);
        assertThat(violations.size(), equalTo(0));
    }

    @Test
    public void testMutipleFieldsValidation() {
        UserData data = new UserData(getDefaultUser().getUsername(), "abc.com", "Test@ 123", "456", -1);
        Set<ConstraintViolation<UserData>> violations = validator.validate(data);
        assertThat(violations.size(), equalTo(5));
        violations.forEach(violation -> {
            switch (violation.getPropertyPath().toString()) {
            case "username":
                assertThat(violation.getMessage(), equalTo("'tester' is already existed"));
                break;
            case "email":
                assertThat(violation.getMessage(), equalTo("email not valid"));
                break;
            case "password":
                assertThat(violation.getMessage(), equalTo("Password must contain at least six of uppercase, "
                        + "lowercase letters, numbers and special characters"));
                break;
            case "confirmPassword":
                assertThat(violation.getMessage(), equalTo("is not matched"));
                break;
            case "manager":
                assertThat(violation.getMessage(), equalTo("is not existed"));
                break;
            }
        });
    }

    @Test
    public void testListUniqueAndWhiteSpaceValidation() {
        Tournament tournament = createTournament(true);

        List<Long> competitorIds = new ArrayList<>();
        competitorIds.add((long) 1);
        competitorIds.add((long) 1);
        competitorIds.add((long) 2);
        TournamentData tournamentData = new TournamentData("   test white space     ", competitorIds,
                tournament.getId());
        Set<ConstraintViolation<TournamentData>> violations = validator.validate(tournamentData);
        assertThat(violations.size(), equalTo(2));
    }

    @Test
    public void testEntityValidatedValidation() {
        Tournament tournament = createTournament(false);

        List<Long> competitorIds = new ArrayList<>();
        competitorIds.add((long) 1);
        competitorIds.add((long) 2);

        TournamentData tournamentData = new TournamentData("World Cup", competitorIds, tournament.getId());
        Set<ConstraintViolation<TournamentData>> violations = validator.validate(tournamentData);
        assertThat(violations.size(), equalTo(1));
        ConstraintViolation<TournamentData> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString(), equalTo("tournamentId"));
        assertThat(violation.getMessage(), equalTo("Not activated"));
    }

    @Test
    public void testModeratorAccessValidation() {
        Tournament tournament = createTournament(true);

        User tester = makeUser("Tester");
        userRepo.save(tester);

        List<Long> memberIds = new ArrayList<>();
        memberIds.add(getDefaultUser().getId());
        memberIds.add(tester.getId());

        Group group = new Group();
        group.setName("Launch 4");
        group.setModerator(getDefaultUser());
        group.setMembers(Collections.emptyList());
        group.setTournament(tournament);
        groupRepo.save(group);

        mockLoginUser(getDefaultUser().getUsername());
        MembersData membersData = new MembersData(group.getId(), memberIds);
        Set<ConstraintViolation<MembersData>> violations = validator.validate(membersData);
        violations = validator.validate(membersData);
        assertThat(violations.size(), equalTo(0));

        mockLoginUser(tester.getUsername());
        violations = validator.validate(membersData);
        assertThat(violations.size(), equalTo(1));
        ConstraintViolation<MembersData> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString(), equalTo("groupId"));
        assertThat(violation.getMessage(), equalTo("Access deny, you are not moderator"));
    }

    @Test
    public void testListEntityExistValidation() {
        Tournament tournament = createTournament(true);

        List<Long> memberIds = new ArrayList<>();
        memberIds.add(getDefaultUser().getId());
        memberIds.add((long) 99999);

        Group group = new Group();
        group.setName("Launch 4");
        group.setModerator(getDefaultUser());
        group.setMembers(Collections.emptyList());
        group.setTournament(tournament);
        groupRepo.save(group);

        mockLoginUser(getDefaultUser().getUsername());
        MembersData membersData = new MembersData(group.getId(), memberIds);
        Set<ConstraintViolation<MembersData>> violations = validator.validate(membersData);
        violations = validator.validate(membersData);
        assertThat(violations.size(), equalTo(1));
        ConstraintViolation<MembersData> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString(), equalTo("memberIds"));
        assertThat(violation.getMessage(), equalTo("Entity is not existed"));
    }

    @Test
    public void testExpritedTimeValidation() {
        Tournament tournament = createTournament(true);

        Group group = new Group();
        group.setName("Launch 4");
        group.setModerator(getDefaultUser());
        group.setMembers(Collections.emptyList());
        group.setTournament(tournament);
        groupRepo.save(group);

        Competitor competitorA = new Competitor();
        competitorA.setName("MU");
        competitorA.setTournament(tournament);
        competitorRepo.save(competitorA);

        Competitor competitorB = new Competitor();
        competitorB.setName("Arsenal");
        competitorB.setTournament(tournament);
        competitorRepo.save(competitorB);

        Round round = new Round();
        round.setName("Final");
        round.setTournament(tournament);
        roundRepo.save(round);

        Match match = new Match();
        LocalDateTime matchTime = LocalDateTime.now();
        match.setMatchTime(matchTime);
        match.setLocation("CH Office");
        match.setCompetitor1(competitorA);
        match.setCompetitor2(competitorB);
        match.setRound(round);
        matchRepo.save(match);

        BettingMatchData bettingMatchData = new BettingMatchData(match.getId(), matchTime.plusMinutes(10));
        Set<ConstraintViolation<BettingMatchData>> violations = validator.validate(bettingMatchData);
        violations = validator.validate(bettingMatchData);
        assertThat(violations.size(), equalTo(1));
        ConstraintViolation<BettingMatchData> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString(), equalTo("expiredTime"));
        assertThat(violation.getMessage(), equalTo("Time is not valid"));

        bettingMatchData = new BettingMatchData(match.getId(), matchTime.minusMinutes(10));
        violations = validator.validate(bettingMatchData);
        assertThat(violations.size(), equalTo(0));
    }

    @Test
    public void testListEntityJoinedValidation(){
        Tournament tournament = createTournament(true);
        Tournament tournament2 = createTournament(true);
        Competitor competitor1 = new Competitor(tournament, "England");
        Competitor competitor2 = new Competitor(tournament, "France");
        Competitor competitor3 = new Competitor(tournament2, "abc");
        competitorRepo.save(competitor1);
        competitorRepo.save(competitor2);
        competitorRepo.save(competitor3);
        List<Long> competitorIds = new ArrayList<>();
        competitorIds.add(competitor1.getId());
        competitorIds.add(competitor2.getId());
        competitorIds.add(competitor3.getId());
        RoundData roundData = new RoundData(tournament.getId(), competitorIds);
        Set<ConstraintViolation<RoundData>> violations = validator.validate(roundData);
        violations = validator.validate(roundData);
        assertThat(violations.size(), equalTo(1));
        ConstraintViolation<RoundData> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString(), equalTo("competitorIds"));
        assertThat(violation.getMessage(), equalTo("is not joined"));
        
    }

    @Test
    public void testFieldNotMatchValidation() {
        MatchData matchData = new MatchData((long) 1, (long) 1);
        Set<ConstraintViolation<MatchData>> violations = validator.validate(matchData);
        assertThat(violations.size(), equalTo(1));
        ConstraintViolation<MatchData> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString(), equalTo("competitor2"));
        assertThat(violation.getMessage(), equalTo("Competitors must be different"));
    }

    static class TournamentData {
        @Pattern(regexp = WHITE_SPACE_REGEX, message = "{validation.pattern.blankspace}")
        String tournamentName;
        @ListUnique
        List<Long> competitorIds;
        @EntityActivated(type = Tournament.class)
        Long tournamentId;

        public TournamentData(String tournamentName, List<Long> competitorIds, Long tournamentId) {
            this.tournamentName = tournamentName;
            this.competitorIds = competitorIds;
            this.tournamentId = tournamentId;
        }
    }

    @FieldMatch(firstField = "password", secondField = "confirmPassword")
    static class UserData {
        @FieldUnique(field = "username", entity = User.class)
        String username;

        @Email
        String email;

        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[#$%&'()*+,-./:!<=>?@\\^_`\\[\\]{|}~;])\\S{6,50}$", message = "{validation.password.message}")
        String password;

        @NotEmpty
        String confirmPassword;

        @EntityExist(type = User.class)
        long manager;

        UserData(String username, String email, String password, String confirmPassword, long manager) {
            this.username = username;
            this.email = email;
            this.password = password;
            this.confirmPassword = confirmPassword;
            this.manager = manager;
        }
    }

    @ListEntityJoinedValid(entities = "competitorIds", entityId = "tournamentId", fieldName = "competitors", type = Tournament.class)
    static class RoundData{
        Long tournamentId;
        List<Long> competitorIds;
        public RoundData(Long tournamentId, List<Long> competitorIds) {
          this.tournamentId = tournamentId;
          this.competitorIds = competitorIds;
        }
    }

    @ModeratorAccess
    static class MembersData {
        Long groupId;

        @ListEntityExist(type = User.class)
        List<Long> memberIds;

        public MembersData(Long groupId, List<Long> memberIds) {
            this.groupId = groupId;
            this.memberIds = memberIds;
        }
    }

    @NotExpiredTime(entityId = "matchId", targetField = "expiredTime", fieldName = "matchTime", type = Match.class)
    static class BettingMatchData {
        Long matchId;
        LocalDateTime expiredTime;

        BettingMatchData(Long matchId, LocalDateTime expiredTime) {
            this.matchId = matchId;
            this.expiredTime = expiredTime;
        };
    }

    @FieldNotMatch(firstField = "competitor1", secondField = "competitor2")
    static class MatchData {
        Long competitor1;
        Long competitor2;

        MatchData(Long competitor1, Long competitor2) {
            this.competitor1 = competitor1;
            this.competitor2 = competitor2;
        }
    }

    private Tournament createTournament(boolean isActivated) {
        Tournament tournament = new Tournament();
        tournament.setActivated(isActivated);
        tournament.setName("World Cup");
        return tournamentRepo.save(tournament);
    }

    @After
    public void clearData() {
        groupRepo.deleteAll();
        matchRepo.deleteAll();
        competitorRepo.deleteAll();
        roundRepo.deleteAll();
        tournamentRepo.deleteAll();
    }

}
