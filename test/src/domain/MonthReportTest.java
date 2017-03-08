package src.domain;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Vladyslav Dovhopol on 3/3/17.
 */
public class MonthReportTest {

    private MonthReport report;

    @Before
    public void setUp() {
        report = new MonthReport(TestData.employee, YearMonth.now());
    }

    @Test
    public void addAction_WrongEmployee_NotAdded() {
        Action action = new Action(new Employee(2L, "Mary Jones"), TestData.office_1, Action.Type.GO_IN, LocalDateTime.now());
        assertFalse(report.add(action));
    }

    @Test
    public void addAction_ReportComposed_NotAdded() {
        report.compose();
        Action action = new Action(report.employee, TestData.office_1, Action.Type.GO_IN, LocalDateTime.now());
        assertFalse(report.add(action));
    }

    @Test
    public void addAction_WrontYearMonth_NotAdded() {
        Action action = new Action(report.employee, TestData.office_1, Action.Type.GO_IN, LocalDateTime.now().plusMonths(1));
        assertFalse(report.add(action));
    }

    @Test
    public void addAction_CorrectAction_Added() {
        Action action = new Action(report.employee, TestData.office_1, Action.Type.GO_IN, LocalDateTime.now());
        assertTrue(report.add(action));
    }

    @Test
    public void compose_TwoWeekReports_CorrectState() {
        final Employee employee = report.getEmployee();

        LocalDateTime week1Arrival = LocalDate.now().withDayOfMonth(1).atTime(9, 0);
        Action week1_goIn_office1 = new Action(employee, TestData.office_1, Action.Type.GO_IN, week1Arrival);
        Action week1_goOut_office1 = new Action(employee, TestData.office_1, Action.Type.GO_OUT, week1Arrival.plusHours(1));
        report.add(week1_goIn_office1); report.add(week1_goOut_office1);

        LocalDateTime week2Arrival = week1Arrival.plusWeeks(1);
        Action week2_goOut_office2 = new Action(employee, TestData.office_2, Action.Type.GO_OUT, week2Arrival);
        Action week2_goIn_office2 = new Action(employee, TestData.office_2, Action.Type.GO_IN, week2Arrival.plusMinutes(5));
        report.add(week2_goOut_office2); report.add(week2_goIn_office2);
        report.compose();
    }
}
