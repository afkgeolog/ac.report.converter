package domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Vladyslav Dovhopol on 3/3/17.
 */
public abstract class Report {

    protected final Employee employee;
    protected boolean isComposed = false;
    protected Statistic statistic = new Statistic();
    protected List<Action> actions = new ArrayList<Action>();

    protected Report(Employee employee) {
        this.employee = employee;
    }

    /**
     * Make report immutable for further changes and calculate all {@link #statistic} regarding to added {@link #actions}.
     */
    protected void compose() {
        isComposed = true;
        sortActionsByTimeAscending();
    }

    public final boolean add(Action action) {
        if (!action.getEmployee().equals(employee) || !isWithinDateRange(action) || isComposed) {
            return false;
        }
        return actions.add(action);
    }

    protected abstract boolean isWithinDateRange(Action action);

    public Employee getEmployee() {
        return employee;
    }

    public boolean isComposed() {
        return isComposed;
    }

    protected Statistic getStatistic() {
        return statistic;
    }

    private void sortActionsByTimeAscending() {
        Comparator<Action> comparator = new Comparator<Action>() {
            public int compare(Action o1, Action o2) {
                return o1.getTime().compareTo(o2.getTime());
            }
        };
        actions.sort(comparator);
    }
}
