package store.controller;

import store.domain.convenienceStore.ConvenienceStore;
import store.domain.order.Choice;
import store.domain.order.Order;
import store.domain.product.ProductDto;
import store.domain.product.Products;
import store.domain.receipt.Receipt;
import store.domain.receipt.ReceiptDto;
import store.service.ConvenienceStoreService;
import store.view.InputView;
import store.view.OutputView;

import java.util.List;
import java.util.function.Supplier;

public class ConvenienceStoreController {

    private final ConvenienceStore convenienceStore;
    private final ConvenienceStoreService convenienceStoreService;
    private final InputView inputView;
    private final OutputView outputView;

    public ConvenienceStoreController(ConvenienceStore convenienceStore, ConvenienceStoreService convenienceStoreService, InputView inputView, OutputView outputView) {
        this.convenienceStore = convenienceStore;
        this.convenienceStoreService = convenienceStoreService;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        do {
            order();
        } while (Choice.NO != executeWithRetry(inputView::readShopAgainChoice));
    }

    public void order() {
        outputView.printStoreInformation(convenienceStore.toString());
        productsToCounter();
        promotionApplicable();
        promotionNotApplicable();
        Choice membershipDiscountStatus = membershipDiscountApply();
        Order order = confirmOrder(membershipDiscountStatus);
        convenienceStore.clearCounter();
        receipt(order);
    }

    public void productsToCounter() {
        convenienceStoreService.checkout(ProductDto.from(executeWithRetry(inputView::readOrder)));
    }

    public void promotionApplicable() {
        List<ProductDto> availablePromotionProducts = convenienceStoreService.availablePromotionProducts(convenienceStore.counter());
        availablePromotionProducts.forEach(product -> convenienceStoreService.addPromotionProductToCheckout(executeWithRetry(() -> inputView.readAddPromotionChoice(product)),
                convenienceStore.counter(),
                product.name(),
                product.quantity()));
    }

    public void promotionNotApplicable() {
        List<ProductDto> unavailablePromotionProducts = convenienceStoreService.unavailablePromotionProducts(convenienceStore.counter());
        unavailablePromotionProducts.forEach(product -> convenienceStoreService.removeProductsFromCheckout(executeWithRetry(() -> inputView.readRemovePromotionChoice(product)),
                convenienceStore.counter(),
                product.name()));
    }

    public Choice membershipDiscountApply() {
        return executeWithRetry(inputView::readMembershipDiscountChoice);
    }

    public Order confirmOrder(Choice membershipDiscount) {
        return Order.createOrder(convenienceStore.counter(), membershipDiscount);
    }

    public void receipt(Order order) {
        Receipt receipt = new Receipt(order.getReceiptTotal(),
                order.getReceiptPromotion(),
                order.getMembershipDiscountPrice(convenienceStore.getMembershipDiscount()));
        ReceiptDto receiptDto = ReceiptDto.from(receipt);
        outputView.printReceipt(receiptDto);
    }

    private <T> T executeWithRetry(Supplier<T> supplier) {
        while (true) {
            try {
                return supplier.get();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
