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

import com.balonek.connections.controller.RegistrationController;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Kris Balonek
 */
public class RegistrationControllerSecurityTest extends AbstractSecuredControllerTest {

	@InjectMocks
	RegistrationController controller;

    @Test
    public void should_allow_access_to_greeting_without_authentication() throws Exception {
        mvc.perform(post("/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"aaa\", \"password\":\"secret\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is("aaa")));
    }
}
