// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import vn.kms.ngaythobet.domain.util.DataInvalidException;
import vn.kms.ngaythobet.domain.util.SecurityUtil;
import vn.kms.ngaythobet.web.dto.ChangePasswordInfo;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional(readOnly = true)
public class UserService {
    private Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepo;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final Random random = new Random();

    @Autowired
    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder,
                       MailService mailService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Transactional
    public void registerUser(String username, String password, String email, String name, String languageTag) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setName(name);
        user.setLanguageTag(languageTag);
        user.setRole(User.Role.USER);
        user.setActivated(false);
        user.setActivationKey(generateRandomKey());
        userRepo.save(user);

        mailService.sendActivationEmailAsync(user);
    }

    @Transactional
    public void activateRegistration(String activationKey, LocalDateTime currentTime) {
        logger.debug("Activating user for activation key {}", activationKey);

        User user = userRepo.findOneByActivationKey(activationKey);
        if (user == null) {
            throw new DataInvalidException("exception.userService.activation-key-invalid");
        }

        // Not allow to activate registration after sending activationKey 7 days
        LocalDateTime sevenDayAgo = currentTime.minusDays(7);
        if (user.getCreatedAt().isBefore(sevenDayAgo)) {
            throw new DataInvalidException("exception.userService.activation-key-expired");
        }

        user.setActivationKey(null);
        user.setActivated(true);
        userRepo.save(user);
        logger.debug("Activated user: {}", user);
    }

    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepo.findOneByEmail(email);
        if (user == null) {
            throw new DataInvalidException("exception.userService.email-invalid");
        }

        if (!user.isActivated()) {
            throw new DataInvalidException("exception.userService.user-not-activated");
        }

        user.setResetKey(generateRandomKey());
        user.setResetTime(LocalDateTime.now());
        userRepo.save(user);

        mailService.sendPasswordResetMailAsync(user);
    }

    @Transactional
    public void completePasswordReset(String newPassword, String resetKey, LocalDateTime currentTime) {
        logger.debug("Reset user password for reset key {}", resetKey);

        User user = userRepo.findOneByResetKey(resetKey);
        if (user == null) {
            throw new DataInvalidException("exception.userService.reset-key-invalid");
        }

        // Not allow to complete reset password after sending resetKey 1 days
        LocalDateTime oneDayAgo = currentTime.minusDays(1);
        if (user.getCreatedAt().isAfter(oneDayAgo)) {
            throw new DataInvalidException("exception.userService.reset-key-expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetKey(null);
        user.setResetTime(null);
        userRepo.save(user);
    }

    @Transactional
    public void updateUserInfo(String name, String email, String languageTag) {
        String username = SecurityUtil.getCurrentLogin();

        userRepo.findOneByUsername(username)
            .filter(user -> user.isActivated())
            .ifPresent(user -> {
                user.setName(name);
                user.setEmail(email);
                user.setLanguageTag(languageTag);

                userRepo.save(user);
            });
    }

    @Transactional
    public void changePassword(ChangePasswordInfo changePasswordInfo) {
        String username = SecurityUtil.getCurrentLogin();
        String encodedNewPassword = passwordEncoder.encode(changePasswordInfo.getPassword());
        Optional<User> userOptional = userRepo.findOneByUsername(username);
        if (userOptional.isPresent()) {
            User currentUser = userOptional.get();
            if (encodedNewPassword.equals(currentUser.getPassword())
                    && (!changePasswordInfo.getCurrentPassword().equals(changePasswordInfo.getPassword()))) {
                userRepo.findOneByUsername(username)
                .filter(user -> user.isActivated())
                .ifPresent(user -> {
                    user.setPassword(passwordEncoder.encode(changePasswordInfo.getPassword()));
                    userRepo.save(user);
                });
            }
        }
    }

    public User getUserInfo() {
        String username = SecurityUtil.getCurrentLogin();

        return userRepo.findOneByUsername(username)
            .filter(user -> user.isActivated())
            .orElse(null);
    }

    private String generateRandomKey() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return DigestUtils.md5DigestAsHex(bytes);
    }
}
