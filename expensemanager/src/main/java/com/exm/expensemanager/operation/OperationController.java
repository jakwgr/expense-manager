package com.exm.expensemanager.operation;

import com.exm.expensemanager.operation.dto.create.OperationRequestUserDTO;
import com.exm.expensemanager.operation.dto.OperationResponseDTO;
import com.exm.expensemanager.operation.dto.update.OperationIncludeUpdateRequestDTO;
import com.exm.expensemanager.operation.dto.update.OperationTypeUpdateRequestDTO;
import com.exm.expensemanager.operation.dto.update.OperationUpdateRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/operation")
public class OperationController {
    private final OperationService operationService;

    @PostMapping("/expense")
    public ResponseEntity<OperationResponseDTO> createExpense(@RequestBody @Valid OperationRequestUserDTO request)
    {
        OperationResponseDTO response = this.operationService.createOperation(request,"EXPENSE");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/income")
    public ResponseEntity<OperationResponseDTO> createIncome(@RequestBody @Valid OperationRequestUserDTO request)
    {
        OperationResponseDTO response = this.operationService.createOperation(request,"INCOME");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OperationResponseDTO> updateOperation(@RequestBody @Valid OperationUpdateRequestDTO request, @PathVariable int id)
    {
        OperationResponseDTO response = this.operationService.updateOperation(request, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/{id}/included")
    public ResponseEntity<OperationResponseDTO> updateOperationInclude(@RequestBody @Valid OperationIncludeUpdateRequestDTO request, @PathVariable int id)
    {
        OperationResponseDTO response = this.operationService.updateOperationInclude(request, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/{id}/operationtype")
    public ResponseEntity<OperationResponseDTO> updateOperationType(@RequestBody @Valid OperationTypeUpdateRequestDTO request, @PathVariable int id)
    {
        OperationResponseDTO response = this.operationService.updateOperationType(request, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperationResponseDTO> getOperationById(@PathVariable int id)
    {
        OperationResponseDTO response = this.operationService.getOperationById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}
