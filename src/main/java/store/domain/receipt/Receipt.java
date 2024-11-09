package store.domain.receipt;

import store.domain.product.Product;
import store.domain.product.Products;

import java.util.List;
import java.util.stream.Collectors;

public class Receipt {
    private final Products products;
    private final Products promotionProducts;
    private final int membershipDiscount;

    public Receipt(Products products, Products promotionProducts, int membershipDiscount) {
        this.products = products;
        this.promotionProducts = promotionProducts;
        this.membershipDiscount = membershipDiscount;
    }

    public Products getProducts() {
        return products;
    }

    public List<Product> getPromotionProducts() {
        return promotionProducts.stream().toList();
    }

    public int getTotalPrice() {
        return products.stream().mapToInt(Product::getPrice).sum();
    }

    public int getPromotionDiscount() {
        return promotionProducts.stream().mapToInt(Product::getPrice).sum();
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getCustomerPrice() {
        return getTotalPrice() - getPromotionDiscount() - getMembershipDiscount();
    }

    public int getTotalQuantity() {
        return products.stream().mapToInt(Product::getQuantity).sum();
    }
}
