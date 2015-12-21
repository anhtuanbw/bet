// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.infras.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import vn.kms.ngaythobet.domain.util.DataInvalidException;

@Component("authenticationProvider")
public class AuthenticationProviderImpl implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthenticationProviderImpl(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        String login = token.getName();
        UserDetails user = null;
        try {
            user = userDetailsService.loadUserByUsername(login);
        } catch (UsernameNotFoundException e) {
            throw new DataInvalidException("exception.user-or-password-incorrect");
        }

        String password = user.getPassword();
        String tokenPassword = (String) token.getCredentials();
        if (!passwordEncoder.matches(tokenPassword, password)) {
            throw new DataInvalidException("exception.user-or-password-incorrect");
        }

        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
