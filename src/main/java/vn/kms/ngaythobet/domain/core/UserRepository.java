// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.core;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    User findOneByActivationKey(String activationKey);

    User findOneByResetKey(String resetKey);

    User findOneByEmail(String email);

    Optional<User> findOneByUsername(String username);
    
    @Query("select u from User u where upper(u.name) like upper(?1) and u.activated = true")
    List<User> findTop10ByNameContainingIgnoreCase(String name, Pageable pageable);
}
