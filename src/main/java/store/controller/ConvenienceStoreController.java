package store.controller;

import store.domain.convenienceStore.ConvenienceStore;
import store.domain.order.Choice;
import store.domain.order.Order;
import store.domain.order.parser.StringToMapParser;
import store.domain.product.Products;
import store.domain.receipt.Receipt;
import store.domain.receipt.ReceiptDto;
import store.service.ConvenienceStoreService;
import store.view.InputView;
import store.view.OutputView;

import java.util.Map;

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
        outputView.printStoreInformation(convenienceStore.toString());
        StringToMapParser stringToMapParser = new StringToMapParser();
        Products checkoutProducts = convenienceStoreService.checkout(stringToMapParser.parse(inputView.readOrder()));

        Map<String, Integer> availablePromotionProducts = convenienceStoreService.availablePromotionProducts(checkoutProducts);
        availablePromotionProducts.forEach((product, value) -> {
            convenienceStoreService.addPromotionProductToCheckout(inputView.readAddPromotionChoice(), checkoutProducts, product, value);
        });

        Map<String, Integer> unavailablePromotionProducts = convenienceStoreService.unavailablePromotionProducts(checkoutProducts);
        unavailablePromotionProducts.forEach((product, value) -> {
            convenienceStoreService.removeProductsFromCheckout(inputView.readRemovePromotionChoice(), checkoutProducts, product);
        });

        Choice membershipDiscount = inputView.readMembershipDiscountChoice();

        Order order = Order.createOrder(checkoutProducts, membershipDiscount);

        Receipt receipt = new Receipt(order.getReceiptTotal(),
                order.getReceiptPromotion(),
                order.getMembershipDiscountPrice(convenienceStore.getMembershipDiscount()));

        ReceiptDto receiptDto = ReceiptDto.from(receipt);

        outputView.printReceipt(receiptDto);

        inputView.readShopAgainChoice();
    }

}
