package store.domain.order;

import java.util.Iterator;
import java.util.List;

public class OrderProducts implements Iterable<OrderProduct>{
    private final List<OrderProduct> orderProducts;

    public OrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public void add(OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
    }

    @Override
    public Iterator<OrderProduct> iterator() {
        return orderProducts.iterator();
    }
}
