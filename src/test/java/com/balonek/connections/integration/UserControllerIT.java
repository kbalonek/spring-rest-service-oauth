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

package com.balonek.connections.integration;

import com.balonek.connections.AbstractSecuredIT;
import com.balonek.connections.domain.User;
import com.balonek.connections.fixtures.UserFixtures;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Kris Balonek
 */
public class UserControllerIT extends AbstractSecuredIT {

	@Test
	public void should_find_user_by_user_id() throws Exception {
        User existingUser = UserFixtures.userWithUserRole();
        mvc.perform(get("/users/{userId}", existingUser.getUserId())
                .header("Authorization", getUserAuthorizationHeader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(existingUser.getUsername())))
                .andExpect(jsonPath("$.userId", is(existingUser.getUserId())));
    }

    @Test
    public void should_return_404_when_user_doesnt_exist() throws Exception {
        mvc.perform(get("/users/{userId}", "no_such_user")
                .header("Authorization", getUserAuthorizationHeader()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_connect_users() throws Exception {
        User existingUser = UserFixtures.userWithUserRole();
        User otherUser = UserFixtures.otherUserWithUserRole();
        String requestBody = String.format("{\"userId\": \"%s\"}", otherUser.getUserId());

        mvc.perform(post("/users/{userId}/connections", existingUser.getUserId())
                .header("Authorization", getUserAuthorizationHeader())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(existingUser.getUsername())))
                .andExpect(jsonPath("$.userId", is(existingUser.getUserId())))
                .andExpect(jsonPath("$.connections[0]", is(otherUser.getUserId())));
    }


    @Test
    public void should_return_404_when_connecting_not_existing_user() throws Exception {
        User existingUser = UserFixtures.userWithUserRole();
        String requestBody = "{\"userId\": \"no_such_user\"}";

        mvc.perform(post("/users/{userId}/connections", existingUser.getUserId())
                .header("Authorization", getUserAuthorizationHeader())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_not_allow_updating_other_users_connections() throws Exception {
        User existingUser = UserFixtures.otherUserWithUserRole();
        String requestBody = "{\"userId\": \"dummy\"}";

        mvc.perform(post("/users/{userId}/connections", existingUser.getUserId())
                .header("Authorization", getUserAuthorizationHeader())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isForbidden());
    }
}
