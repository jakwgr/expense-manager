package com.exm.expensemanager.operation;

import com.exm.expensemanager.operation.dto.OperationRequestDTO;
import com.exm.expensemanager.operation.dto.OperationResponseDTO;
import com.exm.expensemanager.user.dto.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/operation")
public class OperationController {
    private final OperationService operationService;

    @PostMapping
    public ResponseEntity<OperationResponseDTO> createOperation(@RequestBody @Valid OperationRequestDTO request)
    {
        OperationResponseDTO response = this.operationService.createOperation(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
