package src.reader;

import src.domain.Action;
import src.domain.Employee;
import src.domain.Office;

import java.util.*;

/**
 * Created by Vladyslav Dovhopol on 3/13/17.
 */
public class InputReportDto {

    public final List<Action> actions = new ArrayList<>();

    public final Set<Employee> employees = new HashSet<>();

    public final Set<Office> offices = new HashSet<>();

    public void sortActionsByDateTimeAscending() {
        actions.sort((action1, action2) -> action1.getTime().compareTo(action2.getTime()));
    }
}
