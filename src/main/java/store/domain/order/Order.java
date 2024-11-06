package store.domain.order;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.ArrayList;
import store.domain.product.CommonProduct;
import store.domain.product.Product;
import store.domain.product.Products;
import store.domain.product.PromotionProduct;

public class Order {

    private final OrderProducts orderProducts;
    private final Products purchasedProducts;
    private final LocalDate orderDate;

    private Order(OrderProducts orderProducts) {
        this.orderProducts = orderProducts;
        this.purchasedProducts = new Products(new ArrayList<>());
        this.orderDate = LocalDate.from(DateTimes.now());
    }

    public static Order createOrder(OrderProducts orderProducts) {
        return new Order(orderProducts);
    }

    public OrderProducts getOrderProducts() {
        return orderProducts;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void addPurchasedProducts(Product product, int orderQuantity) {
        if (product instanceof CommonProduct) purchasedProducts.add(new CommonProduct((CommonProduct) product, orderQuantity));
        if (product instanceof PromotionProduct) purchasedProducts.add(new PromotionProduct((PromotionProduct) product, orderQuantity));
    }
}
