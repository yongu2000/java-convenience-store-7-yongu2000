package store.domain.receipt;

import store.domain.product.Product;
import store.domain.product.Products;

public class Receipt {
    private final Products receiptTotal;
    private final Products receiptPromotion;
    private final int membershipDiscount;

    public Receipt(Products receiptTotal, Products receiptPromotion, int membershipDiscount) {
        this.receiptTotal = receiptTotal;
        this.receiptPromotion = receiptPromotion;
        this.membershipDiscount = membershipDiscount;
    }

    public int getTotalPrice() {
        return receiptTotal.stream().mapToInt(Product::getPrice).sum();
    }

    public int getPromotionDiscount() {
        return receiptPromotion.stream().mapToInt(Product::getPrice).sum();
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getCustomerPrice() {
        return getTotalPrice() - getPromotionDiscount() - getMembershipDiscount();
    }
}
