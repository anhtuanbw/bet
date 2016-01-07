package vn.kms.ngaythobet.domain.tournament;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.kms.ngaythobet.domain.core.User;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByModerator(User user);
    Optional<Group> findByIdAndModerator(Long groupId, User moderator);
    List<Group> findByMembers(User user);
    Group findByIdAndMembers(Long groupId, User user);
    
}
