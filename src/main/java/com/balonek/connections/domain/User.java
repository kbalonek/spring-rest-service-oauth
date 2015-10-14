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

import static com.google.common.base.Preconditions.checkNotNull;

public class User {

	final private String userId;
	final private String username;
	final private String password;
	final private Set<Roles> roles;
	final private Set<String> connectedUserIds;

    public User(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.roles = user.getRoles();
        this.connectedUserIds = user.getConnectedUserIds();
    }

    private User(Builder builder) {
        this.userId = builder.userId;
        this.username = builder.username;
        this.password = builder.password;
        this.roles = builder.roles;
        this.connectedUserIds = builder.connectedUserIds;
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

    public Set<String> getConnectedUserIds() {
        return Collections.unmodifiableSet(connectedUserIds);
    }

    public static class Builder {

        private String userId;
        private String username;
        private String password;
        private Set<Roles> roles;
        private Set<String> connectedUserIds;

        public User build() {
            checkNotNull(userId);
            checkNotNull(username);
            checkNotNull(password);
            checkNotNull(roles);
            checkNotNull(connectedUserIds);
            return new User(this);
        }
        public Builder withUserId(String userId){
            this.userId = userId;
            return this;
        }

        public Builder withUsername(String username){
            this.username = username;
            return this;
        }

        public Builder withPassword(String password){
            this.password = password;
            return this;
        }

        public Builder withRoles(Set<Roles> roles){
            this.roles = roles;
            return this;
        }

        public Builder withConnectedUserIds(Set<String> connectedUserIds){
            this.connectedUserIds = connectedUserIds;
            return this;
        }
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(userId, user.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId);
	}
}
