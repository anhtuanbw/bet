// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.UserService;
import vn.kms.ngaythobet.domain.util.SecurityUtil;
import vn.kms.ngaythobet.infras.security.xauth.Token;
import vn.kms.ngaythobet.infras.security.xauth.TokenProvider;
import vn.kms.ngaythobet.web.dto.ChangePasswordInfo;
import vn.kms.ngaythobet.web.dto.RegisterUserInfo;
import vn.kms.ngaythobet.web.dto.UpdateUserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api")
public class AccountRest {
    private final UserService userService;

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    @Autowired
    public AccountRest(UserService userService, TokenProvider tokenProvider,
                       AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @RequestMapping(value = "/register", method = POST)
    public void registerAccount(@Valid @RequestBody RegisterUserInfo user) {
        userService.registerUser(user);
    }

    @RequestMapping(value = "/activate", method = GET)
    public void activateAccount(@RequestParam(value = "key") String key) {
        userService.activateRegistration(key, LocalDateTime.now());
    }

    @RequestMapping(value = "/login", method = POST)
    public Token login(@RequestParam String username, @RequestParam String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails details = userDetailsService.loadUserByUsername(username);

        return tokenProvider.createToken(details);
    }

    /**
     * check if the user is authenticated, and return its login.
     */
    @RequestMapping(value = "/authenticate", method = GET)
    public String isAuthenticated() {
        return SecurityUtil.getCurrentLogin();
    }

    @RequestMapping(value = "/reset-password/init", method = POST)
    public void requestPasswordReset(@RequestBody String email) {
        userService.requestPasswordReset(email);
    }

    @RequestMapping(value = "/reset-password/finish", method = POST)
    public void finishPasswordReset(@RequestParam(value = "key") String key,
                                    @Valid @RequestBody ChangePasswordInfo passwordInfo) {
        userService.completePasswordReset(passwordInfo.getPassword(), key, LocalDateTime.now());
    }

    @RequestMapping(value = "/account", method = GET)
    public User getAccount() {
        return userService.getUserInfo();
    }

    @RequestMapping(value = "/account", method = POST)
    public void updateAccountInfo(@Valid @RequestBody UpdateUserInfo user) {
        userService.updateUserInfo(user.getName(), user.getEmail(), user.getLanguageTag());
    }

    @RequestMapping(value = "/account/change-password", method = POST)
    public void changePassword(@Valid @RequestBody ChangePasswordInfo passwordInfo) {
        userService.changePassword(passwordInfo);
    }

    @RequestMapping(value = "/logout", method = POST)
    public void logout(HttpServletRequest request,HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, response, authentication);
    }
}
