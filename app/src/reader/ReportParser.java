package src.reader;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import src.domain.Action;
import src.domain.Employee;
import src.domain.Office;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

/**
 * Created by Vladyslav Dovhopol on 3/8/17.
 */
public final class ReportParser {

    private InputReportDto reportDto;

    /**
     * Structure of row in input report.
     */
    private enum CellInfo {
        ACTION_ID,
        CARD_NUMBER,
        EMPLOYEE_ID,
        EMPLOYEE_NAME,
        GROUP,
        ACTION_TIME,
        /**
         * Office name and action type in pattern :  "{officeName}-{actionType}"
         */
        ADDRESS,
        IS_SUCCESSFUL,
        ACTION_DESCRIPTION
    }

    /**
     * Read report and parse data to {@link #reportDto}.
     */
    public synchronized InputReportDto parse(File xlsReport) throws Exception {
        reportDto = new InputReportDto();

        try(FileInputStream reportStream = new FileInputStream(xlsReport);
            Workbook workbook = new HSSFWorkbook(reportStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                Employee employee = readEmployee(row);
                Office office = readOffice(row);
                readAction(row, employee, office);
            }
        }

        return reportDto;
    }

    /**
     * Maps employee data from row to domain model and persists data in {@link #reportDto}
     */
    private Employee readEmployee(Row row) {
        String unparsedId = getCell(row, CellInfo.EMPLOYEE_ID).getStringCellValue().trim();
        String name = readEmployeeName(row);
        if (unparsedId.isEmpty() && name.isEmpty()) {
            //Employee without name and id are not processed
            return null;
        }
        Employee employee = new Employee(Long.valueOf(unparsedId), name);

        reportDto.employees.add(employee);
        return employee;
    }

    private String readEmployeeName(Row row) {
        String unparsedName = getCell(row, CellInfo.EMPLOYEE_NAME).getStringCellValue().trim();
        String name = "";
        StringTokenizer tokenizer = new StringTokenizer(unparsedName, "_ ");
        while (tokenizer.hasMoreTokens()) {
            String partOfName = tokenizer.nextToken();
            if (partOfName.matches("[A-Z]?[0-9]*")) {
                continue;
            } else {
                name = name.isEmpty() ? partOfName : name + " " + partOfName;
            }
        }
        return name;
    }

    /**
     * Maps office data from row to domain model and persists data in {@link #reportDto}
     */
    private Office readOffice(Row row) {
        String officeName = getCell(row, CellInfo.ADDRESS).getStringCellValue();
        officeName = officeName.substring(0, officeName.lastIndexOf("-"));
        Office office = new Office(officeName);

        reportDto.offices.add(office);
        return office;
    }

    /**
     * Maps action from row to domain model and persists data in {@link #reportDto}
     * @param employee employee parse on current row. May be NULL.
     * @param office office parse on current row
     * @param row current row
     */
    private void readAction(Row row, Employee employee, Office office) throws Exception {
        if (employee == null) {
            //Employee without name and ID
            return;
        }
        boolean isSuccessfulAction = getCell(row, CellInfo.IS_SUCCESSFUL).getNumericCellValue() == 1;
        if (!isSuccessfulAction) {
            return;
        }
        LocalDateTime actionTime = readActionTime(row);
        Action.Type type = readActionType(row);

        Action action = new Action(employee, office, type, actionTime);

        reportDto.actions.add(action);
    }

    private LocalDateTime readActionTime(Row row) {
        String timeString = getCell(row, CellInfo.ACTION_TIME).getStringCellValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss eeee");
        return  LocalDateTime.parse(timeString, formatter);
    }

    private Action.Type readActionType(Row row) throws Exception {
        String address = getCell(row, CellInfo.ADDRESS).getStringCellValue();
        String actionType = address.substring(address.lastIndexOf("-")+1);

        switch (actionType) {
            case "Вход": return Action.Type.GO_IN;
            case "Выход": return Action.Type.GO_OUT;
            default: throw new Exception("Unrecognized action type: " + actionType);
        }
    }

    private Cell getCell(Row row, CellInfo cellInfo) {
        return row.getCell(cellInfo.ordinal());
    }
}
