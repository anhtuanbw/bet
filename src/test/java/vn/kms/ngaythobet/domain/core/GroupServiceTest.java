package vn.kms.ngaythobet.domain.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import vn.kms.ngaythobet.BaseTest;
import vn.kms.ngaythobet.domain.tournament.Group;
import vn.kms.ngaythobet.domain.tournament.GroupRepository;
import vn.kms.ngaythobet.domain.tournament.GroupService;
import vn.kms.ngaythobet.domain.tournament.Tournament;
import vn.kms.ngaythobet.domain.tournament.TournamentRepository;
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

    @Test
    public void testCreateGroup() {
        Tournament tournament = new Tournament();
        tournament.setActivated(true);
        tournament.setName("World Cup");
        tournament.setNumOfCompetitor((long) 32);
        Tournament temp = tournamentRepo.save(tournament);

        User user = getDefaultUser();
        CreateGroupInfo createGroupInfo = new CreateGroupInfo();
        createGroupInfo.setName("Launch 4");
        createGroupInfo.setTournamentId(temp.getId());
        createGroupInfo.setModerator(user.getUsername());
        groupService.createGroup(createGroupInfo);

        Group group = groupRepo.findOne((long) 1);
        assertThat(group.getName(), is(equalTo("Launch 4")));
        assertThat(group.getModerator().getUsername(), is(equalTo(user.getUsername())));
        assertThat(group.getTournament().getName(), is(equalTo(tournament.getName())));

        groupRepo.delete(group);
        tournamentRepo.delete(tournament);
    }
}
