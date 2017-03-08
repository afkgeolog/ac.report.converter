package src.domain;

import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;

import static org.junit.Assert.*;

/**
 * Created by Vladyslav Dovhopol on 3/3/17.
 */
public class WeekReportTest {

    private WeekReport report;

    private final LocalDateTime startOfWeekMidnight = TestData.startOfWeek.atTime(0,0);

    @Before
    public void setUp() {
        report = new WeekReport(TestData.employee, TestData.startOfWeek);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createWeekReport_StartDateNotMonday_Exception() {
        new WeekReport(TestData.employee, LocalDate.of(2016, 1, 1));
    }

    @Test
    public void addAction_BeforeTheStartDate_NotAdded() {
        LocalDateTime dayBeforeStartOfWeek = report.getStartOfWeek().minusDays(1).atTime(0,0);
        Action action = new Action(report.getEmployee(), TestData.office_1, Action.Type.GO_IN, dayBeforeStartOfWeek);
        assertFalse(report.add(action));
    }

    @Test
    public void addAction_AfterTheEndDate_NotAdded() {
        LocalDateTime dayAfterStartOfWeek = report.getEndOfWeek().plusDays(1).atTime(0,0);
        Action action = new Action(report.getEmployee(), TestData.office_1, Action.Type.GO_IN, dayAfterStartOfWeek);
        assertFalse(report.add(action));
    }

    @Test
    public void addAction_WrongEmployee_NotAdded() {
        Employee employee = new Employee(2L, "Mary Jones");
        Action action = new Action(employee, TestData.office_1, Action.Type.GO_IN, startOfWeekMidnight);
        assertFalse(report.add(action));
    }

    @Test
    public void addAction_ReportComposed_NotAdded() {
        report.compose();
        Action action = new Action(report.getEmployee(), TestData.office_1, Action.Type.GO_IN, startOfWeekMidnight);
        assertFalse(report.add(action));
    }

    @Test
    public void addAction_OnStartDate_Added() {
        Action action = new Action(report.getEmployee(), TestData.office_1, Action.Type.GO_IN, startOfWeekMidnight);
        assertTrue(report.add(action));
    }

    @Test
    public void addAction_OnEndDate_Added() {
        LocalDateTime endOfWeekMidnight = report.getEndOfWeek().atTime(0,0);
        Action action = new Action(report.getEmployee(), TestData.office_1, Action.Type.GO_IN, endOfWeekMidnight);
        assertTrue(report.add(action));
    }

    @Test
    public void compose_containTwoDayReports_CorrectStatistics() {
        final Employee employee = report.getEmployee();

        LocalDateTime day1Arrival = LocalDate.now().with(ChronoField.DAY_OF_WEEK, 2).atTime(9, 0);
        Action day1_goIn_office1 = new Action(employee, TestData.office_1, Action.Type.GO_IN, day1Arrival);
        Action day1_goOut_office1 = new Action(employee, TestData.office_1, Action.Type.GO_OUT, day1Arrival.plusHours(1));
        report.add(day1_goIn_office1); report.add(day1_goOut_office1);

        LocalDateTime day2Arrival = day1Arrival.plusDays(1).plusHours(1);
        Action day2_goOut_office2 = new Action(employee, TestData.office_2, Action.Type.GO_OUT, day2Arrival);
        Action day2_goIn_office2 = new Action(employee, TestData.office_2, Action.Type.GO_IN, day2Arrival.plusMinutes(5));
        Action day2_goIn_office1 = new Action(employee, TestData.office_1, Action.Type.GO_IN, day2Arrival.plusHours(1));
        Action day2_goOut_office1 = new Action(employee, TestData.office_1, Action.Type.GO_OUT, day2Arrival.plusHours(2));
        report.add(day2_goOut_office2); report.add(day2_goIn_office2);
        report.add(day2_goIn_office1); report.add(day2_goOut_office1);
        report.compose();

        Statistic statistic = report.getStatistic();
        assertEquals(LocalTime.of(9, 30), statistic.getArrivalTime());
        assertEquals(LocalTime.of(11, 0), statistic.getLeavingTime());
        assertEquals(2, statistic.getTeleports());
        assertEquals(Duration.ofHours(2), statistic.getTimeAtWork());
        assertEquals(Duration.ofMinutes(55), statistic.getTimeInTeleports());
        assertEquals(1, statistic.getNumberOfTeleportsOnArrival());
        assertEquals(0, statistic.getNumberOfTeleportsOnLeaving());
    }
}
