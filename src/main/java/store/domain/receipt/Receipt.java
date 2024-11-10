package store.domain.receipt;

import java.util.List;
import store.domain.product.Product;
import store.domain.product.ProductDto;
import store.domain.product.Products;

public class Receipt {

    private final Products products;
    private final Products promotionProducts;
    private final int membershipDiscount;

    private Receipt(Products products, Products promotionProducts, int membershipDiscount) {
        this.products = products;
        this.promotionProducts = promotionProducts;
        this.membershipDiscount = membershipDiscount;
    }

    public static Receipt of(Products products, Products promotionProducts, int membershipDiscount) {
        return new Receipt(products, promotionProducts, membershipDiscount);
    }

    public List<ProductDto> getProducts() {
        return products.stream()
            .map(product -> ProductDto.of(product.getName(), product.getPrice(), product.getQuantity())).toList();
    }

    public List<ProductDto> getPromotionProducts() {
        return promotionProducts.stream()
            .map(product -> ProductDto.of(product.getName(), product.getPrice(), product.getQuantity())).toList();
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
