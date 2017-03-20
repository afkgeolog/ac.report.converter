package src.writer;

import org.apache.poi.ss.usermodel.Row;
import src.domain.DayReport;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Created by Vladyslav Dovhopol on 3/15/17.
 */
class DayReportWriter extends StatisticRowWriter {

    public DayReportWriter(Row row) {
        super(row);
    }

    public void write(DayReport report) {
        writeDay(report);
        write(report.getStatistic());
    }

    private void writeDay(DayReport report) {
        LocalDate date = report.getDate();
        String dayName = date.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH);
        write(Cells.PERIOD, dayName);
    }
}
