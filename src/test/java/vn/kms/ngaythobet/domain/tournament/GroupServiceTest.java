package vn.kms.ngaythobet.domain.tournament;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import vn.kms.ngaythobet.BaseTest;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.User.Role;
import vn.kms.ngaythobet.domain.util.DataInvalidException;
import vn.kms.ngaythobet.web.dto.AddNewMemberInfo;
import vn.kms.ngaythobet.web.dto.CheckModeratorInfo;
import vn.kms.ngaythobet.web.dto.CreateGroupInfo;

/**
 * 
 * @author thangpham
 *
 */
public class GroupServiceTest extends BaseTest {
    @Autowired
    private TournamentRepository tournamentRepo;
    @Autowired
    private GroupRepository groupRepo;
    @Autowired
    private GroupService groupService;

    private User user;
    private Tournament tournament;

    @Override
    protected void doStartUp() {
        tournament = new Tournament();
        tournament.setActivated(true);
        tournament.setName("World Cup");
        tournament.setNumOfCompetitor((long) 32);
        tournament.setGroups(Collections.emptyList());
        tournamentRepo.save(tournament);

        user = getDefaultUser();
        createGroup();
    }

    @Test
    public void testCreateGroup() {

        List<Group> groups = groupRepo.findAll();
        assertThat(1, equalTo(groups.size()));
        assertThat(groups.get(0).getName(), equalTo("Launch 4"));
        assertThat(groups.get(0).getModerator().getUsername(), equalTo(user.getUsername()));
        assertThat(groups.get(0).getTournament().getName(), equalTo(tournament.getName()));

        try {
            createGroup();
        } catch (DataInvalidException exception) {
            assertThat(exception.getMessage(), equalTo("{validation.group.existName.message}"));
        }
    }

    @Test
    @Transactional
    public void testAddMember() {
        List<Group> groups = groupRepo.findAll();
        List<Long> memberIds = new ArrayList<>();
        AddNewMemberInfo addNewMemberInfo = new AddNewMemberInfo();

        memberIds.add(getDefaultUser().getId());
        addNewMemberInfo.setGroupId(groups.get(0).getId());
        addNewMemberInfo.setMemberIds(memberIds);

        try {
            groupService.addMember(addNewMemberInfo);
        } catch (DataInvalidException exception) {
            assertThat(exception.getMessage(), equalTo("{validation.list.user.not.new.message}"));
        }

        User tester2 = makeUser("Tester2");
        userRepo.save(tester2);
        memberIds = new ArrayList<>();
        memberIds.add(tester2.getId());
        addNewMemberInfo.setMemberIds(memberIds);

        groupService.addMember(addNewMemberInfo);
        groups = groupRepo.findAll();
        assertThat(2, equalTo(groups.get(0).getMembers().size()));
    }

    @Test
    public void testCheckModeratorPermission() {
        User tester3 = makeUser("Tester3");
        userRepo.save(tester3);
        List<Group> groups = groupRepo.findAll();

        CheckModeratorInfo checkModeratorInfo = new CheckModeratorInfo();
        checkModeratorInfo.setGroupId(groups.get(0).getId());
        checkModeratorInfo.setUserId(tester3.getId());

        try {
            groupService.checkModeratorPermission(checkModeratorInfo);
        } catch (DataInvalidException exception) {
            assertThat(exception.getMessage(), equalTo("{validation.moderator.access.deny.message}"));
        }
    }

    @Test
    public void testCheckGroupByRole() {
        User tester4 = makeUser("Tester4");
        userRepo.save(tester4);
        User admin = makeUser("admin");
        admin.setRole(Role.ADMIN);
        userRepo.save(admin);

        mockLoginUser(user);
        List<Group> groups = groupService.getGroupByRole(tournament.getId());
        assertThat(1, equalTo(groups.size()));

        mockLoginUser(tester4);
        groups = groupService.getGroupByRole(tournament.getId());
        assertThat(0, equalTo(groups.size()));

        mockLoginUser(admin);
        groups = groupService.getGroupByRole(tournament.getId());
        assertThat(1, equalTo(groups.size()));

    }

    @Test
    public void testCheckMemberPermission() {
        User tester5 = makeUser("Tester5");
        userRepo.save(tester5);

        Group group = new Group();
        group.setMembers(Arrays.asList(getDefaultUser()));
        group.setModerator(tester5);
        group.setName("Launch 4");
        group.setTournament(tournament);
        groupRepo.save(group);

        mockLoginUser(tester5);
        try {
            groupService.checkMemberPermission(group.getId());
        } catch (DataInvalidException exception) {
            assertThat(exception.getMessage(), equalTo("{exception.unauthorized}"));
        }
    }

    @After
    public void deteleData() {
        groupRepo.deleteAll();
        tournamentRepo.deleteAll();
    }

    private void createGroup() {
        CreateGroupInfo createGroupInfo = new CreateGroupInfo();
        createGroupInfo.setName("Launch 4");
        createGroupInfo.setTournamentId(tournament.getId());
        createGroupInfo.setModerator(user.getId());
        groupService.createGroup(createGroupInfo);
    }

}
