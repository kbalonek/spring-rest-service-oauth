package com.balonek.connections.controller;

import com.balonek.connections.controller.transport.UserDto;
import com.balonek.connections.domain.User;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by kris on 10/14/15.
 */
@Component
public class UserTransformer {

    public UserDto toDto(User user) {
        return new UserDto(user.getUserId(), user.getUsername());
    }

    public Collection<UserDto> toDto(Collection<User> users) {
        return users.stream().map(this::toDto).collect(Collectors.toList());
    }
}
