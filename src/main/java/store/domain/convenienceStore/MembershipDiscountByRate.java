package store.domain.convenienceStore;

import store.domain.product.CommonProduct;
import store.domain.product.Products;
import store.domain.product.PromotionProduct;

public class MembershipDiscountByRate implements MembershipDiscount{

    private static final double DISCOUNT_RATE = 0.3;

    @Override
    public int getDiscountPrice(Products purchasedProducts) {
        int promotionProductPrice = getMembershipDiscountIncludedPromotionProductPrice(purchasedProducts);
        int commonProductPrice = getMembershipDiscountIncludedCommonProductPrice(purchasedProducts);
        int totalMembershipDiscountPrice = promotionProductPrice + commonProductPrice;
        return (int) Math.round(totalMembershipDiscountPrice * DISCOUNT_RATE);
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
