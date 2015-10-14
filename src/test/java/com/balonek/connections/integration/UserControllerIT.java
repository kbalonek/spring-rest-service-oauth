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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}
