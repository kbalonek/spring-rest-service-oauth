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

import com.balonek.connections.controller.transport.ClientErrorDto;
import com.balonek.connections.controller.transport.RegistrationRequestDto;
import com.balonek.connections.controller.transport.UserDto;
import com.balonek.connections.domain.User;
import com.balonek.connections.domain.exception.UserAlreadyExistsException;
import com.balonek.connections.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class RegistrationController {

	private final UserService userService;
    private final UserTransformer userTransformer;

    @Autowired
	public RegistrationController(UserService userService, UserTransformer userTransformer){
		this.userService = userService;
        this.userTransformer = userTransformer;
    }

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ClientErrorDto> handleUserAlreadyExistsException(HttpServletRequest req, Exception e) {
		ClientErrorDto error = new ClientErrorDto(e.getMessage(), req.getRequestURI());
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST )
	@ResponseStatus(HttpStatus.CREATED)
	public UserDto register(@Valid @RequestBody RegistrationRequestDto registrationRequestDto) {
		// TODO encrypt the password
        User user = userService.createUser(
                registrationRequestDto.getUsername(),
                registrationRequestDto.getPassword());
        return userTransformer.toDto(user);
	}
}
