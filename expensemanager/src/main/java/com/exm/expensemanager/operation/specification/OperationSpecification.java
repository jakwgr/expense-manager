package com.exm.expensemanager.operation.specification;

import com.exm.expensemanager.common.SpecificationUtil;
import com.exm.expensemanager.operation.Operation;
import com.exm.expensemanager.operation.dto.OperationFilterDTO;
import org.springframework.data.jpa.domain.Specification;

public class OperationSpecification {

    public static Specification<Operation> buildSpecification(OperationFilterDTO request)
    {
        return Specification
                .<Operation>where(SpecificationUtil.equals("id", request.id()))
                .and(SpecificationUtil.containsStringIgnoreCase("title", request.title()))
                .and(SpecificationUtil.betweenTwo(
                        "operationDate", request.operationDateFrom(), request.operationDateTo()))
                .and(SpecificationUtil.betweenTwo(
                        "amount", request.amountMin(), request.amountMax()))
                .and(SpecificationUtil.containsStringIgnoreCase("currency", request.currency()))
                .and(SpecificationUtil.containsStringIgnoreCase("type", request.type()))
                .and(SpecificationUtil.equals("included", request.included()))
                .and(SpecificationUtil.equals("operationType", request.operationType()));
    }
}
