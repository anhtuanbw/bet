package vn.kms.ngaythobet.domain.tournament;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.kms.ngaythobet.domain.core.MailService;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.UserRepository;
import vn.kms.ngaythobet.domain.util.DataInvalidException;
import vn.kms.ngaythobet.web.dto.AddNewMemberInfo;
import vn.kms.ngaythobet.web.dto.CreateGroupInfo;

/**
 * 
 * @author thangpham
 *
 */
@Service
public class GroupService {
    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

    private final GroupRepository groupRepo;
    private final UserRepository userRepo;
    private final TournamentRepository tournamentRepo;
    private final MailService mailService;
    
    @Autowired
    public GroupService(GroupRepository groupRepo, UserRepository userRepo, TournamentRepository tournamentRepo,
            MailService mailService) {
        this.groupRepo = groupRepo;
        this.userRepo = userRepo;
        this.tournamentRepo = tournamentRepo;
        this.mailService = mailService;
    }

    @Transactional
    public void createGroup(CreateGroupInfo createGroupInfo) {
        User moderator = userRepo.findOne(createGroupInfo.getModerator());
        Group group = new Group();
        List<User> members = new ArrayList<>();
        members.add(moderator);
        group.setName(createGroupInfo.getName());
        group.setModerator(moderator);
        group.setTournament(tournamentRepo.getOne(createGroupInfo.getTournamentId()));
        group.setMembers(members);
        groupRepo.save(group);
        mailService.sendMailToGroupModeratorAsync(moderator, group);
    }

    @Transactional
    public void addMember(AddNewMemberInfo newNemberInfo) {
        Group group = groupRepo.findOne(newNemberInfo.getGroupId());
        List<User> newMembers = newNemberInfo.getMemberIds().stream().map(id -> userRepo.getOne(id)).collect(Collectors.toList());
        Set<User> members = new HashSet<>( group.getMembers());
        for(User user : newMembers){
            if(members.contains(user)){
                throw new DataInvalidException("validation.list.user.not.new.message");
            }
        }
        group.getMembers().addAll(newMembers);
        groupRepo.save(group);
        newMembers.forEach(member -> mailService.sendMailToGroupMemberAsync(member, group));
    }
}
