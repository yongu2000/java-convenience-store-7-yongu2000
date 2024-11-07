package store;

import store.controller.ConvenienceStoreController;
import store.domain.convenienceStore.ConvenienceStore;
import store.domain.convenienceStore.ConvenienceStoreInitializeByFile;
import store.domain.order.Choice;
import store.domain.order.parser.StringToMapParser;
import store.domain.product.Products;
import store.service.ConvenienceStoreService;

import java.util.Map;

public class Application {
    public static void main(String[] args) {
        ConvenienceStoreController controller = new ConvenienceStoreController(new ConvenienceStore(new ConvenienceStoreInitializeByFile().products()));
        controller.run();
    }

}
