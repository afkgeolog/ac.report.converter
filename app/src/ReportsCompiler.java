package src;

import src.domain.Action;
import src.domain.Employee;
import src.domain.MonthReport;
import src.reader.InputReportDto;

import java.util.*;

/**
 * Created by Vladyslav Dovhopol on 3/14/17.
 */
public class ReportsCompiler {

    private ReportsCompiler() {}

    public static Map<Employee, List<MonthReport>> compile(InputReportDto inputReportDto) {
        inputReportDto.sortActionsByDateTimeAscending();
        Map<Employee, List<Action>> actionsMap = mapActions(inputReportDto.actions);

        Map<Employee, List<MonthReport>> composedMonthReports = new HashMap<>();
        actionsMap.forEach((employee, actions) -> {
            List<MonthReport> monthReports = compileReports(actions);
            composedMonthReports.put(employee, monthReports);
        });

        return composedMonthReports;
    }

    private static Map<Employee, List<Action>> mapActions(Collection<Action> actions) {
        Map<Employee, List<Action>> result = new HashMap<>();
        for (Action action : actions) {
            Employee employee = action.getEmployee();
            if (!result.containsKey(employee)) {
                result.put(employee, new ArrayList<>());
            }
            result.get(employee).add(action);
        }
        return result;
    }

    /**
     * @param actions actions sorted in ascending order performed by one employee
     * @return List of composed month reports.
     */
    private static List<MonthReport> compileReports(List<Action> actions) {
        List<MonthReport> monthReports= new ArrayList<>();

        MonthReport report = MonthReport.from(actions.get(0));
        for (Action action : actions) {
            if (!report.add(action)) {
                report.compose();
                monthReports.add(report);
                report = MonthReport.from(action);
                report.add(action);
            }
        }
        report.compose();
        monthReports.add(report);

        return monthReports;
    }
}
