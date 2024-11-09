package store.domain.receipt;

import store.domain.product.Product;
import store.domain.product.Products;

import java.util.List;

public record ReceiptDto (
    Products products,
    List<Product> promotionProducts,
    int totalQuantity,
    int totalPrice,
    int promotionDiscount,
    int membershipDiscount,
    int customerPrice
) {

    public static ReceiptDto from(Receipt receipt){
        return new ReceiptDto(
            receipt.getProducts(),
            receipt.getPromotionProducts(),
            receipt.getTotalQuantity(),
            receipt.getTotalPrice(),
            receipt.getPromotionDiscount(),
            receipt.getMembershipDiscount(),
            receipt.getCustomerPrice()

        );
    }
}
