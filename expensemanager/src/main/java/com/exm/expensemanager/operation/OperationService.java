package com.exm.expensemanager.operation;

import com.exm.expensemanager.common.Util;
import com.exm.expensemanager.common.enums.Currency;
import com.exm.expensemanager.common.enums.OperationType;
import com.exm.expensemanager.handler.exceptions.OperationNotCreatedException;
import com.exm.expensemanager.handler.exceptions.WrongCurrencyException;
import com.exm.expensemanager.handler.exceptions.WrongOperationTypeException;
import com.exm.expensemanager.operation.dto.OperationRequestDTO;
import com.exm.expensemanager.operation.dto.OperationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OperationService {
    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;
    private final Util util;

    public OperationResponseDTO createOperation(OperationRequestDTO request)
    {
        var operationType = request.operationType();
        Operation savedOperation;

        if (!operationType.equalsIgnoreCase(OperationType.EXPENSE.toString()) &&
                !operationType.equalsIgnoreCase(OperationType.INCOME.toString()))
        {
            throw new WrongOperationTypeException();
        }
        if(!util.doesCurrencyContains(String.valueOf(request.currency())))
        {
            throw new WrongCurrencyException();
        }

        OperationRequestDTO requestUP = new OperationRequestDTO(
                request.title(), request.operationDate(), request.amount(), request.currency().toUpperCase(), request.type(), request.included(), request.operationType().toUpperCase());

        try{
            savedOperation = this.operationRepository.save(this.operationMapper.toEntity(requestUP));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        return this.operationMapper.toResponseDTO(savedOperation);
    }
}
