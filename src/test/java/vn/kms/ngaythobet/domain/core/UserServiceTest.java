// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.core;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static vn.kms.ngaythobet.domain.core.User.Role.USER;

import java.time.LocalDateTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.security.crypto.password.PasswordEncoder;

import vn.kms.ngaythobet.BaseTest;
import vn.kms.ngaythobet.domain.util.DataInvalidException;
import vn.kms.ngaythobet.web.dto.ChangePasswordInfo;
import vn.kms.ngaythobet.web.dto.RegisterUserInfo;

public class UserServiceTest extends BaseTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @Override
    protected void doStartUp() {
        MailService mailService = mock(MailService.class);
        when(mailService.sendEmailAsync(anyString(), anyString(), anyString(), anyBoolean(), anyBoolean()))
                .thenReturn(new AsyncResult<>(true));

        userService = new UserService(userRepo, passwordEncoder, mailService);
    }

    @Test
    public void testRegisterUser() {
        String username = "test123";
        RegisterUserInfo registerUserInfo = new RegisterUserInfo();
        registerUserInfo.setUsername(username);
        registerUserInfo.setEmail("test123@test.local");
        registerUserInfo.setLanguageTag("en");
        registerUserInfo.setName("Test User");
        registerUserInfo.setPassword("Test@123");

        userService.registerUser(registerUserInfo);

        // verify activationKey was generated
        User user = userRepo.findOneByUsername(username).get();
        String activationKey = user.getActivationKey();

        assertThat(activationKey, notNullValue());
        assertThat(user.isActivated(), is(false));

        LocalDateTime now = LocalDateTime.now();

        // do activation with wrong key, it must be failed
        exception.expectMessage("{exception.userService.activation-key-invalid}");
        userService.activateRegistration(activationKey + "123", now);

        // do activation after 7 days, it must be failed
        now = LocalDateTime.now().plusDays(7);
        exception.expect(DataInvalidException.class);
        exception.expectMessage("{exception.userService.activation-key-expired}");
        userService.activateRegistration(activationKey, now);

        // do activation with correct key and time, it must be passed (no
        // exception)
        now = LocalDateTime.now();
        userService.activateRegistration(activationKey, now);
        user = userRepo.findOneByUsername(username).get();
        assertThat(user.isActivated(), is(true));
        assertThat(user.getActivationKey(), nullValue());

        // do activation again, it must be failed
        now = LocalDateTime.now();
        exception.expect(DataInvalidException.class);
        exception.expectMessage("{exception.userService.activation-key-invalid}");
        userService.activateRegistration(activationKey, now);
    }

    @Test
    public void testResetPassword() {
        String username = "test456";
        String email = "test456@test.local";
        RegisterUserInfo registerUserInfo = new RegisterUserInfo();
        registerUserInfo.setUsername(username);
        registerUserInfo.setEmail(email);
        registerUserInfo.setLanguageTag("en");
        registerUserInfo.setName("Test User");
        registerUserInfo.setPassword("Test@456");
        userService.registerUser(registerUserInfo);

        // request reset password with wrong email
        exception.expectMessage("{exception.userService.email-invalid}");
        userService.requestPasswordReset("test456789@test.local");

        // request reset password but user is not activated
        exception.expectMessage("{exception.userService.user-not-activated}");
        userService.requestPasswordReset(email);

        User user = userRepo.findOneByUsername(username).get();
        String activationKey = user.getActivationKey();
        userService.activateRegistration(activationKey, LocalDateTime.now());

        // request reset password successfully (no exception)
        userService.requestPasswordReset(email);
        user = userRepo.findOneByUsername(username).get();
        String resetKey = user.getResetKey();
        assertThat(resetKey, notNullValue());
        assertThat(user.getResetTime(), greaterThan(user.getCreatedAt()));

        LocalDateTime now = LocalDateTime.now();

        // complete reset password with wrong key, it must be failed
        exception.expectMessage("{exception.userService.reset-key-invalid}");
        userService.completePasswordReset("Test@456789", resetKey, now);

        // complete reset password after 1 day, it must be failed
        now = LocalDateTime.now().plusDays(1);
        exception.expectMessage("{exception.userService.reset-key-expired}");
        userService.completePasswordReset("Test@456789", resetKey, now);

        // complete reset password with correct key and time, it must be passed
        // (no exception)
        now = LocalDateTime.now();
        userService.completePasswordReset("Test@456789", resetKey, now);
        user = userRepo.findOneByUsername(username).get();
        assertThat(user.getResetKey(), notNullValue());
        assertThat(user.getResetTime(), notNullValue());

        // complete reset password again, it must be failed
        now = LocalDateTime.now();
        exception.expectMessage("{exception.userService.reset-key-invalid}");
        userService.completePasswordReset("Test@456789", resetKey, now);
    }
    
    @Test
    public void testResetPasswordWithSuccess() {
        User defaultUser = getDefaultUser();
        String username = defaultUser.getUsername();
        mockLoginUser(username);
        
        assertThat(defaultUser.getEmail(), notNullValue());
        assertThat(defaultUser.getEmail().isEmpty(),is(false));
        
        userService.requestPasswordReset(defaultUser.getEmail());
        User user = userRepo.findOne(defaultUser.getId());
        
        assertThat(user, notNullValue());
        
        String resetKey = user.getResetKey();
        
        LocalDateTime now = LocalDateTime.now();
        
        // complete reset password with correct key and time, it must be passed (no exception)
        userService.completePasswordReset("Abc@123", resetKey, now);
        
        assertThat(user.getResetTime(), greaterThan(user.getCreatedAt()));
        assertThat(user.getResetKey(), notNullValue());
        assertThat(user.getResetTime(), notNullValue());
        // the reset key invalid & not expired
        LocalDateTime oneDayAgo = now.minusDays(1);
        assertThat(user.getResetTime().isAfter(oneDayAgo), is(true));
        
    }

    @Test
    public void testUpdateUserInfo() {
        User defaultUser = getDefaultUser();
        String username = defaultUser.getUsername();
        mockLoginUser(username);

        userService.updateUserInfo("TesterX User", "testerx@test.local", "vi_vn");

        User user = userRepo.findOne(defaultUser.getId());
        assertThat(user.getName(), equalTo("TesterX User"));
        assertThat(user.getEmail(), equalTo("testerx@test.local"));
        assertThat(user.getLanguageTag(), equalTo("vi_vn"));
    }

    @Test
    public void testChangePassword() {
        String username = "hieu";
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("Tester@123"));
        user.setEmail(username + "@test.local");
        user.setName(username + " User");
        user.setLanguageTag("en");
        user.setActivated(true);
        user.setRole(USER);
        userRepo.save(user);
        mockLoginUser(username);
        ChangePasswordInfo changePasswordInfo = new ChangePasswordInfo();
        changePasswordInfo.setCurrentPassword("Tester@123");
        changePasswordInfo.setPassword("Abc@015");
        changePasswordInfo.setConfirmPassword("Abc@015");
        userService.changePassword(changePasswordInfo);
        User userWithNewPassword = userRepo.findOneByUsername(username).get();
        assertThat(passwordEncoder.matches("Abc@015", userWithNewPassword.getPassword()), is(true));
    }

    @Test
    public void testGetUserInfo() {
        User defaultUser = getDefaultUser();
        String username = defaultUser.getUsername();
        mockLoginUser(username);

        User user = userService.getUserInfo();
        assertThat(user.getName(), equalTo("tester User"));
        assertThat(user.getEmail(), equalTo("tester@test.local"));
        assertThat(user.getLanguageTag(), equalTo("en"));
    }
}
