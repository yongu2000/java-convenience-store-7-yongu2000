package store.domain.conveniencestore;

import store.domain.product.Products;

public interface MembershipDiscount {

    int getDiscountPrice(Products purchasedProducts);
}
