package src.domain;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Created by Vladyslav Dovhopol on 3/3/17.
 */
public class Statistic {
    int teleports;
    Duration timeAtWork = Duration.ZERO;
    /**
     * This field represent how much time person spent in teleportation.
     * When teleport occurs there is no way to determine how much time does person spent at work from it.
     */
    Duration timeInTeleports = Duration.ZERO;
    LocalTime arrivalTime;
    LocalTime leavingTime;

    int numberOfTeleportsOnArrival;

    int numberOfTeleportsOnLeaving;

    void addDurationInTeleports(Duration duration) {
        timeInTeleports = timeInTeleports.plus(duration);
    }

    void addDurationAtWork(Duration duration) {
        timeAtWork = timeAtWork.plus(duration);
    }

    public int getTeleports() {
        return teleports;
    }

    public Duration getTimeAtWork() {
        return timeAtWork;
    }

    public Duration getTimeInTeleports() {
        return timeInTeleports;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public LocalTime getLeavingTime() {
        return leavingTime;
    }

    public int getNumberOfTeleportsOnArrival() {
        return numberOfTeleportsOnArrival;
    }

    public int getNumberOfTeleportsOnLeaving() {
        return numberOfTeleportsOnLeaving;
    }
}
