// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static vn.kms.ngaythobet.domain.util.Constants.WHITE_SPACE_REGEX;

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
import vn.kms.ngaythobet.domain.tournament.Group;
import vn.kms.ngaythobet.domain.tournament.GroupRepository;
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

    private Tournament createTournament(boolean isActivated) {
        Tournament tournament = new Tournament();
        tournament.setActivated(isActivated);
        tournament.setName("World Cup");
        return tournamentRepo.save(tournament);
    }

    @After
    public void clearData() {
        groupRepo.deleteAll();
        tournamentRepo.deleteAll();
    }

}
