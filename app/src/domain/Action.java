package src.domain;

import java.time.LocalDateTime;

/**
 * Created by Vladyslav Dovhopol on 3/2/17.
 */
public class Action {

    public enum Type {
        GO_IN,
        GO_OUT;
    }

    private final Employee employee;

    private final Office office;

    private final Type type;

    private final LocalDateTime time;

    public Action(Employee employee, Office office, Type type, LocalDateTime localDateTime) {
        this.employee = employee;
        this.office = office;
        this.type = type;
        this.time = localDateTime;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Office getOffice() {
        return office;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public Type getType() {
        return type;
    }

    public boolean isGoIn() {
        return type == Type.GO_IN;
    }

    public boolean isGoOut() {
        return type == Type.GO_OUT;
    }
}
