// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.core;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    User findOneByActivationKey(String activationKey);

    User findOneByResetKey(String resetKey);

    User findOneByEmail(String email);

    Optional<User> findOneByUsername(String username);
}
