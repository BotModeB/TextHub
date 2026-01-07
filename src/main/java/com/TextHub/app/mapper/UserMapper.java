package com.TextHub.app.mapper;

import com.TextHub.TextHub.Entity.User;
import com.TextHub.TextHub.Entity.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(User user);
}
