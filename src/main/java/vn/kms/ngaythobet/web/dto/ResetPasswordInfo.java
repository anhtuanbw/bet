// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.web.dto;

import org.hibernate.validator.constraints.NotEmpty;
import vn.kms.ngaythobet.domain.validation.FieldMatch;

import javax.validation.constraints.Pattern;
import static vn.kms.ngaythobet.domain.util.Constants.*;

@FieldMatch(firstField = "password", secondField = "confirmPassword")
public class ResetPasswordInfo {
    @NotEmpty
    @Pattern(regexp = PASSWORD_REGEX, message = "{validation.password.message}")
    private String password;

    @NotEmpty
    private String confirmPassword;

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
    
}
