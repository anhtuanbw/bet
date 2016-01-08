package vn.kms.ngaythobet.infras.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import vn.kms.ngaythobet.domain.core.User;

/**
 * 
 * @author thangpham
 *
 */
public class CustomUserDetails extends org.springframework.security.core.userdetails.User {
    private static final long serialVersionUID = 20160105L;
    private User user;

    public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), authorities);
        this.user = user;
    }

    public CustomUserDetails(User user, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
            boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(user.getName(), user.getPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
                authorities);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
