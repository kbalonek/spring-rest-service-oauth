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

import com.balonek.connections.controller.transport.ClientErrorDto;
import com.balonek.connections.controller.transport.UserDto;
import com.balonek.connections.domain.exception.UserNotFoundException;
import com.balonek.connections.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserTransformer userTransformer;
	private UserService userService;

	@Autowired
	public UserController(UserTransformer userTransformer, UserService userService) {
		this.userTransformer = userTransformer;
		this.userService = userService;
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ClientErrorDto> handleUserNotFoundException(HttpServletRequest req, Exception e) {
		ClientErrorDto error = new ClientErrorDto(e.getMessage(), req.getRequestURI());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public UserDto getUsers(@PathVariable String userId) {
		return userTransformer.toDto(userService.findUserByUserId(userId));
	}
}
