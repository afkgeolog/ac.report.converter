package domain;

import java.time.YearMonth;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vladyslav Dovhopol on 3/2/17.
 * Container for week reports. Month report doesn't have it's own statistic.
 */
public class MonthReport extends Report{

    private YearMonth yearMonth;

    private List<WeekReport> weekReports = new ArrayList<WeekReport>(5);

    public MonthReport(Employee employee, YearMonth yearMonth) {
        super(employee);
        this.yearMonth = yearMonth;
    }

    protected boolean isWithinDateRange(Action action) {
        YearMonth actionYearMonth = YearMonth.from(action.getTime());
        return actionYearMonth.equals(yearMonth);
    }

    public void compose() {
        super.compose();
        if (actions.size() == 0) {
            return;
        }
        WeekReport weekReport =
                new WeekReport(employee, actions.get(0).getTime().toLocalDate().with(ChronoField.DAY_OF_WEEK, 1));
        for (Action action : actions) {
            if (!weekReport.add(action)) {
                weekReport.compose();
                weekReports.add(weekReport);
                weekReport = new WeekReport(employee, action.getTime().toLocalDate().with(ChronoField.DAY_OF_WEEK, 1));
            }
        }
        weekReport.compose();
        weekReports.add(weekReport);
    }

    public YearMonth getYearMonth() {
        return yearMonth;
    }
}
