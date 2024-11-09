package store.view;

import store.domain.receipt.ReceiptDto;

public class OutputView {

    private static final String GREETING = "안녕하세요. W편의점입니다.\n";
    private static final String STORE_PRODUCTS = "현재 보유하고 있는 상품입니다.\n";

    private static final String RECEIPT_START = "==============W 편의점================\n";
    private static final String RECEIPT_PRODUCTS_START = "상품명\t\t수량\t금액\n";
    private static final String RECEIPT_PRODUCT = "%s\t\t%d\t%,d\n";
    private static final String RECEIPT_PROMOTION_PRODUCTS_START = "=============증\t정===============\n";
    private static final String RECEIPT_PROMOTION_PRODUCT = "%s\t\t%d\n";

    private static final String RECEIPT_END = "====================================\n";
    private static final String RECEIPT_TOTAL_PRICE = "총구매액\t\t%d\t%,d\n";
    private static final String RECEIPT_PROMOTION_DISCOUNT = "행사할인\t\t\t-%,d\n";
    private static final String RECEIPT_MEMBERSHIP_DISCOUNT = "멤버십할인\t\t\t-%,d\n";
    private static final String RECEIPT_CUSTOMER_PRICE = "내실돈\t\t\t%,d\n";


    public void printStoreInformation(String products) {
        System.out.print(GREETING);
        System.out.print(STORE_PRODUCTS);
        System.out.print(products);
        System.out.println();
    }

    public void printReceipt(ReceiptDto receiptDto) {
        System.out.print(RECEIPT_START);
        System.out.print(RECEIPT_PRODUCTS_START);

        receiptDto.products().stream().forEach(product -> {
            System.out.printf(RECEIPT_PRODUCT, product.getName(), product.getQuantity(), product.getPrice());
                }
        );
        System.out.print(RECEIPT_PROMOTION_PRODUCTS_START);
        receiptDto.promotionProducts().forEach(product -> {
            System.out.printf(RECEIPT_PROMOTION_PRODUCT, product.getName(), product.getQuantity());
        });
        System.out.print(RECEIPT_END);
        System.out.printf(RECEIPT_TOTAL_PRICE, receiptDto.totalQuantity(), receiptDto.totalPrice());
        System.out.printf(RECEIPT_PROMOTION_DISCOUNT, receiptDto.promotionDiscount());
        System.out.printf(RECEIPT_MEMBERSHIP_DISCOUNT, receiptDto.membershipDiscount());
        System.out.printf(RECEIPT_CUSTOMER_PRICE, receiptDto.customerPrice());
    }
}