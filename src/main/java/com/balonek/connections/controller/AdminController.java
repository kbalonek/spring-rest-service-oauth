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

package com.balonek.connections.controller;

import com.balonek.connections.controller.transport.UserWithConnectionsDto;
import com.balonek.connections.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/admin")
public class AdminController {

	private final UserTransformer userTransformer;
	private UserService userService;

	@Autowired
	public AdminController(UserTransformer userTransformer, UserService userService) {
		this.userTransformer = userTransformer;
		this.userService = userService;
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public Collection<UserWithConnectionsDto> getUsers() {
		return userTransformer.toUserWithConnectionsDto(userService.getAllUsers());
	}
}
