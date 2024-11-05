package store.domain.order;

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
}
