package src.writer;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import src.domain.MonthReport;
import src.domain.WeekReport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Month;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Vladyslav Dovhopol on 3/15/17.
 */
class MonthReportWriter {

    private static final Logger logger = Logger.getGlobal();

    public void write(File directory, MonthReport report) throws IOException {
        logger.info("Start writing " + WriterUtils.convertToFileName(report));
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        new TitleRowWriter(sheet.createRow(0)).write(report);
        write(sheet, report);

        autosizeColumns(sheet);
        writeWorkbookToFileSystem(workbook, directory, report);
    }

    private void write(Sheet sheet, MonthReport monthReport) {
        Month month = monthReport.getYearMonth().getMonth();
        WeekReportWriter weekReportWriter = new WeekReportWriter();
        int startRowNumber = 1;
        for (WeekReport weekReport : monthReport.getWeekReports()) {
            logger.info("Start writing week report. Week #" + weekReport.getNumberOfWeekInMonth());
            int lastRowNumber = weekReportWriter.write(sheet, startRowNumber, weekReport);
            startRowNumber = lastRowNumber + 1;
        }
        List<WeekReport> weekReports = monthReport.getWeekReports();
        weekReportWriter.write(sheet, 1, weekReports.get(0));
    }

    /**
     * Make width of all columns match content.
     * @param sheet sheet in which columns widths will be set
     */
    private void autosizeColumns(Sheet sheet) {
        logger.info("Autosize columns.");
        for (int i = 0; i < RowWriter.Cells.values().length; ++i) {
            sheet.autoSizeColumn(i, true);
        }
    }

    private void writeWorkbookToFileSystem(Workbook workbook, File directory, MonthReport report) throws IOException {
        String fileName = WriterUtils.convertToFileName(report);
        File file = new File(directory, fileName);
        if (file.exists()) {
            logger.info("Existing report will be deleted.");
            if (!file.delete()) {
                logger.warning("Couldn't delete existing report.");
            }
        }
        try (FileOutputStream outputStream = new FileOutputStream(file)){
            workbook.write(outputStream);
            workbook.close();
            logger.fine("Report " + WriterUtils.convertToFileName(report) + " has been written successfully to file system.");
        }
    }


    private class TitleRowWriter extends RowWriter {
        public TitleRowWriter(Row row) {
            super(row);
        }

        private void write(MonthReport report) {
            writeTitle(report);
            createEmptyCells();

            mergeCells();
            logger.fine("Title has been written.");
        }

        private void writeTitle(MonthReport report) {
            String employeeName = report.getEmployee().getName();
            Month month = report.getYearMonth().getMonth();
            String monthString = StringUtils.capitalize(month.toString().toLowerCase());
            int year = report.getYearMonth().getYear();
            Cell cell = write(Cells.PERIOD, employeeName + " " + monthString + " " + year);
            CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);

            Font bold_16 = Fonts.create_BOLD_16(row.getSheet().getWorkbook());
            CellUtil.setFont(cell, bold_16);
        }

        private void createEmptyCells() {
            for (Cells cellsEnum : Cells.values()) {
                if (!cellsEnum.equals(Cells.PERIOD)) {
                    row.createCell(cellsEnum.ordinal());
                }
            }
        }

        private void mergeCells() {
            Sheet sheet = row.getSheet();
            CellRangeAddress mergeRegion =
                    new CellRangeAddress(row.getRowNum(), row.getRowNum(), row.getFirstCellNum(), row.getLastCellNum()-1);
            sheet.addMergedRegion(mergeRegion);
        }
    }
}
