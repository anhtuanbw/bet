// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.web.dto;

import org.hibernate.validator.constraints.Email;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.validation.FieldUnique;

import javax.validation.constraints.Size;

public class UpdateUserInfo {
    @Size(max = 50)
    private String name;

    @Email
    @Size(min = 5, max = 100)
    @FieldUnique(field = "email", entity = User.class)
    private String email;

    @Size(min = 2, max = 5)
    private String languageTag;

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
