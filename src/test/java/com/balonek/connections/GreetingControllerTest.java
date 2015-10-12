/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.balonek.connections;

import com.balonek.connections.controller.GreetingController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Kris Balonek
 * @author Roy Clarkson
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class GreetingControllerTest {

    public static final String CLIENT_ID = "clientapp";
    public static final String CLIENT_SECRET = "123456";

    @Autowired
	WebApplicationContext context;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	@InjectMocks
	GreetingController controller;

	private MockMvc mvc;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.webAppContextSetup(context)
				.addFilter(springSecurityFilterChain)
				.build();
	}

	@Test
	public void should_not_allow_access_to_greeting_when_not_authenticated() throws Exception {
		mvc.perform(get("/greeting")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.error", is("unauthorized")));
	}


    @Test
    public void should_not_allow_access_to_greeting_when_using_basic_authentication() throws Exception {
        mvc.perform(get("/greeting")
                .header("Authorization", getBasicAuthorizationHeader(CLIENT_ID, CLIENT_SECRET))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("unauthorized")));
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

	private String getBasicAuthorizationHeader(String login, String password) {
		String credentials = login + ":" + password;
		return "Basic " + new String(Base64Utils.encode(credentials.getBytes()));
	}

	@Test
	public void should_greet_user_by_name() throws Exception {
		String accessToken = getAccessToken("user_and_admin", "spring");

		mvc.perform(get("/greeting")
				.header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content", is("Hello, User and Admin!")));
	}

	@Test
	public void should_allow_access_to_greeting_when_user_has_roles_user_and_admin() throws Exception {
		String accessToken = getAccessToken("user_and_admin", "spring");

		mvc.perform(get("/greeting")
				.header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isOk());
	}


	@Test
	public void should_allow_access_to_greeting_when_user_has_role_admin() throws Exception {
		String accessToken = getAccessToken("admin", "spring");

		mvc.perform(get("/greeting")
				.header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isOk());
	}


	@Test
	public void should_allow_access_to_greeting_when_user_has_role_user() throws Exception {
		String accessToken = getAccessToken("user", "spring");

		mvc.perform(get("/greeting")
				.header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isOk());
	}

    @Test
    public void should_not_allow_access_to_users_when_not_authenticated() throws Exception {
        mvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("unauthorized")));
    }

    @Test
    public void should_not_allow_access_to_users_when_using_basic_authentication() throws Exception {
        mvc.perform(get("/users")
                .header("Authorization", getBasicAuthorizationHeader(CLIENT_ID, CLIENT_SECRET))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("unauthorized")));
    }

	@Test
	public void should_allow_access_to_users_when_user_has_roles_user_and_admin() throws Exception {
		mvc.perform(get("/users")
				.header("Authorization", "Bearer " + getAccessToken("user_and_admin", "spring")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)));
	}

	@Test
	public void should_allow_access_to_users_when_user_has_role_admin() throws Exception {
		mvc.perform(get("/users")
				.header("Authorization", "Bearer " + getAccessToken("admin", "spring")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)));
	}

	@Test
	public void should_not_allow_access_to_users_when_user_has_role_user() throws Exception {
		mvc.perform(get("/users")
				.header("Authorization", "Bearer " + getAccessToken("user", "spring")))
				.andExpect(status().is(403));
	}

}
