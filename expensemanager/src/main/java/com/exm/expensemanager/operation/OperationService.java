package com.exm.expensemanager.operation;

import com.exm.expensemanager.common.Util;
import com.exm.expensemanager.common.enums.Currency;
import com.exm.expensemanager.common.enums.OperationType;
import com.exm.expensemanager.handler.exceptions.WrongOperationIncludeException;
import com.exm.expensemanager.handler.exceptions.operation.OperationNotCreatedException;
import com.exm.expensemanager.handler.exceptions.operation.OperationNotDeletedException;
import com.exm.expensemanager.handler.exceptions.operation.OperationNotExistsException;
import com.exm.expensemanager.handler.exceptions.WrongCurrencyException;
import com.exm.expensemanager.handler.exceptions.WrongOperationTypeException;
import com.exm.expensemanager.handler.exceptions.operation.OperationNotUpdatedException;
import com.exm.expensemanager.operation.dto.OperationFilterDTO;
import com.exm.expensemanager.operation.dto.create.OperationRequestDTO;
import com.exm.expensemanager.operation.dto.create.OperationRequestUserDTO;
import com.exm.expensemanager.operation.dto.OperationResponseDTO;
import com.exm.expensemanager.operation.dto.update.OperationIncludeUpdateRequestDTO;
import com.exm.expensemanager.operation.dto.update.OperationTypeUpdateRequestDTO;
import com.exm.expensemanager.operation.dto.update.OperationUpdateRequestDTO;
import com.exm.expensemanager.operation.specification.OperationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OperationService {
    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;
    private final Util util;

    public OperationResponseDTO createOperation(OperationRequestUserDTO request, String operationType)
    {
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
        if(!util.isBoolCorrect(request.included()))
        {
            throw new WrongOperationTypeException();
        }

        OperationRequestDTO requestUP = new OperationRequestDTO(
                request.title(), request.operationDate(), request.amount(), request.currency().toUpperCase(), request.type(), Boolean.parseBoolean(request.included()), operationType.toUpperCase());

        try{
            savedOperation = this.operationRepository.save(this.operationMapper.toEntity(requestUP));
        } catch (RuntimeException e) {
            throw new OperationNotCreatedException();
        }

        return this.operationMapper.toResponseDTO(savedOperation);
    }

    public OperationResponseDTO updateOperation(OperationUpdateRequestDTO request, int id)
    {
        Operation operation = this.operationRepository.findById(id)
                .orElseThrow(OperationNotExistsException::new);
        Operation updatedOperation;

        if(request.title() != null) operation.setTitle(request.title());
        if(request.operationDate() != null) operation.setOperationDate(request.operationDate());
        if(request.amount() != null) operation.setAmount(Double.parseDouble(String.valueOf(request.amount())));
        if(request.currency() != null)
        {
            if(!util.doesCurrencyContains(String.valueOf(request.currency())))
            {
                throw new WrongCurrencyException();
            }
            operation.setCurrency(Currency.valueOf(request.currency().toUpperCase()));
        }
        if(request.type() != null) operation.setType(request.type());

        try{
            updatedOperation = this.operationRepository.save(operation);
        } catch (RuntimeException e) {
            throw new OperationNotUpdatedException();
        }

        return this.operationMapper.toResponseDTO(updatedOperation);
    }

    public OperationResponseDTO updateOperationInclude(OperationIncludeUpdateRequestDTO request, int id)
    {
        Operation operation = this.operationRepository.findById(id)
                .orElseThrow(OperationNotExistsException::new);
        Operation updatedOperation;

        if(request.included() != null)
        {
            if(!util.isBoolCorrect(request.included()))
            {
                throw new WrongOperationIncludeException();
            }
            if(operation.isIncluded() == Boolean.parseBoolean(request.included()))
            {
                return this.operationMapper.toResponseDTO(operation);
            }
            operation.setIncluded(Boolean.parseBoolean(request.included()));
        }

        try{
            updatedOperation = this.operationRepository.save(operation);
        } catch (RuntimeException e) {
            throw new OperationNotUpdatedException();
        }

        return this.operationMapper.toResponseDTO(updatedOperation);
    }

    public OperationResponseDTO updateOperationType(OperationTypeUpdateRequestDTO request, int id)
    {
        Operation operation = this.operationRepository.findById(id)
                .orElseThrow(OperationNotExistsException::new);
        Operation updatedOperation;

        if (request.operationType() != null)
        {
            if (!request.operationType().equalsIgnoreCase(OperationType.EXPENSE.toString()) &&
                    !request.operationType().equalsIgnoreCase(OperationType.INCOME.toString()))
            {
                throw new WrongOperationTypeException();
            }
            if(Objects.equals(operation.getOperationType().toString(), request.operationType()))
            {
                return this.operationMapper.toResponseDTO(operation);
            }
            operation.setOperationType(OperationType.valueOf(request.operationType().toUpperCase()));
        }

        try{
            updatedOperation = this.operationRepository.save(operation);
        } catch (RuntimeException e) {
            throw new OperationNotUpdatedException();
        }

        return this.operationMapper.toResponseDTO(updatedOperation);
    }

    public OperationResponseDTO getOperationById(Integer id)
    {
        Operation operation = this.operationRepository.findById(id)
                .orElseThrow(OperationNotExistsException::new);

        return this.operationMapper.toResponseDTO(operation);
    }

    public List<OperationResponseDTO> getAllOperations()
    {
        return this.operationRepository.findAll()
                .stream()
                .map(operationMapper::toResponseDTO)
                .toList();
    }

    public List<Operation> getOperationsForReport(LocalDate start, LocalDate end)
    {
      return this.operationRepository.findAllOperationsBetweenDatesOrderByDateAsc(start, end);
    };

    public Page<OperationResponseDTO> getOperations(Pageable pageable, OperationFilterDTO filter)
    {
        Specification<Operation> specification = OperationSpecification.buildSpecification(filter);

        return this.operationRepository.findAll(specification, pageable)
                .map(this.operationMapper::toResponseDTO);
    }

    public void deleteOperation(Integer id)
    {
        Operation operation = this.operationRepository.findById(id)
                .orElseThrow(OperationNotExistsException::new);
        try{
            this.operationRepository.deleteById(id);
        } catch (RuntimeException e) {
            throw new OperationNotDeletedException(null);
        }

        return;
    }

    public void deleteAllOperations()
    {
        try{
            this.operationRepository.deleteAll();
        } catch (RuntimeException e) {
            throw new OperationNotDeletedException("ALL");
        }

        return;
    }
}
