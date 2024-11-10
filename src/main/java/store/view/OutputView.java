package store.view;

import store.domain.receipt.ReceiptDto;

public class OutputView {

    private static final String GREETING = "안녕하세요. W편의점입니다.\n";
    private static final String STORE_PRODUCTS = "현재 보유하고 있는 상품입니다.\n";

    private static final String RECEIPT_START = "===============W 편의점===============\n";
    private static final String RECEIPT_PRODUCTS_START = String.format("%-10s %10s %10s\n", "상품명", "수량", "금액");
    private static final String RECEIPT_PRODUCT = "%-10s %10d %,10d\n";
    private static final String RECEIPT_PROMOTION_PRODUCTS_START = "===============증   정===============\n";
    private static final String RECEIPT_PROMOTION_PRODUCT = "%-10s %10d\n";

    private static final String RECEIPT_END = "====================================\n";
    private static final String RECEIPT_TOTAL_PRICE = String.format("%-10s %%10d %%,10d\n", "총구매액");
    private static final String RECEIPT_PROMOTION_DISCOUNT = String.format("%-10s %20s%%,d\n", "행사할인", "-");
    private static final String RECEIPT_MEMBERSHIP_DISCOUNT = String.format("%-10s %20s%%,d\n", "멤버십할인", "-");
    private static final String RECEIPT_CUSTOMER_PRICE = String.format("%-10s %%,20d\n", "내실돈");

    public void printStoreInformation(String products) {
        System.out.print(GREETING);
        System.out.print(STORE_PRODUCTS);
        System.out.print(products);
        System.out.println();
    }

    public void printReceipt(ReceiptDto receiptDto) {
        System.out.print(RECEIPT_START);
        printReceiptProducts(receiptDto);
        printReceiptPromotionProducts(receiptDto);
        System.out.print(RECEIPT_END);
        printReceiptPrices(receiptDto);
    }

    private void printReceiptProducts(ReceiptDto receiptDto) {
        System.out.print(RECEIPT_PRODUCTS_START);
        receiptDto.products()
            .forEach(
                product -> System.out.printf(RECEIPT_PRODUCT, product.name(), product.quantity(), product.price()));
    }

    private void printReceiptPromotionProducts(ReceiptDto receiptDto) {
        System.out.print(RECEIPT_PROMOTION_PRODUCTS_START);
        receiptDto.promotionProducts()
            .forEach(product -> System.out.printf(RECEIPT_PROMOTION_PRODUCT, product.name(), product.quantity())
            );
    }

    private void printReceiptPrices(ReceiptDto receiptDto) {
        System.out.printf(RECEIPT_TOTAL_PRICE, receiptDto.totalQuantity(), receiptDto.totalPrice());
        System.out.printf(RECEIPT_PROMOTION_DISCOUNT, receiptDto.promotionDiscount());
        System.out.printf(RECEIPT_MEMBERSHIP_DISCOUNT, receiptDto.membershipDiscount());
        System.out.printf(RECEIPT_CUSTOMER_PRICE, receiptDto.customerPrice());
    }
}
