// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.infras.security.xauth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TokenProvider {

    private final String secretKey;
    private final int tokenValidity;

    public TokenProvider(String secretKey, int tokenValidity) {
        this.secretKey = secretKey;
        this.tokenValidity = tokenValidity;
    }

    public Token createToken(UserDetails userDetails) {
        long expires = System.currentTimeMillis() + tokenValidity * 1000L;
        String token = userDetails.getUsername() + ":" + expires + ":" + computeSignature(userDetails, expires);
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
}
