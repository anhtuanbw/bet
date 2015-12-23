// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.web.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.validation.FieldMatch;
import vn.kms.ngaythobet.domain.validation.FieldUnique;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import static vn.kms.ngaythobet.domain.util.Constants.*;

@FieldMatch(firstField = "password", secondField = "confirmPassword")
public class RegisterUserInfo {
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    @NotEmpty
    @Size(min = 6, max = 50)
    @FieldUnique(field = "username", entity = User.class)
    private String username;

    @NotEmpty
    @Pattern(regexp = PASSWORD_REGEX, message = "{validation.password.message}")
    private String password;

    @NotEmpty
    private String confirmPassword;

    @Size(max = 50)
    @NotEmpty
    private String name;

    @NotEmpty
    @Email
    @Size(min = 6, max = 100)
    @FieldUnique(field = "email", entity = User.class)
    private String email;

    @Size(min = 2, max = 5)
    private String languageTag;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguageTag() {
        return languageTag;
    }

    public void setLanguageTag(String languageTag) {
        this.languageTag = languageTag;
    }
}
