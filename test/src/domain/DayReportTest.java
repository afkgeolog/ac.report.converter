package src.domain;

import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Created by Vladyslav Dovhopol on 3/3/17.
 */
public class DayReportTest {

    private DayReport report;

    @Before
    public void setUp() {
        report = new DayReport(TestData.employee, LocalDate.now());
    }

    @Test
    public void addAction_DayReportComposed_NotAdded() {
        report.compose();
        Action action = new Action(report.getEmployee(), TestData.office_1, Action.Type.GO_IN, LocalDateTime.now());
        assertFalse(report.add(action));
    }

    @Test
    public void addAction_FutureDate_NotAdded() {
        Action action = new Action(report.getEmployee(), TestData.office_1, Action.Type.GO_IN, LocalDateTime.now().plusDays(1));
        assertFalse(report.add(action));
    }

    @Test
    public void addAction_InvalidEmployee_NotAdded() {
        Action action = new Action(new Employee(2L, "Mary Jones"), TestData.office_1, Action.Type.GO_IN, LocalDateTime.now());
        assertFalse(report.add(action));
    }

    @Test
    public void addAction_ValidAction_Added() {
        Action action = new Action(report.getEmployee(), TestData.office_1, Action.Type.GO_IN, LocalDateTime.now());
        assertTrue(report.add(action));
        assertEquals(1, report.actions.size());
    }

    @Test
    public void compose_OnlyActionIsTeleport_Correct() {
        Action action = new Action(report.getEmployee(), TestData.office_1, Action.Type.GO_OUT, LocalDateTime.now());
        report.add(action);
        report.compose();

        Statistic statistic = report.getStatistic();
        assertEquals(1, statistic.getTeleports());
        assertEquals(Duration.ZERO, statistic.getTimeAtWork());
        assertEquals(Duration.ZERO, statistic.getTimeInTeleports());
        assertEquals(Duration.ZERO, statistic.getTotalTime());
    }

    @Test
    public void compose_ActionsForTeleport_Correct() {
        LocalDateTime now = LocalDateTime.now();
        Action goIn1 = new Action(report.getEmployee(), TestData.office_1, Action.Type.GO_IN, now);
        Action goIn2 = new Action(report.getEmployee(), TestData.office_1, Action.Type.GO_IN, now.plusHours(1));
        report.add(goIn1);
        report.add(goIn2);
        report.compose();

        Statistic statistic = report.getStatistic();
        assertEquals(2, statistic.getTeleports());
        assertEquals(Duration.ZERO, statistic.getTimeAtWork());
        assertEquals(Duration.ofHours(1), statistic.getTimeInTeleports());
        assertEquals(Duration.ofHours(1), statistic.getTotalTime());
    }

    @Test
    public void compose_ActionsForDoubleTeleport_Correct() {
        LocalDateTime now = LocalDateTime.now();
        Action goInOffice1 = new Action(report.getEmployee(), TestData.office_1, Action.Type.GO_IN, now);
        Action goOutOffice2 = new Action(report.getEmployee(), TestData.office_2, Action.Type.GO_OUT, now.plusHours(1));
        report.add(goOutOffice2);
        report.add(goInOffice1);
        report.compose();

        Statistic statistic = report.getStatistic();
        assertEquals(2, statistic.getTeleports());
        assertEquals(Duration.ZERO, statistic.getTimeAtWork());
        assertEquals(Duration.ofHours(1), statistic.getTimeInTeleports());
        assertEquals(Duration.ofHours(1), statistic.getTotalTime());
    }

    @Test
    public void compose_ActionsWithoutTeleports_Correct() {
        LocalDateTime now = LocalDateTime.now();
        Action goInOffice1 = new Action(report.getEmployee(), TestData.office_1, Action.Type.GO_IN, now);
        Action goOutOffice1 = new Action(report.getEmployee(), TestData.office_1, Action.Type.GO_OUT, now.plusHours(1));
        Action goInOffice2 = new Action(report.getEmployee(), TestData.office_2, Action.Type.GO_IN, now.plusHours(2));
        Action goOutOffice2 = new Action(report.getEmployee(), TestData.office_2, Action.Type.GO_OUT, now.plusHours(3));
        report.add(goInOffice1);report.add(goOutOffice1); report.add(goInOffice2); report.add(goOutOffice2);
        report.compose();

        Statistic statistic = report.getStatistic();
        assertEquals(now.toLocalTime(), statistic.getArrivalTime());
        assertEquals(now.plusHours(3).toLocalTime(), statistic.getLeavingTime());
        assertEquals(0, statistic.getTeleports());
        assertEquals(Duration.ofHours(2), statistic.getTimeAtWork());
        assertEquals(Duration.ZERO, statistic.getTimeInTeleports());
        assertEquals(Duration.ofHours(2), statistic.getTotalTime());
    }
}
