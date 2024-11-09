package store.domain.order;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import store.domain.convenienceStore.MembershipDiscount;
import store.domain.product.Product;
import store.domain.product.Products;
import store.domain.product.PromotionProduct;

public class Order {

    private final Products purchasedProducts;
    private final LocalDate orderDate;
    private final Choice membershipDiscountStatus;

    private Order(Products orderProducts, Choice membershipDiscountStatus) {
        this.purchasedProducts = orderProducts;
        this.membershipDiscountStatus = membershipDiscountStatus;
        this.orderDate = LocalDate.from(DateTimes.now());
    }

    public static Order createOrder(Products orderProducts, Choice membershipDiscount) {
        return new Order(orderProducts, membershipDiscount);
    }

    public TotalPrice getTotalPrice() {
        Map<String, Integer> totalPrice = new LinkedHashMap<>();
        List<Product> products = new ArrayList<>();
        purchasedProducts.stream().forEach(product ->
                totalPrice.merge(product.getName(), product.getTotalPrice(), Integer::sum)
        );
        return new TotalPrice(totalPrice);
    }

    public TotalPrice getTotalPromotionPrice() {
        Map<String, Integer> totalPrice = new LinkedHashMap<>();
        purchasedProducts.stream()
                .filter(product -> product instanceof PromotionProduct)
                .map(PromotionProduct.class::cast)
                .forEach(product -> totalPrice.put(product.getName(), product.getPromotionDiscountPrice()));
        return new TotalPrice(totalPrice);
    }

    public int getMembershipDiscountPrice(MembershipDiscount membershipDiscount) {
        if (membershipDiscountStatus.equals(Choice.YES)) return membershipDiscount.getDiscountPrice(purchasedProducts);
        return 0;
    }
}
