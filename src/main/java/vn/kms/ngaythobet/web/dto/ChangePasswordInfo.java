// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.web.dto;

import org.hibernate.validator.constraints.NotEmpty;
import vn.kms.ngaythobet.domain.validation.FieldMatch;

import javax.validation.constraints.Pattern;

@FieldMatch(firstField = "password", secondField = "confirmPassword")
public class ChangePasswordInfo {
    @NotEmpty
    private String currentPassword;
    
    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{6,50}$"
    , message = "{validation.password.message}")
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

    public String getCurrentPassword() {
        return currentPassword;
}

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
    
}
