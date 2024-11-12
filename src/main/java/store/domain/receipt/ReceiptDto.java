package store.domain.receipt;

import java.util.List;
import store.domain.product.ProductDto;

public record ReceiptDto(
    List<ProductDto> products,
    List<ProductDto> promotionProducts,
    int totalQuantity,
    int totalPrice,
    int promotionDiscount,
    int membershipDiscount,
    int customerPrice
) {

    public static ReceiptDto from(Receipt receipt) {
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
