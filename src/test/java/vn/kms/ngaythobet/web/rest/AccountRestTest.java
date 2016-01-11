package vn.kms.ngaythobet.web.rest;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.kms.ngaythobet.BaseTest;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.UserRepository;
import vn.kms.ngaythobet.domain.core.UserService;
import vn.kms.ngaythobet.domain.util.Constants;
import vn.kms.ngaythobet.infras.security.CustomUserDetails;
import vn.kms.ngaythobet.infras.security.UserDetailsServiceImpl;
import vn.kms.ngaythobet.infras.security.xauth.Contants;
import vn.kms.ngaythobet.infras.security.xauth.Token;
import vn.kms.ngaythobet.infras.security.xauth.TokenProvider;
import vn.kms.ngaythobet.infras.security.xauth.XAuthTokenFilter;
import vn.kms.ngaythobet.web.dto.LoginInfo;

public class AccountRestTest extends BaseTest {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    private TokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    private UserDetailsServiceImpl userDetailsService;

    private MockMvc mockMvc;

    private Token token;

    private XAuthTokenFilter filter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        tokenProvider = new TokenProvider(Contants.SECRET_KEY, Contants.TOKEN_VALIDITY);
        filter = new XAuthTokenFilter(userDetailsService, tokenProvider);
        userDetailsService = new UserDetailsServiceImpl(userRepo);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AccountRest(userService, tokenProvider, authenticationManager, userDetailsService))
                .addFilters(filter).build();
    }

    @Test
    public void testAuthenticate() throws Exception {
        mockMvc.perform(get("/api/authenticate")).andExpect(status().isOk()).andExpect(content().string(""));

        int size = userRepo.findAll().size();
        ObjectMapper ow = new ObjectMapper();
        assertThat(size, equalTo(1));
        User registerUserInfo = createUser("email@yahoo.com", "user1", "123467", "user1");
        User user = userRepo.save(registerUserInfo);
        size = userRepo.findAll().size();
        String json = ow.writer().writeValueAsString(user);
        assertThat(size, equalTo(2));
        mockLoginUser(registerUserInfo);
        mockMvc.perform(get("/api/authenticate").header(Constants.XAUTH_TOKEN_HEADER_NAME, token.getToken()))
                .andExpect(status().isOk()).andExpect(content().string(json));
    }

    @Test
    public void testLogout() throws Exception {
        User user = getDefaultUser();
        mockLoginUser(user);
        mockMvc.perform(post("/api/logout").header(Constants.XAUTH_TOKEN_HEADER_NAME, token.getToken()))
                .andExpect(status().isOk());
    }

    @Test
    public void testLogin() throws Exception {
        User user = getDefaultUser();
        LoginInfo info = new LoginInfo();
        info.setPassword(user.getPassword());
        info.setUsername(user.getUsername());
        ObjectMapper ow = new ObjectMapper();
        String json = ow.writer().writeValueAsString(info);
        mockMvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    private User createUser(String email, String name, String password, String username) {
        User user = new User();
        user.setActivated(true);
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        user.setUsername(username);
        return user;
    }

    @Override
    protected void mockLoginUser(User user) {
        List<GrantedAuthority> authorities = singletonList(new SimpleGrantedAuthority(user.getRole().getAuthority()));
        CustomUserDetails customUserDetails = new CustomUserDetails(user, authorities);
        SecurityContextHolder.getContext()
                .setAuthentication(new TestingAuthenticationToken(customUserDetails, user.getUsername()));
        token = tokenProvider.createToken(customUserDetails);
    }

    @After
    public void clearData() {
        userRepo.deleteAll();
    }
}
