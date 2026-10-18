package com.exm.expensemanager.operation;

import org.apache.coyote.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Integer>, JpaSpecificationExecutor<Operation> {

    @Query("""
        SELECT o FROM Operation o WHERE o.operationDate >= :start
        AND o.operationDate <= :end ORDER BY o.operationDate ASC
""")
    List<Operation> findAllOperationsBetweenDatesOrderByDateAsc(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);
}
