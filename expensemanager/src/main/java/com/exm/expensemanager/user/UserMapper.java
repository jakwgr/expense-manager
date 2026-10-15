package com.exm.expensemanager.user;

import com.exm.expensemanager.user.dto.UserRequestDTO;
import com.exm.expensemanager.user.dto.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserResponseDTO toDTO(User user);

    User toEntity(UserRequestDTO request);
}
