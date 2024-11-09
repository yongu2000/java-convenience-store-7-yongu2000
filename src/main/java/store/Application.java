package store;

import store.controller.ConvenienceStoreController;
import store.domain.convenienceStore.ConvenienceStore;
import store.domain.convenienceStore.ConvenienceStoreInitializeByFile;
import store.domain.convenienceStore.MembershipDiscountByRate;
import store.domain.order.Choice;
import store.domain.order.parser.StringToMapParser;
import store.domain.product.Products;
import store.service.ConvenienceStoreService;
import store.view.InputView;
import store.view.OutputView;

import java.util.Map;

public class Application {
    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();
        ConvenienceStoreController controller = appConfig.convenienceStoreController();
        controller.run();
    }

}
