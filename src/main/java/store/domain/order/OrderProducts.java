package store.domain.order;

import java.util.List;

public class OrderProducts {
    private final List<OrderProduct> orderProducts;

    public OrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public void add(OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
    }
}
