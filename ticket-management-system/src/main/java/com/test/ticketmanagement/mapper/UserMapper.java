package com.test.ticketmanagement.mapper;

import com.test.ticketmanagement.api.model.UserRequest;
import com.test.ticketmanagement.api.model.UserResponse;
import com.test.ticketmanagement.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tickets", ignore = true)
    User toEntity(UserRequest userRequest);

    UserResponse toResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tickets", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUserFromRequest(UserRequest userRequest, @MappingTarget User user);
}