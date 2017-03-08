package src.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

/**
 * Created by Vladyslav Dovhopol on 3/3/17.
 */
public class TestData {

    static final Office office_1 = new Office("Test office 1");
    static final Office office_2 = new Office("Test office 2");
    static final Employee employee = new Employee(1L, "John Doe");
    static final LocalDate today = LocalDate.now();
    static final LocalDate startOfWeek = today.with(ChronoField.DAY_OF_WEEK, 1);
}
