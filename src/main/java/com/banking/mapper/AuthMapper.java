package com.banking.mapper;

//

import com.banking.dto.SignupRequest;
import com.banking.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    User SignupRequesttoUser(SignupRequest dto);
}
