package src.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Vladyslav Dovhopol on 3/3/17.
 */
public class DayReport extends Report {

    private final LocalDate date;

    DayReport(Employee employee, LocalDate date) {
        super(employee);
        this.date = date;
    }

    protected boolean isWithinDateRange(Action action) {
        return action.getTime().toLocalDate().equals(date);
    }

    public void compose() {
        super.compose();
        if (actions.size() == 0) {
            return;
        }
        statistic.arrivalTime = actions.get(0).getTime().toLocalTime();
        statistic.leavingTime = actions.get(actions.size()-1).getTime().toLocalTime();

        if (actions.get(0).isGoOut()) {
            statistic.teleports++;
            statistic.numberOfTeleportsOnArrival++;
        }
        if (actions.get(actions.size()-1).isGoIn()) {
            statistic.teleports++;
            statistic.numberOfTeleportsOnLeaving++;
        }

        Action previousAction;
        for (int i = 1; i < actions.size(); ++i) {
            previousAction = actions.get(i-1);
            Action action = actions.get(i);

            LocalTime previousActionTime = previousAction.getTime().toLocalTime();
            LocalTime actionTime = action.getTime().toLocalTime();
            Duration durationBetweenActions = Duration.between(previousActionTime, actionTime);

            if (action.getType() == previousAction.getType()) {
                statistic.teleports++;
                statistic.addDurationInTeleports(durationBetweenActions);
            } else
            if (previousAction.isGoIn() && action.isGoOut() && !action.getOffice().equals(previousAction.getOffice())) {
                statistic.teleports += 2;
                statistic.addDurationInTeleports(durationBetweenActions);
            } else
            if (previousAction.isGoIn() && action.isGoOut()){
                statistic.addDurationAtWork(durationBetweenActions);
            }
        }
    }

    public Statistic getStatistic() {
        return super.getStatistic();
    }

    public LocalDate getDate() {
        return date;
    }
}
