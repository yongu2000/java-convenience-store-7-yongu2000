package store.controller;

import store.domain.convenienceStore.ConvenienceStore;
import store.domain.convenienceStore.ConvenienceStoreInitializeByFile;
import store.domain.order.Choice;
import store.domain.order.parser.StringToMapParser;
import store.domain.product.Products;
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

        Products checkoutProducts = convenienceStoreService.checkout(stringToMapParser.parse("[콜라-2]"));
        Map<String, Integer> availablePromotionProducts = convenienceStoreService.availablePromotionProducts(convenienceStore, checkoutProducts);
        availablePromotionProducts.forEach((product, value) -> {
            convenienceStoreService.addPromotionProduct(Choice.YES, checkoutProducts, product, value);
        });
        System.out.println(availablePromotionProducts);
        System.out.println(convenienceStore);
    }

}
