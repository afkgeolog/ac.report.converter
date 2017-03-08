package src.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vladyslav Dovhopol on 3/2/17.
 */
public class WeekReport extends Report {
    /**
     * Start of the week. Always Monday.
     */
    private final LocalDate startOfWeek;
    private final LocalDate endOfWeek;
    private List<DayReport> dayReports = new ArrayList<DayReport>(7);

    WeekReport(Employee employee, LocalDate startOfWeek) {
        super(employee);
        if (startOfWeek.getDayOfWeek().getValue() != 1) {
            throw new IllegalArgumentException("Start of week must be Monday.");
        }
        this.startOfWeek = startOfWeek;
        endOfWeek = startOfWeek.plusDays(6);
    }

    protected boolean isWithinDateRange(Action action) {
        LocalDate actionDate = action.getTime().toLocalDate();
        return (actionDate.isAfter(startOfWeek) || actionDate.isEqual(startOfWeek)) &&
               (actionDate.isBefore(endOfWeek) || actionDate.isEqual(endOfWeek));
    }

    public void compose(){
        super.compose();
        if (actions.size() == 0) {
            return;
        }
        composeDayReports();
        composeStatistic();
    }

    private void composeDayReports() {
        DayReport dayReport = new DayReport(employee, actions.get(0).getTime().toLocalDate());
        for (Action action : actions) {
            if (!dayReport.add(action)) {
                dayReport.compose();
                dayReports.add(dayReport);
                dayReport = new DayReport(employee, action.getTime().toLocalDate());
                dayReport.add(action);
            }
        }
        dayReport.compose();
        dayReports.add(dayReport);
    }

    private void composeStatistic() {
        long sumOfArrivalTimeSeconds = 0;
        long sumOfLeavingTimeSeconds = 0;
        for (DayReport dayReport : dayReports) {
            sumOfArrivalTimeSeconds += dayReport.statistic.getArrivalTime().toSecondOfDay();
            sumOfLeavingTimeSeconds += dayReport.statistic.getLeavingTime().toSecondOfDay();
            statistic.numberOfTeleportsOnArrival += dayReport.statistic.getNumberOfTeleportsOnArrival();
            statistic.numberOfTeleportsOnLeaving += dayReport.statistic.getNumberOfTeleportsOnLeaving();
            statistic.teleports += dayReport.statistic.getTeleports();
            statistic.addDurationAtWork(dayReport.statistic.getTimeAtWork());
            statistic.addDurationInTeleports(dayReport.statistic.getTimeInTeleports());
        }
        statistic.arrivalTime = LocalTime.ofSecondOfDay(sumOfArrivalTimeSeconds / dayReports.size());
        statistic.leavingTime = LocalTime.ofSecondOfDay(sumOfLeavingTimeSeconds / dayReports.size());
    }

    public LocalDate getStartOfWeek() {
        return startOfWeek;
    }

    public LocalDate getEndOfWeek() {
        return endOfWeek;
    }

    public Statistic getStatistic() {
        return super.getStatistic();
    }
}
