package com.exm.expensemanager.operation;

import com.exm.expensemanager.common.enums.OperationType;
import com.exm.expensemanager.operation.dto.OperationRequestDTO;
import com.exm.expensemanager.operation.dto.OperationResponseDTO;
import com.exm.expensemanager.user.User;
import com.exm.expensemanager.user.dto.UserRequestDTO;
import com.exm.expensemanager.user.dto.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OperationMapper {

    OperationResponseDTO toResponseDTO(Operation operation);

    Operation toEntity(OperationRequestDTO request);

}
