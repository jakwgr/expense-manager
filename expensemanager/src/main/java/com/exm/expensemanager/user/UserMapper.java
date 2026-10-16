package com.exm.expensemanager.user;

import com.exm.expensemanager.user.dto.UserRequestDTO;
import com.exm.expensemanager.user.dto.UserResponseDTO;
import com.exm.expensemanager.user.dto.UserUpdateRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "lastName", source = "lastName")
    UserResponseDTO toResponseDTO(User user);

    @Mapping(target = "lastName", source = "lastName")
    User toEntity(UserRequestDTO request);

    @Mapping(target = "lastName", source = "lastName")
    User toEntity(UserUpdateRequestDTO request);
}
