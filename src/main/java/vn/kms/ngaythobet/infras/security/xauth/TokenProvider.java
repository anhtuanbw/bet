// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.infras.security.xauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.UserService;
import vn.kms.ngaythobet.domain.util.Constants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

public class TokenProvider {

    private final String secretKey;
    private final int tokenValidity;
    private final Cache<String, Object> tokenCache;
    @Autowired
    private UserService userService;

    public TokenProvider(String secretKey, int tokenValidity) {
        this.secretKey = secretKey;
        this.tokenValidity = tokenValidity;
        tokenCache = CacheBuilder.newBuilder().expireAfterWrite(tokenValidity, TimeUnit.SECONDS).maximumSize(1000).build();
    }

    public Token createToken(UserDetails userDetails) {
        long expires = System.currentTimeMillis() + tokenValidity * 1000L;
        String token = userDetails.getUsername() + ":" + expires + ":" + computeSignature(userDetails, expires);
        tokenCache.put(token, userDetails.getUsername());
        return new Token(token, expires);
    }

    public String getUsernameFromToken(String authToken) {
        if (StringUtils.isEmpty(authToken)) {
            return null;
        }

        String[] parts = authToken.split(":");
        return parts[0];
    }

    public boolean validateToken(String authToken, UserDetails userDetails) {
        String[] parts = authToken.split(":");
        if (tokenCache.getIfPresent(authToken) == null) {
            return false;
        }
        if (parts.length != 3) {
            return false;
        }

        long expires;
        try {
            expires = Long.parseLong(parts[1]);
        } catch (NumberFormatException ex) {
            // invalid token format
            return false;
        }

        String signature = parts[2];
        String expectedSignature = computeSignature(userDetails, expires);

        return expires >= System.currentTimeMillis() && signature.equals(expectedSignature);
    }

    private String computeSignature(UserDetails userDetails, long expires) {
        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(userDetails.getUsername()).append(":");
        signatureBuilder.append(expires).append(":");
        signatureBuilder.append(userDetails.getPassword()).append(":");
        signatureBuilder.append(secretKey);

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }
        return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));
    }

    public void removeToken(String token) {
        tokenCache.invalidate(token);
    }

    public User getUsernameByHeader(SimpMessageHeaderAccessor headerAccessor) {
        MultiValueMap<String, String> nativeHeaders = headerAccessor
                .getMessageHeaders()
                .get(StompHeaderAccessor.NATIVE_HEADERS, MultiValueMap.class);
        String token = null;
        if (nativeHeaders.get(Constants.XAUTH_TOKEN_HEADER_NAME) != null
                && !nativeHeaders.get(Constants.XAUTH_TOKEN_HEADER_NAME)
                        .isEmpty()) {
            token = nativeHeaders.get(Constants.XAUTH_TOKEN_HEADER_NAME).get(0);
            String username = getUsernameFromToken(token);
            User user = userService.getUsernameByUsername(username);
            return user;
        }
        return null;
    }
}
