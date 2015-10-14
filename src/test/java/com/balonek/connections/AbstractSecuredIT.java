package com.balonek.connections;

import com.balonek.connections.data.UserDao;
import com.balonek.connections.fixtures.UserFixtures;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Kris Balonek
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public abstract class AbstractSecuredIT {

    public static final String CLIENT_ID = "clientapp";
    public static final String CLIENT_SECRET = "123456";

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    private UserDao userDao;

    protected MockMvc mvc;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(springSecurityFilterChain)
                .build();
        createTestUsers();
    }

    private void createTestUsers() {
        userDao.saveUser(UserFixtures.userWithUserRole());
        userDao.saveUser(UserFixtures.userWithAdminRole());
    }

    protected String getAdminAuthorizationHeader() throws Exception {
        return "Bearer " + getAccessToken(UserFixtures.ADMIN_USERNAME, UserFixtures.TEST_PASSWORD);
    }

    protected String getUserAuthorizationHeader() throws Exception {
        return "Bearer " + getAccessToken(UserFixtures.USER_USERNAME, UserFixtures.TEST_PASSWORD);
    }

    private String getAccessToken(String username, String password) throws Exception {
        String authorization = getBasicAuthorizationHeader(CLIENT_ID, CLIENT_SECRET);
        String contentType = MediaType.APPLICATION_JSON + ";charset=UTF-8";

        String content = mvc
                .perform(
                        post("/oauth/token")
                                .header("Authorization", authorization)
                                .contentType(
                                        MediaType.APPLICATION_FORM_URLENCODED)
                                .param("username", username)
                                .param("password", password)
                                .param("grant_type", "password")
                                .param("scope", "read write")
                                .param("client_id", CLIENT_ID)
                                .param("client_secret", CLIENT_SECRET))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.access_token", is(notNullValue())))
                .andExpect(jsonPath("$.token_type", is(equalTo("bearer"))))
                .andExpect(jsonPath("$.refresh_token", is(notNullValue())))
                .andExpect(jsonPath("$.expires_in", is(greaterThan(4000))))
                .andExpect(jsonPath("$.scope", is(equalTo("read write"))))
                .andReturn().getResponse().getContentAsString();

        return content.substring(17, 53);
    }

    protected String getBasicAuthorizationHeader(String login, String password) {
        String credentials = login + ":" + password;
        return "Basic " + new String(Base64Utils.encode(credentials.getBytes()));
    }
}
