package store.domain.convenienceStore;

import store.domain.product.CommonProduct;
import store.domain.product.Products;
import store.domain.product.PromotionProduct;

public class MembershipDiscountByRate implements MembershipDiscount{

    private static final double DISCOUNT_RATE = 0.3;
    private static final int MAX_DISCOUNT_PRICE = 8_000;

    @Override
    public int getDiscountPrice(Products purchasedProducts) {
        int promotionProductPrice = getMembershipDiscountIncludedPromotionProductPrice(purchasedProducts);
        int commonProductPrice = getMembershipDiscountIncludedCommonProductPrice(purchasedProducts);
        int totalMembershipDiscountPrice = promotionProductPrice + commonProductPrice;
        int discountPrice = (int) Math.round(totalMembershipDiscountPrice * DISCOUNT_RATE);
        return Math.min(discountPrice, MAX_DISCOUNT_PRICE);
    }

    private int getMembershipDiscountIncludedPromotionProductPrice(Products purchasedProducts) {
        return purchasedProducts.stream()
                .filter(product -> product instanceof PromotionProduct)
                .map(PromotionProduct.class::cast)
                .mapToInt(PromotionProduct::getMembershipDiscountIncludedPrice)
                .sum();
    }

    private int getMembershipDiscountIncludedCommonProductPrice(Products purchasedProducts) {
        return purchasedProducts.stream()
                .filter(product -> product instanceof CommonProduct)
                .map(CommonProduct.class::cast)
                .mapToInt(CommonProduct::getTotalPrice)
                .sum();
    }
}
