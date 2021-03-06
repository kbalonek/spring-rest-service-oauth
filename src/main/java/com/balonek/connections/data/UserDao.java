/*
 * Copyright 2014-2015 the original author or authors.
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

package com.balonek.connections.data;

import com.balonek.connections.domain.User;

import java.util.Collection;
import java.util.Optional;

public interface UserDao {

	Optional<User> findByUsername(String login);
	Optional<User> findByUserId(String id);
	Collection<User> getAllUsers();
    /**
     * Returns all users with usernames containing the pattern. Returns all the users if pattern is an empty string.
     */
    Collection<User> searchUsers(String containing);
    User saveUser(User user);
}
