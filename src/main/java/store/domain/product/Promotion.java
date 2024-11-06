package store.domain.product;

import java.time.LocalDate;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate startDate;
    private final LocalDate endDate;

    private Promotion(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Promotion of(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
        return new Promotion(name, buy, get, startDate, endDate);
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean checkAvailable(LocalDate orderDate) {
        return orderDate.isAfter(startDate) && orderDate.isBefore(endDate);
    }

    public int getPromotionQuantity(int orderProductQuantity) {
        return orderProductQuantity / (buy + get);
    }
}
