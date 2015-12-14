// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.infras.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.kms.ngaythobet.domain.core.UserRepository;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;

@Component("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        String lowercaseUsername = username.toLowerCase();
        Optional<vn.kms.ngaythobet.domain.core.User> dbUser = userRepo.findOneByUsername(lowercaseUsername);

        return dbUser.map(user -> {
            if (!user.isActivated()) {
                throw new UserNotActivatedException("User " + username + " is not activated!");
            }

            List<GrantedAuthority> authorities = singletonList(new SimpleGrantedAuthority(user.getRole().getAuthority()));
            return new User(lowercaseUsername, user.getPassword(), authorities);
        }).orElseThrow(() -> new UsernameNotFoundException("Authentication failed!"));
    }
}
