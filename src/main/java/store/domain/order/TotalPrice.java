package store.domain.order;

import java.util.Map;

public class TotalPrice {
    private final Map<String, Integer> totalPrice;

    public TotalPrice(Map<String, Integer> totalPrice) {
        this.totalPrice = totalPrice;
    }

}
