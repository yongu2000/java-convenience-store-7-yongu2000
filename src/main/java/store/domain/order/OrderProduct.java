package store.domain.order;

import java.util.Objects;

public class OrderProduct {
    private final String name;
    private final int quantity;

    private OrderProduct(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public static OrderProduct of(String name, int quantity) {
        return new OrderProduct(name, quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderProduct that = (OrderProduct) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
