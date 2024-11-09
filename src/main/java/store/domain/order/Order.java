package store.domain.order;

import java.util.*;

import store.domain.convenienceStore.MembershipDiscount;
import store.domain.product.Product;
import store.domain.product.Products;
import store.domain.product.PromotionProduct;
import store.domain.product.ReceiptProduct;

public class Order {

    private final Products purchasedProducts;
    private final Choice membershipDiscountStatus;

    private Order(Products orderProducts, Choice membershipDiscountStatus) {
        this.purchasedProducts = orderProducts;
        this.membershipDiscountStatus = membershipDiscountStatus;
    }

    public static Order createOrder(Products orderProducts, Choice membershipDiscountStatus) {
        return new Order(orderProducts, membershipDiscountStatus);
    }

    public Products getReceiptTotal() {
        Map<String, Integer> totalProductsPrice = createTotalProductPriceData();
        Map<String, Integer> totalProductsQuantity = createTotalProductQuantityData();
        List<Product> products = new ArrayList<>();
        totalProductsPrice.keySet().forEach(productName -> {
            products.add(new ReceiptProduct(productName, totalProductsPrice.get(productName), totalProductsQuantity.get(productName)));
        });
        return new Products(products);
    }

    private Map<String, Integer> createTotalProductPriceData() {
        Map<String, Integer> totalProductsPrice = new LinkedHashMap<>();
        purchasedProducts.stream().forEach(product ->
                totalProductsPrice.merge(product.getName(), product.getPrice() * product.getQuantity(), Integer::sum)
        );
        return totalProductsPrice;
    }

    private Map<String, Integer> createTotalProductQuantityData() {
        Map<String, Integer> totalProductsQuantity = new LinkedHashMap<>();
        purchasedProducts.stream().forEach(product ->
                totalProductsQuantity.merge(product.getName(), product.getQuantity(), Integer::sum)
        );
        return totalProductsQuantity;
    }

    public Products getReceiptPromotion() {
        List<Product> products = new ArrayList<>();
        purchasedProducts.stream()
                .filter(product -> product instanceof PromotionProduct)
                .map(PromotionProduct.class::cast)
                .filter(PromotionProduct::promotionIsApplicable)
                .forEach(product -> products.add(new ReceiptProduct(product.getName(), product.getPrice() * product.getPromotionDiscountQuantity(), product.getPromotionDiscountQuantity())));
        return new Products(products);
    }

    public int getMembershipDiscountPrice(MembershipDiscount membershipDiscount) {
        if (membershipDiscountStatus.equals(Choice.YES)) return membershipDiscount.getDiscountPrice(purchasedProducts);
        return 0;
    }
}
