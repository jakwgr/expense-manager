package com.exm.expensemanager.operation;

import com.exm.expensemanager.operation.dto.OperationFilterDTO;
import com.exm.expensemanager.operation.dto.OperationReportDTO;
import com.exm.expensemanager.operation.dto.create.OperationRequestUserDTO;
import com.exm.expensemanager.operation.dto.OperationResponseDTO;
import com.exm.expensemanager.operation.dto.update.OperationIncludeUpdateRequestDTO;
import com.exm.expensemanager.operation.dto.update.OperationTypeUpdateRequestDTO;
import com.exm.expensemanager.operation.dto.update.OperationUpdateRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/operation")
public class OperationController {
    private final OperationService operationService;
    private final OperationExcelService operationExcelService;

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
    public ResponseEntity<OperationResponseDTO> getOperationById(@PathVariable Integer id)
    {
        OperationResponseDTO response = this.operationService.getOperationById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<OperationResponseDTO>> getAllOperations()
    {
        List<OperationResponseDTO> response = this.operationService.getAllOperations();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping()
    public ResponseEntity<Page<OperationResponseDTO>> getOperations(@ModelAttribute OperationFilterDTO request, Pageable page)
    {
        Page<OperationResponseDTO> response = this.operationService.getOperations(page, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOperation(@PathVariable int id)
    {
        this.operationService.deleteOperation(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }

    @DeleteMapping("/all")
    ResponseEntity<Void> deleteAllOperations()
    {
        this.operationService.deleteAllOperations();

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }

    @PostMapping("report/download")
    public ResponseEntity<byte[]> downloadExcel(@RequestBody @Valid OperationReportDTO report)
    {
        byte[] excelBytes = operationExcelService.generateExcel(report);

        String fileName = String.format("GENERATED_REPORT_FROM_%s_TO_%s.xlsx",
                report.start(),
                report.end());

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(fileName).build().toString())
                .body(excelBytes);
    }
}
