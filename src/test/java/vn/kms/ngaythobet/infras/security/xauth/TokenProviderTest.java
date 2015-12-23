package vn.kms.ngaythobet.infras.security.xauth;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import vn.kms.ngaythobet.BaseTest;
import vn.kms.ngaythobet.domain.core.User;

public class TokenProviderTest extends BaseTest {

    private TokenProvider tokenProvider;

    private Token token;

    private UserDetails user;

    @Override
    protected void doStartUp() {
        tokenProvider = new TokenProvider(Contants.SECRET_KEY,
                Contants.TOKEN_VALIDITY);
        User defaultUser = getDefaultUser();
        user = new org.springframework.security.core.userdetails.User(
                defaultUser.getUsername(), defaultUser.getPassword(),
                AuthorityUtils.NO_AUTHORITIES);
        token = tokenProvider.createToken(user);
    }

    @Test
    public void testValidateToken() {
        String tokenString = token.getToken();
        String[] part = tokenString.split(":");
        boolean isvalid = tokenProvider.validateToken(tokenString, user);
        assertThat(isvalid, is(true));

        StringBuilder tokenTestExpire = new StringBuilder();
        tokenTestExpire.append(part[0]).append(":");
        tokenTestExpire.append(System.currentTimeMillis()).append(":");
        tokenTestExpire.append(part[2]);
        isvalid = tokenProvider.validateToken(tokenTestExpire.toString(), user);
        assertThat(isvalid, is(false));

        StringBuilder tokenTestSignature = new StringBuilder();
        tokenTestSignature.append(part[0]).append(":");
        tokenTestSignature.append(part[1]).append(":");
        tokenTestSignature.append(System.currentTimeMillis());
        isvalid = tokenProvider.validateToken(tokenTestSignature.toString(),
                user);
        assertThat(isvalid, is(false));

        StringBuilder tokenTestExpireTimeFormat = new StringBuilder();
        tokenTestExpireTimeFormat.append(part[0]).append(":");
        tokenTestExpireTimeFormat.append("test format").append(":");
        tokenTestExpireTimeFormat.append(part[2]);
        isvalid = tokenProvider
                .validateToken(tokenTestExpireTimeFormat.toString(), user);
        assertThat(isvalid, is(false));

    }

}
