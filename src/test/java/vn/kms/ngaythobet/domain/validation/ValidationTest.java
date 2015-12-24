// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.validation;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import vn.kms.ngaythobet.BaseTest;
import vn.kms.ngaythobet.domain.core.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.Pattern;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ValidationTest extends BaseTest {
    @Autowired
    private Validator validator;

    @Test
    public void testEntityExistValidation() {
        UserData data = new UserData("test", "test@test.local", "Test@123", "Test@123", -1);
        Set<ConstraintViolation<UserData>> violations = validator.validate(data);
        assertThat(violations.size(), equalTo(1));

        ConstraintViolation<UserData> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString(), equalTo("manager"));
        assertThat(violation.getMessage(), equalTo("is not existed"));

        data.manager = getDefaultUser().getId();
        violations = validator.validate(data);
        assertThat(violations.size(), equalTo(0));
    }

    @Test
    public void testFieldMatchValidation() {
        UserData data = new UserData("test", "test@test.local", "Test@123", "Test@12", getDefaultUser().getId());
        Set<ConstraintViolation<UserData>> violations = validator.validate(data);
        assertThat(violations.size(), equalTo(1));

        ConstraintViolation<UserData> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString(), equalTo("confirmPassword"));
        assertThat(violation.getMessage(), equalTo("is not matched"));

        data.confirmPassword = "Test@123";
        violations = validator.validate(data);
        assertThat(violations.size(), equalTo(0));
    }

    @Test
    public void testFieldUniqueValidation() {
        User defaultUser = getDefaultUser();

        UserData data = new UserData(defaultUser.getUsername(), "test@test.local", "Test@123",
                                    "Test@123", defaultUser.getId());
        Set<ConstraintViolation<UserData>> violations = validator.validate(data);
        assertThat(violations.size(), equalTo(1));

        ConstraintViolation<UserData> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString(), equalTo("username"));
        assertThat(violation.getMessage(), equalTo("'tester' is already existed"));

        data.username = defaultUser.getUsername() + "123";
        violations = validator.validate(data);
        assertThat(violations.size(), equalTo(0));
    }

    @Test
    public void testMutipleFieldsValidation() {
        UserData data = new UserData(getDefaultUser().getUsername(), "abc.com", "Test@ 123", "456", -1);
        Set<ConstraintViolation<UserData>> violations = validator.validate(data);
        assertThat(violations.size(), equalTo(5));
        violations.forEach(violation -> {
            switch (violation.getPropertyPath().toString()) {
                case "username":
                    assertThat(violation.getMessage(), equalTo("'tester' is already existed"));
                    break;
                case "email":
                    assertThat(violation.getMessage(), equalTo("email not valid"));
                    break;
                case "password":
                    assertThat(violation.getMessage(), equalTo("Password must contain at least six of uppercase, "
                            + "lowercase letters, numbers and special characters"));
                    break;
                case "confirmPassword":
                    assertThat(violation.getMessage(), equalTo("is not matched"));
                    break;
                case "manager":
                    assertThat(violation.getMessage(), equalTo("is not existed"));
                    break;
            }
        });
    }

    @FieldMatch(firstField = "password", secondField = "confirmPassword")
    static class UserData {
        @FieldUnique(field = "username", entity = User.class)
        String username;

        @Email
        String email;

        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[#$%&'()*+,-./:!<=>?@\\^_`\\[\\]{|}~;])\\S{6,50}$",
                message = "{validation.password.message}")
        String password;

        @NotEmpty
        String confirmPassword;

        @EntityExist(type = User.class)
        long manager;

        UserData(String username, String email, String password, String confirmPassword, long manager) {
            this.username = username;
            this.email = email;
            this.password = password;
            this.confirmPassword = confirmPassword;
            this.manager = manager;
        }
    }
}
