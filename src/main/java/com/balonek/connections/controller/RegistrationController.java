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

package com.balonek.connections.controller;

import com.balonek.connections.controller.transport.RegistrationRequestDto;
import com.balonek.connections.controller.transport.UserDto;
import com.balonek.connections.domain.User;
import com.balonek.connections.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistrationController {

	private UserService userService;

	@Autowired
	public RegistrationController(UserService userService){
		this.userService = userService;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST )
	@ResponseStatus(HttpStatus.CREATED)
	public UserDto register(@RequestBody RegistrationRequestDto registrationRequestDto) {
		// TODO encrypt the password
		return toDto(userService.createUser(registrationRequestDto.getUsername(), registrationRequestDto.getPassword()));
	}

	private UserDto toDto(User user) {
		return new UserDto(user.getUserId(), user.getUsername());
	}

}
