package store.domain.convenienceStore;

import store.domain.product.Products;

public interface MembershipDiscount {

    int getDiscountPrice(Products purchasedProducts);
}
