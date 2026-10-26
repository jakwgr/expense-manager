package com.exm.expensemanager.operation;

import com.exm.expensemanager.common.Util;
import com.exm.expensemanager.nbp.CurrencyExchangeService;
import com.exm.expensemanager.nbp.dto.SummaryResultDTO;
import com.exm.expensemanager.operation.dto.OperationReportDTO;
import com.exm.expensemanager.operation.dto.OperationResponseDTO;
import com.exm.expensemanager.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OperationExcelService {
    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;
    private final OperationService operationService;
    private final Util util;
    private final CurrencyExchangeService currencyExchangeService;
    private final UserService userService;

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

            //formatting date cells
            CellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(
                    workbook.getCreationHelper().createDataFormat().getFormat("yyyy-mm-dd")
            );

            // formatting currency cells
            CellStyle currencyAmountStyle = workbook.createCellStyle();
            currencyAmountStyle.setDataFormat(
                    workbook.getCreationHelper().createDataFormat().getFormat("#,##0.00")
            );
            currencyAmountStyle.setAlignment(HorizontalAlignment.RIGHT);

            //title
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

            Row titleRow = sheet.createRow(0);
            XSSFCell dateTitle = (XSSFCell) titleRow.createCell(0);
            dateTitle.setCellValue(String.format("REPORT FROM %s - %s (READONLY)", start, end));
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

                XSSFCell amountCell = (XSSFCell) row.createCell(3);
                amountCell.setCellValue(operation.amount().doubleValue());
                amountCell.setCellStyle(currencyAmountStyle);

                XSSFCell currencyCell = (XSSFCell) row.createCell(4);
                currencyCell.setCellValue(operation.currency());
                currencyCell.setCellStyle(centerStyle);

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

            //table setup
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
            Map<String, BigDecimal> originalSums = operationService.sumOperationsForReport(operations);
            SummaryResultDTO summary = currencyExchangeService.calculateTotalSummary(originalSums, userService.getUserCurrencyInformation().code());

            //summary table style
            CellStyle boldStyle = workbook.createCellStyle();
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            boldStyle.setFont(boldFont);

            rowNum += 2;

            Row summaryHeaderRow = sheet.createRow(rowNum++);
            Cell sumTitleCell = summaryHeaderRow.createCell(1);
            sumTitleCell.setCellValue("SUMMARY:");
            sumTitleCell.setCellStyle(boldStyle);

            Row summaryTitlesRow = sheet.createRow(rowNum++);
            Cell amountBefore = summaryTitlesRow.createCell(1);
            amountBefore.setCellValue("AMOUNT BEFORE:");
            amountBefore.setCellStyle(boldStyle);
            Cell currencyBefore = summaryTitlesRow.createCell(2);
            currencyBefore.setCellValue("CURRENCY:");
            currencyBefore.setCellStyle(boldStyle);
            Cell amountAfter = summaryTitlesRow.createCell(3);
            amountAfter.setCellValue("AMOUNT AFTER:");
            amountAfter.setCellStyle(boldStyle);
            Cell currencyAfter = summaryTitlesRow.createCell(4);
            currencyAfter.setCellValue("CURRENCY:");
            currencyAfter.setCellStyle(boldStyle);
            Cell approxExchRate = summaryTitlesRow.createCell(5);
            approxExchRate.setCellValue("APPROX. EXCHANGE RATE:");
            approxExchRate.setCellStyle(boldStyle);

            for (Map.Entry<String, BigDecimal> entry : summary.originalAmounts().entrySet()) {
                String curr = entry.getKey();
                BigDecimal origAmount = entry.getValue();
                BigDecimal convAmount = summary.convertedAmounts().get(curr);
                BigDecimal rate = summary.rates().get(curr);

                Row sumRow = sheet.createRow(rowNum++);

                Cell origAmountCell = sumRow.createCell(1);
                origAmountCell.setCellValue(origAmount.doubleValue());
                origAmountCell.setCellStyle(currencyAmountStyle);
                sumRow.createCell(2).setCellValue(curr);

                if (!curr.equalsIgnoreCase(summary.baseCurrency())) {

                    Cell convCell = sumRow.createCell(3);
                    convCell.setCellValue(convAmount.doubleValue());
                    convCell.setCellStyle(currencyAmountStyle);

                    sumRow.createCell(4).setCellValue(summary.baseCurrency());
                    sumRow.createCell(5).setCellValue(rate.doubleValue());
                }
                else
                {
                    Cell convCell = sumRow.createCell(3);
                    convCell.setCellValue(origAmount.doubleValue());
                    convCell.setCellStyle(currencyAmountStyle);
                    sumRow.createCell(4).setCellValue(curr);
                    sumRow.createCell(5).setCellValue(1);

                }
            }

            Row totalRow = sheet.createRow(rowNum + 1);
            Cell totalLabelCell = totalRow.createCell(1);
            totalLabelCell.setCellValue("SUM (" + summary.baseCurrency() + "):");
            totalLabelCell.setCellStyle(boldStyle);

            Cell totalValCell = totalRow.createCell(3);
            totalValCell.setCellValue(summary.totalSum().doubleValue());

            CellStyle totalStyle = workbook.createCellStyle();
            totalStyle.cloneStyleFrom(currencyAmountStyle);
            totalStyle.setFont(boldFont);
            totalValCell.setCellStyle(totalStyle);

            //autosize
            for (int i = 0; i < numberOfColumns; i++) {
                sheet.autoSizeColumn(i);

                var currentWidth = sheet.getColumnWidth(i);

                sheet.setColumnWidth(i, currentWidth + 1800);
            }

            // header styles
            for(int i = 0; i < numberOfColumns; i++)
            {
                Row row = sheet.getRow(1);

                XSSFCell headerCell = (XSSFCell) row.getCell(i);
                headerCell.setCellStyle(centerStyle);
            }

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