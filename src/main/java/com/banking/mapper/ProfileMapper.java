package com.banking.mapper;

import com.banking.schema.UserProfileDto;
import com.banking.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    UserProfileDto convertUserToUserProfileDto(User user);
}
