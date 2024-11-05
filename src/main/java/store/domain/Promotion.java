package store.domain;

import java.time.LocalDate;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate start_date;
    private final LocalDate end_date;

    private Promotion(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        start_date = startDate;
        end_date = endDate;
    }

    public static Promotion of(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
        return new Promotion(name, buy, get, startDate, endDate);
    }

    @Override
    public String toString() {
        return name;
    }
}
