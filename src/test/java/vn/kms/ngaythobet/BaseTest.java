// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet;

import static vn.kms.ngaythobet.domain.core.User.Role.USER;

import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import vn.kms.ngaythobet.domain.core.ChangeLogRepository;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("utest")
@SpringApplicationConfiguration(classes = { TestConfiguration.class })
public abstract class BaseTest {
    @Autowired
    protected UserRepository userRepo;

    @Autowired
    protected ChangeLogRepository changeLogRepo;

    private User defaultUser;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void startUp() {
        defaultUser = makeUser("tester");
        userRepo.save(defaultUser);
        mockLoginUser("admin");

        doStartUp();
    }

    @After
    public void tearDown() {
        userRepo.delete(defaultUser);
        changeLogRepo.deleteByEntityId(defaultUser.getId());
        doTearDown();
    }

    public User getDefaultUser() {
        return defaultUser;
    }

    protected void mockLoginUser(String username) {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(username, username));
    }

    protected void doStartUp() {
        // implemented by sub-classes
    }

    protected void doTearDown() {
        // implemented by sub-classes
    }

    protected User makeUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("Tester@123");
        user.setEmail(username + "@test.local");
        user.setName(username + " User");
        user.setLanguageTag("en");
        user.setActivated(true);
        user.setGroups(Collections.emptyList());
        user.setRole(USER);

        return user;
    }
}
