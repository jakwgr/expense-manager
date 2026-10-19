package com.exm.expensemanager.operation;

import com.exm.expensemanager.common.Util;
import com.exm.expensemanager.operation.dto.OperationReportDTO;
import com.exm.expensemanager.operation.dto.OperationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationExcelService {
    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;
    private final OperationService operationService;
    private final Util util;

    public byte[] generateExcel(OperationReportDTO report) {
        try {
            var start = report.start();
            var end = report.end();

            List<OperationResponseDTO> operations = this.operationService.getOperationsForReport(start, end)
                    .stream()
                    .map(operationMapper::toResponseDTO)
                    .toList();

            System.out.println(operationService.sumAfterExchange(operationService.sumOperationsForReport(operations)));

            System.out.println(operationService.sumOperationsForReport(operations));

            Workbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = (XSSFSheet) workbook.createSheet();

            // center cells
            CellStyle centerStyle = workbook.createCellStyle();
            centerStyle.setAlignment(HorizontalAlignment.CENTER);

            //title cell format
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            Font font = workbook.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 16);
            titleStyle.setFont(font);
            titleStyle.setFont(font);

            //formatting date cells
            CellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(
                    workbook.getCreationHelper().createDataFormat().getFormat("yyyy-mm-dd")
            );

            //title
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

            Row titleRow = sheet.createRow(0);
            XSSFCell dateTitle = (XSSFCell) titleRow.createCell(0);
            dateTitle.setCellValue(String.format("REPORT FROM %s - %s (READONLY)", start, end));
            dateTitle.setCellStyle(dateCellStyle);
            dateTitle.setCellStyle(titleStyle);

            //Header
            Row headerRow = sheet.createRow(1);
            headerRow.createCell(0).setCellValue("OPERATION ID");
            headerRow.createCell(1).setCellValue("TITLE");
            headerRow.createCell(2).setCellValue("DATE OF OPERATION");
            headerRow.createCell(3).setCellValue("AMOUNT");
            headerRow.createCell(4).setCellValue("CURRENCY");
            headerRow.createCell(5).setCellValue("TYPE OF OPERATION");
            headerRow.createCell(6).setCellValue("IS OPERATION INCLUDED");
            headerRow.createCell(7).setCellValue("EXPENSE/INCOME");




            //Data
            int rowNum = 2;
            for (OperationResponseDTO operation : operations) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(operation.id());
                row.createCell(1).setCellValue(operation.title());

                XSSFCell dateCell = (XSSFCell) row.createCell(2);
                dateCell.setCellValue(operation.operationDate());
                dateCell.setCellStyle(dateCellStyle);

                row.createCell(3).setCellValue(operation.amount().doubleValue());
                row.createCell(4).setCellValue(operation.currency());
                row.getCell(4).setCellStyle(dateCellStyle);
                row.createCell(5).setCellValue(operation.type());
                row.createCell(6).setCellValue(operation.included().toString().toUpperCase());
                row.createCell(7).setCellValue(operation.operationType().toUpperCase());
            }

            int numberOfColumns = headerRow.getLastCellNum();

            //setting table size
            AreaReference areaReference = workbook.getCreationHelper().createAreaReference(
                    new CellReference(1,0),
                    new CellReference(rowNum-1, numberOfColumns-1)
            );

            //autosize
            for (int i = 0; i < numberOfColumns; i++) {
                sheet.autoSizeColumn(i);

                var currentWidth = sheet.getColumnWidth(i);

                sheet.setColumnWidth(i, currentWidth + 1800);
            }

            // header styles
            for(int i = 0; i<numberOfColumns; i++)
            {
                Row row = sheet.getRow(1);

                XSSFCell dateCell = (XSSFCell) row.getCell(i);
                dateCell.setCellStyle(centerStyle);
            }


            XSSFTable table = sheet.createTable(areaReference);
            table.setName("ReportTable");
            table.setDisplayName("ReportTable");

            //table style
            var styleInfo = table.getCTTable().addNewTableStyleInfo();
            styleInfo.setName("TableStyleMedium9");
            styleInfo.setShowRowStripes(true);
            styleInfo.setShowColumnStripes(false);

            //table filters
            table.getCTTable().addNewAutoFilter().setRef(areaReference.formatAsString());

            //creating xlms
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
