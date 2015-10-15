package com.balonek.connections.controller;

import com.balonek.connections.controller.transport.UserDto;
import com.balonek.connections.controller.transport.UserWithConnectionsDto;
import com.balonek.connections.domain.User;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Kris Balonek
 */
@Component
public class UserTransformer {

    public UserDto toDto(User user) {
        return new UserDto(user.getUserId(), user.getUsername());
    }

    public Collection<UserDto> toDto(Collection<User> users) {
        return users.stream().map(this::toDto).collect(Collectors.toList());
    }

    public UserWithConnectionsDto toUserWithConnectionsDto(User user) {
        return new UserWithConnectionsDto(
                user.getUserId(),
                user.getUsername(),
                user.getConnectedUserIds());
    }

    public Collection<UserWithConnectionsDto> toUserWithConnectionsDto(Collection<User> users) {
        return users.stream().map(this::toUserWithConnectionsDto).collect(Collectors.toList());
    }
}
