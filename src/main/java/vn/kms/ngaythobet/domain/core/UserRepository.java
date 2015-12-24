// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.core;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    User findOneByActivationKey(String activationKey);

    User findOneByResetKey(String resetKey);

    User findOneByEmail(String email);

    Optional<User> findOneByUsername(String username);

    List<User> findByNameContainingIgnoreCase(String name);
}
