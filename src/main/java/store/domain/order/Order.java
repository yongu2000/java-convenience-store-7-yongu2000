package store.domain.order;

public class Order {

    private OrderProducts orderProducts;

    private Order(OrderProducts orderProducts) {
        this.orderProducts = orderProducts;
    }

    public static Order createOrder(OrderProducts orderProducts) {
        return new Order(orderProducts);
    }

}
