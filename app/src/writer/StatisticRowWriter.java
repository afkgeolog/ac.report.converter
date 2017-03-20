package src.writer;

import org.apache.poi.ss.usermodel.Row;
import src.domain.Statistic;

/**
 * Created by Vladyslav Dovhopol on 3/17/17.
 */
abstract class StatisticRowWriter extends RowWriter {

    protected StatisticRowWriter(Row row) {
        super(row);
    }

    protected void write(Statistic statistic) {
        writeArrivalTime(statistic);
        writeLeavingTime(statistic);
        writeTimeAtWork(statistic);
        writeTimeInTeleports(statistic);
        writeTeleports(statistic);
    }

    private void writeArrivalTime(Statistic statistic) {
        String formattedTime = WriterUtils.formatTime(statistic.getArrivalTime());
        write(Cells.ARRIVAL_TIME, formattedTime);
    }

    private void writeLeavingTime(Statistic statistic) {
        String formattedLeavingTime = WriterUtils.formatTime(statistic.getLeavingTime());
        write(Cells.LEAVING_TIME, formattedLeavingTime);
    }

    private void writeTimeAtWork(Statistic statistic) {
        String formattedTimeAtWork = WriterUtils.formatDuration(statistic.getTimeAtWork());
        write(Cells.TIME_AT_WORK, formattedTimeAtWork);
    }

    private void writeTimeInTeleports(Statistic statistic) {
        String formattedTimeInTeleports = WriterUtils.formatDuration(statistic.getTimeInTeleports());
        write(Cells.TIME_IN_TELEPORTS, formattedTimeInTeleports);
    }

    private void writeTeleports(Statistic statistic) {
        String formattedTeleports = String.valueOf(statistic.getTeleports());
        write(Cells.TELEPORTS, formattedTeleports);
    }
}
