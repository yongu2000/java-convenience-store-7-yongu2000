package store.controller;

import store.domain.convenienceStore.ConvenienceStore;
import store.domain.order.Choice;
import store.domain.order.Order;
import store.domain.order.parser.StringToMapParser;
import store.domain.product.Products;
import store.domain.receipt.Receipt;
import store.service.ConvenienceStoreService;

import java.util.Map;

public class ConvenienceStoreController {

    private final ConvenienceStore convenienceStore;

    public ConvenienceStoreController(ConvenienceStore convenienceStore) {
        this.convenienceStore = convenienceStore;
    }

    public void run() {
        ConvenienceStoreService convenienceStoreService = new ConvenienceStoreService(
                convenienceStore);

        System.out.println(convenienceStore);
        System.out.println();

        StringToMapParser stringToMapParser = new StringToMapParser();

        Products checkoutProducts = convenienceStoreService.checkout(stringToMapParser.parse("[콜라-13]"));

        Map<String, Integer> availablePromotionProducts = convenienceStoreService.availablePromotionProducts(checkoutProducts);
        availablePromotionProducts.forEach((product, value) -> {
            convenienceStoreService.addPromotionProductToCheckout(Choice.YES, checkoutProducts, product, value);
        });
        System.out.println(availablePromotionProducts);

        Map<String, Integer> unavailablePromotionProducts = convenienceStoreService.unavailablePromotionProducts(checkoutProducts);
        unavailablePromotionProducts.forEach((product, value) -> {
            convenienceStoreService.removeProductsFromCheckout(Choice.NO, checkoutProducts, product);
        });
        System.out.println(unavailablePromotionProducts);

        Choice membershipDiscount = Choice.ofString("Y");
        Order order = Order.createOrder(checkoutProducts, membershipDiscount);

        Products totalPrice = order.getReceiptTotal();
        Products totalPromotionPrice = order.getReceiptPromotion();
        int membershipDiscountPrice = order.getMembershipDiscountPrice(convenienceStore.getMembershipDiscount());

        Receipt receipt = new Receipt(totalPrice, totalPromotionPrice, membershipDiscountPrice);

        System.out.println(receipt.getTotalPrice());
        System.out.println(receipt.getPromotionDiscount());
        System.out.println(receipt.getMembershipDiscount());
        System.out.println(receipt.getCustomerPrice());
        System.out.println(convenienceStore);
    }

}
