package vn.kms.ngaythobet.domain.tournament;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.kms.ngaythobet.domain.core.User;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByModerator(User user);
}
