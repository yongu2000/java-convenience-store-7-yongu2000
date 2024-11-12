package store.domain.product;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;

public class Promotion {

    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final boolean applicable;

    private Promotion(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
        this.applicable = checkAvailable(DateTimes.now().toLocalDate());
    }

    public static Promotion of(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
        return new Promotion(name, buy, get, startDate, endDate);
    }

    public boolean isApplicable() {
        return applicable;
    }

    private boolean checkAvailable(LocalDate orderDate) {
        return orderDate.isAfter(startDate) && orderDate.isBefore(endDate);
    }

    public int getAvailablePromotionQuantity(int quantity) {
        if (quantity % (buy + get) >= buy) {
            return (buy + get) - quantity % (buy + get);
        }
        return 0;
    }

    public int getUnavailablePromotionQuantity(int orderProductQuantity) {
        return orderProductQuantity % (buy + get);
    }

    public int getAppliedPromotionQuantity(int quantity) {
        return quantity / (buy + get) * get;
    }

    @Override
    public String toString() {
        return name;
    }
}
