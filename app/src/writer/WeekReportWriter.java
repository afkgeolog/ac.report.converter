package src.writer;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import src.domain.DayReport;
import src.domain.WeekReport;

import java.util.logging.Logger;

/**
 * Created by Vladyslav Dovhopol on 3/15/17.
 */
class WeekReportWriter {

    private static final Logger logger = Logger.getGlobal();

    /**
     * Write week report to excel sheet.
     * @param sheet sheet on which week report should be written
     * @param startRowNumber row on which writing should start
     * @param report week report
     * @return Last row number of week report on excel sheet.
     */
    public int write(Sheet sheet, int startRowNumber, WeekReport report) {
        Row headerRow = sheet.createRow(startRowNumber);
        new HeaderRowWriter(headerRow).write(report);

        final int dayReportsRowNumber = startRowNumber + 1;
        writeDayReports(sheet, dayReportsRowNumber, report);

        final int summaryRowNumber = dayReportsRowNumber + report.getDayReports().size() + 1;
        Row summaryRow = sheet.createRow(summaryRowNumber);
        new SummaryRowWriter(summaryRow).write(report);

        final int horizontalRuleRowNumber = summaryRowNumber + 1;
        Row horizontalRuleRow = sheet.createRow(horizontalRuleRowNumber);
        new HorizontalRuleRowWriter(horizontalRuleRow).write();
        logger.fine("Week report has been written successfully.");

        return horizontalRuleRowNumber;
    }

    private void writeDayReports(Sheet sheet, int rowNumber, WeekReport weekReport) {
        int dayNumber = 0;
        for (DayReport dayReport : weekReport.getDayReports()) {
            Row row = sheet.createRow(rowNumber + dayNumber++);
            new DayReportWriter(row).write(dayReport);
        }
    }

    private class HeaderRowWriter extends RowWriter {
        public HeaderRowWriter(Row row) {
            super(row);
        }

        public void write(WeekReport weekReport) {
            write(Cells.PERIOD, "Week " + weekReport.getNumberOfWeekInMonth());
            write(Cells.ARRIVAL_TIME, "Arrival time");
            write(Cells.LEAVING_TIME, "Leaving time");
            write(Cells.TIME_AT_WORK, "Time at work");
            write(Cells.TIME_IN_TELEPORTS, "Time in teleports");
            write(Cells.TELEPORTS_ON_ARRIVAL, "Teleports on arrival");
            write(Cells.TELEPORTS_ON_LEAVING, "Teleports on leaving");
            applyFont();
        }

    }

    private class SummaryRowWriter extends StatisticRowWriter {
        protected SummaryRowWriter(Row row) {
            super(row);
        }

        public void write(WeekReport report) {
            writeWeek();
            write(report.getStatistic());
        }

        private void writeWeek() {
            Cell cell = write(Cells.PERIOD, "Summary");
            Font bold_12 = Fonts.create_BOLD_12(row.getSheet().getWorkbook());
            CellUtil.setFont(cell, bold_12);
        }
    }

    private class HorizontalRuleRowWriter extends RowWriter {
        public HorizontalRuleRowWriter(Row row) {
            super(row);
        }

        public void write() {
            CellStyle cellStyle = row.getSheet().getWorkbook().createCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
            cellStyle.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
            cellStyle.setFillPattern(FillPatternType.BIG_SPOTS);
            for (Cells cellEnum : Cells.values()) {
                write(cellEnum, "", cellStyle);
            }
        }
    }
}
