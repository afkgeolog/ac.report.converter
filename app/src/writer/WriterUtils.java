package src.writer;

import src.domain.Employee;
import src.domain.MonthReport;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Vladyslav Dovhopol on 3/15/17.
 */
class WriterUtils {

    private WriterUtils(){}

    /**
     * @param employee employee
     * @return Name applicable to be used in file/folder names.
     */
    public static String formatName(Employee employee) {
        return employee.getName().replaceAll("[^A-Za-z0-9А-Яа-я_]", "_");
    }

    public static String convertToFileName(MonthReport report) {
        StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder .append(formatName(report.getEmployee())).append("_")
                        .append(report.getYearMonth().getMonth()).append("_")
                        .append(report.getYearMonth().getYear()).append(".xls");
        return fileNameBuilder.toString();
    }

    public static String formatTime(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }

    public static String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() - hours * 60;
        StringBuilder builder = new StringBuilder();
        if (hours > 0) {
            builder.append(hours).append(" hours ");
        }
        if (minutes > 0) {
            builder.append(minutes).append(" minutes");
        }

        return builder.toString();
    }
}
