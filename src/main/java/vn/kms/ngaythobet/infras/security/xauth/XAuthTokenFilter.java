// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.infras.security.xauth;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.GenericFilterBean;

import vn.kms.ngaythobet.domain.util.Constants;

public class XAuthTokenFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;

    public XAuthTokenFilter(UserDetailsService detailsService, TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String authToken = httpServletRequest.getHeader(Constants.XAUTH_TOKEN_HEADER_NAME);
            tokenProvider.setAuthenticationFromToken(authToken);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
