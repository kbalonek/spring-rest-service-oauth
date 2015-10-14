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
import com.balonek.connections.controller.RegistrationController;
import com.balonek.connections.domain.User;
import com.balonek.connections.fixtures.UserFixtures;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Kris Balonek
 */
public class RegistrationControllerIT extends AbstractSecuredIT {

	@InjectMocks
	RegistrationController controller;

    @Test
    public void should_successfully_create_user() throws Exception {
        mvc.perform(post("/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"create_user_test\", \"password\":\"secret\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is("create_user_test")))
                .andExpect(jsonPath("$.userId", is("create_user_test")))
                .andExpect(content().string(not(containsString("secret"))));
    }

    @Test
    public void should_return_409_conflict_when_user_already_exists() throws Exception {
        User existingUser = UserFixtures.userWithUserRole();
        String requestBody = getRequestBodyForUsername(existingUser.getUsername());
        mvc.perform(post("/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isConflict());
    }

    private String getRequestBodyForUsername(String username) {
        return String.format("{\"username\":\"%s\", \"password\":\"secret\"}", username);
    }
}
