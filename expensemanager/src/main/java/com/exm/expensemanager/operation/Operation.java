package com.exm.expensemanager.operation;

import com.exm.expensemanager.common.enums.Currency;
import com.exm.expensemanager.common.enums.OperationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "operation")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Setter(AccessLevel.NONE)
    int id;

    @NotBlank
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "operation_date", nullable = false)
    private LocalDate operationDate;

    @NotNull
    @Column(name = "amount", nullable = false)
    private double amount;

    @NotNull
    @Column(name = "currency", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @NotBlank
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "included", nullable = false)
    private boolean included;

    @NotNull
    @Column(name = "operation_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    public Operation(String title, LocalDate operationDate, double amount, Currency currency, String type, boolean included, OperationType operationType) {
        this.title = title;
        this.operationDate = operationDate;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.included = included;
        this.operationType = operationType;
    }
}
