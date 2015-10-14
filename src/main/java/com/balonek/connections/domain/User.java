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

package com.balonek.connections.domain;

import com.balonek.connections.domain.security.Roles;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class User {

	final private String userId;
	final private String username;
	final private String password;
	final private Set<Roles> roles;

	public User(String userId, String username, String password, Set<Roles> roles) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

    public User(User user) {
        this(user.getUserId(), user.getUsername(), user.getPassword(), user.getRoles());
    }

    public String getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Set<Roles> getRoles() {
		return Collections.unmodifiableSet(roles);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(userId, user.userId) &&
				Objects.equals(username, user.username) &&
				Objects.equals(password, user.password) &&
				Objects.equals(roles, user.roles);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, username, password, roles);
	}
}
