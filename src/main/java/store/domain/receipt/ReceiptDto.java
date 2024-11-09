package store.domain.receipt;

import store.domain.product.ProductDto;

import java.util.List;

public record ReceiptDto (
    List<ProductDto> products,
    List<ProductDto> promotionProducts,
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
