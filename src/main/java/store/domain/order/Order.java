package store.domain.order;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.ArrayList;
import store.domain.Product;
import store.domain.Products;

public class Order {

    private final OrderProducts orderProducts;
    private final Products purchasedProducts;
    private final Products purchasedPromotionProducts;
    private final LocalDate orderDate;

    private Order(OrderProducts orderProducts) {
        this.orderProducts = orderProducts;
        this.orderDate = LocalDate.from(DateTimes.now());
        this.purchasedProducts = new Products(new ArrayList<>());
        this.purchasedPromotionProducts = new Products(new ArrayList<>());
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
        purchasedProducts.add(Product.of(product, orderQuantity));
    }

    public void addPurchasedPromotionProducts(Product promotionProduct, int orderQuantity) {
        purchasedPromotionProducts.add(Product.of(promotionProduct, orderQuantity));
    }
}
