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

import com.balonek.connections.controller.AdminController;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Kris Balonek
 */
public class AdminControllerTest extends AbstractSecuredControllerTest {

	@InjectMocks
	AdminController controller;

    @Test
    public void should_not_allow_access_to_users_when_not_authenticated() throws Exception {
        mvc.perform(get("/admin/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("unauthorized")));
    }

    @Test
    public void should_not_allow_access_to_users_when_using_basic_authentication() throws Exception {
        mvc.perform(get("/admin/users")
                .header("Authorization", getBasicAuthorizationHeader(CLIENT_ID, CLIENT_SECRET))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("unauthorized")));
    }

	@Test
	public void should_allow_access_to_users_when_user_has_roles_user_and_admin() throws Exception {
		mvc.perform(get("/admin/users")
				.header("Authorization", "Bearer " + getAccessToken("user_and_admin", "spring")))
				.andExpect(status().isOk());
	}

	@Test
	public void should_allow_access_to_users_when_user_has_role_admin() throws Exception {
		mvc.perform(get("/admin/users")
				.header("Authorization", "Bearer " + getAccessToken("admin", "spring")))
				.andExpect(status().isOk());
	}

	@Test
	public void should_not_allow_access_to_users_when_user_has_role_user() throws Exception {
		mvc.perform(get("/admin/users")
				.header("Authorization", "Bearer " + getAccessToken("user", "spring")))
				.andExpect(status().isForbidden());
	}

}