package com.exm.expensemanager.operation;

import com.exm.expensemanager.operation.dto.create.OperationRequestDTO;
import com.exm.expensemanager.operation.dto.OperationResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OperationMapper {

    OperationResponseDTO toResponseDTO(Operation operation);

    Operation toEntity(OperationRequestDTO request);

}
