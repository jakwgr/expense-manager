package com.exm.expensemanager.operation;

import com.exm.expensemanager.common.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperationExcelService {
    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;
    private final Util util;
}
