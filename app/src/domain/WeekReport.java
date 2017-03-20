package src.domain;

import java.time.*;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    private final Month month;
    /**
     * Since week may belong to two months, number of week may have values from 1 to 6.
     * For example value 6 is valid for May and October 2016.
     */
    private final int numberOfWeekInMonth;
    private List<DayReport> dayReports = new ArrayList<DayReport>(7);

    WeekReport(Employee employee, LocalDate startOfWeek, Month month) {
        super(employee);
        if (startOfWeek.getDayOfWeek().getValue() != 1) {
            throw new IllegalArgumentException("Start of week must be Monday.");
        }
        this.startOfWeek = startOfWeek;
        endOfWeek = startOfWeek.plusDays(6);

        if (!startOfWeek.getMonth().equals(month) && !endOfWeek.getMonth().equals(month)) {
            throw new IllegalArgumentException("Week report started on " + startOfWeek + " doesn't belong to month " + month);
        }
        this.month = month;
        this.numberOfWeekInMonth = calculateNumberOfWeek();
    }

    private int calculateNumberOfWeek() {
        final boolean isStartOfWeekOnMonth = startOfWeek.getMonth().equals(month);
        final boolean isEndOfWeekOnMonth = endOfWeek.getMonth().equals(month);
        if (!isStartOfWeekOnMonth && isEndOfWeekOnMonth) {
            return 1;
        }
        int dayOfWeekOnMonthStart = month.adjustInto(startOfWeek).with(ChronoField.DAY_OF_MONTH, 1L).get(ChronoField.DAY_OF_WEEK);
        return (dayOfWeekOnMonthStart + getStartOfWeek().getDayOfMonth() - 1) / 7 + 1;
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

    public Collection<DayReport> getDayReports() {
        return Collections.unmodifiableCollection(dayReports);
    }

    public int getNumberOfWeekInMonth() {
        return numberOfWeekInMonth;
    }
}
