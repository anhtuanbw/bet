// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.infras.security.Http401UnauthorizedEntryPoint;
import vn.kms.ngaythobet.infras.security.xauth.TokenProvider;
import vn.kms.ngaythobet.infras.security.xauth.XAuthTokenConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Profile("!utest")
public class SecurityConfig extends WebSecurityConfigurerAdapter implements EnvironmentAware {

    @Autowired
    private Http401UnauthorizedEntryPoint authenticationEntryPoint;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    private RelaxedPropertyResolver propertyResolver;

    @Bean
    public TokenProvider tokenProvider() {
        String secret = propertyResolver.getProperty("secret-key", String.class);
        int validityInSeconds = propertyResolver.getProperty("token-validity", Integer.class);

        return new TokenProvider(secret, validityInSeconds);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "ngaythobet.xauth.");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers("/scripts/**/*.{js,html}")
            .antMatchers("/i18n/**")
            .antMatchers("/assets/**")
            .antMatchers("/swagger-ui.html");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
            .and()
            .csrf()
            .disable()
            .headers()
            .frameOptions()
            .disable()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/api/activate").permitAll()
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/login").permitAll()
            .antMatchers("/api/reset-password/*").permitAll()
            .antMatchers("/api/tournaments/create").hasRole(User.Role.ADMIN.toString())
            .antMatchers("/api/tournaments/active").hasRole(User.Role.ADMIN.toString())
            .antMatchers("/api/tournaments/findAll").hasRole(User.Role.ADMIN.toString())
            .antMatchers("/api/competitors/findByTournamentId").hasRole(User.Role.ADMIN.toString())
            .antMatchers("/api/**").authenticated()
            .and()
            .apply(securityConfigurerAdapter());

    }

    private XAuthTokenConfigurer securityConfigurerAdapter() {
        return new XAuthTokenConfigurer(userDetailsService, tokenProvider());
    }
}
