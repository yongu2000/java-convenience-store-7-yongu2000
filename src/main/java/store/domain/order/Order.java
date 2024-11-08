package store.domain.order;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;

import store.domain.product.CommonProduct;
import store.domain.product.Product;
import store.domain.product.Products;
import store.domain.product.PromotionProduct;

public class Order {

    private final Products purchasedProducts;
    private final LocalDate orderDate;
    private final Choice membershipDiscount;

    private Order(Products orderProducts, Choice membershipDiscount) {
        this.purchasedProducts = orderProducts;
        this.membershipDiscount = membershipDiscount;
        this.orderDate = LocalDate.from(DateTimes.now());
    }

    public static Order createOrder(Products orderProducts, Choice membershipDiscount) {
        return new Order(orderProducts, membershipDiscount);
    }

    public int getTotalPrice() {
        return purchasedProducts.stream()
                .mapToInt(Product::getQuantity)
                .sum();
    }
}
