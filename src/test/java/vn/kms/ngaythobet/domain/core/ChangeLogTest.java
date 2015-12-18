// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.nullValue;
import static vn.kms.ngaythobet.domain.core.ChangeLog.Action.DELETE;
import static vn.kms.ngaythobet.domain.core.ChangeLog.Action.INSERT;
import static vn.kms.ngaythobet.domain.core.ChangeLog.Action.UPDATE;
import static vn.kms.ngaythobet.domain.core.User.Role.ADMIN;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.data.domain.Sort;

import vn.kms.ngaythobet.BaseTest;
import vn.kms.ngaythobet.domain.core.ChangeLog.Action;
import vn.kms.ngaythobet.domain.core.ChangeLog.Change;

public class ChangeLogTest extends BaseTest {
    @Override
    public void startUp() {
        mockLoginUser("admin");
    }

    @Override
    public void tearDown() {
        userRepo.deleteAll();
        changeLogRepo.deleteAll();
    }

    @Test
    public void testAuditFieldsCorrect() throws InterruptedException {
        User user = makeUser("tester");
        user = userRepo.save(user);
        Thread.sleep(1);
        assertThat(user.getCreatedAt(), lessThan(LocalDateTime.now()));
        assertThat(user.getCreatedBy(), equalTo("admin"));
        assertThat(user.getModifiedAt(), nullValue());
        assertThat(user.getModifiedBy(), nullValue());

        user.setName("tester2");
        user = userRepo.save(user);
        Thread.sleep(1);
        assertThat(user.getCreatedAt(), lessThan(user.getModifiedAt()));
        assertThat(user.getCreatedBy(), equalTo("admin"));
        assertThat(user.getModifiedAt(), lessThan(LocalDateTime.now()));
        assertThat(user.getModifiedBy(), equalTo("admin"));
    }

    @Test
    public void testCUDChangeLogs() throws InterruptedException {
        // scenario: create, update twice and then delete user
        User user = makeUser("tester");

        mockLoginUser("user1");
        userRepo.save(user);
        Thread.sleep(1);

        mockLoginUser("user2");
        user.setName("Tester2 User");
        user.setRole(ADMIN);
        user.setActivationKey("abcdef");
        userRepo.save(user);
        Thread.sleep(1);

        mockLoginUser("user3");
        user.setActivationKey("1234567890");
        user.setPassword("tester2@123");
        userRepo.save(user);
        Thread.sleep(1);

        mockLoginUser("user4");
        userRepo.delete(user);
        Thread.sleep(1);

        // verify the change logs
        List<ChangeLog> logs = changeLogRepo.findAll(new Sort("timestamp"));
        Map<String, Change> expectedChanges;
        assertThat(logs.size(), equalTo(4));

        ChangeLog createLog = logs.get(0);
        expectedChanges = null;
        assertLogIsCorrect(createLog, "user1", INSERT, user.getClass(), user.getId(), expectedChanges);

        ChangeLog update1Log = logs.get(1);
        expectedChanges = new HashMap<>();
        expectedChanges.put("name", new Change("tester User", "Tester2 User"));
        expectedChanges.put("role", new Change("USER", "ADMIN"));
        expectedChanges.put("activationKey", new Change(null, "abcdef"));
        assertLogIsCorrect(update1Log, "user2", UPDATE, user.getClass(), user.getId(), expectedChanges);

        ChangeLog update2Log = logs.get(2);
        expectedChanges = new HashMap<>();
        expectedChanges.put("activationKey", new Change("abcdef", "1234567890"));
        expectedChanges.put("password", new Change("Tester@123", "tester2@123"));
        assertLogIsCorrect(update2Log, "user3", UPDATE, user.getClass(), user.getId(), expectedChanges);

        ChangeLog deleteLog = logs.get(3);
        expectedChanges = null;
        assertLogIsCorrect(deleteLog, "user4", DELETE, user.getClass(), user.getId(), expectedChanges);
    }

    private void assertLogIsCorrect(ChangeLog actualLog, String expectedUsername, Action expectedAction,
                                    Class<?> expectedType, long expectedEntityId, Map<String, Change> expectedChanges) {
        assertThat(actualLog.getUsername(), equalTo(expectedUsername));
        assertThat(actualLog.getAction(), equalTo(expectedAction));
        assertThat(actualLog.getEntityType(), equalTo(expectedType.getName()));
        assertThat(actualLog.getEntityId(), equalTo(expectedEntityId));

        if (expectedChanges == null) {
            assertThat(actualLog.getEntityChanges(), nullValue());
        } else {
            Map<String, Change> actualChanges = actualLog.getEntityChanges();
            assertThat(actualChanges.size(), equalTo(expectedChanges.size()));
            expectedChanges.entrySet().forEach(expectedEntry ->
                assertThat(actualChanges.get(expectedEntry.getKey()), equalTo(expectedEntry.getValue())));
        }
    }
}
