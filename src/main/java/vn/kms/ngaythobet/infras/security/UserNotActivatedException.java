// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.infras.security;

import org.springframework.security.core.AuthenticationException;

public class UserNotActivatedException extends AuthenticationException {

    public UserNotActivatedException(String message) {
        super(message);
    }
}
