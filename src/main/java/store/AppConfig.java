package store;

import java.util.ArrayList;
import store.controller.ConvenienceStoreController;
import store.domain.convenienceStore.ConvenienceStore;
import store.domain.convenienceStore.ConvenienceStoreInitializeByFile;
import store.domain.convenienceStore.MembershipDiscountByRate;
import store.domain.product.Products;
import store.service.ConvenienceStoreService;
import store.view.InputView;
import store.view.OutputView;

public class AppConfig {

    private ConvenienceStore convenienceStore;
    private ConvenienceStoreService convenienceStoreService;
    private ConvenienceStoreController convenienceStoreController;

    public ConvenienceStore convenienceStore() {
        if (convenienceStore == null) {
            convenienceStore = new ConvenienceStore(new ConvenienceStoreInitializeByFile().products(),
                new MembershipDiscountByRate(), new Products(new ArrayList<>()));
            return convenienceStore;
        }
        return convenienceStore;
    }

    public ConvenienceStoreService convenienceStoreService() {
        if (convenienceStoreService == null) {
            convenienceStoreService = new ConvenienceStoreService(convenienceStore());
            return convenienceStoreService;
        }
        return convenienceStoreService;
    }

    public ConvenienceStoreController convenienceStoreController() {
        if (convenienceStoreController == null) {
            convenienceStoreController = new ConvenienceStoreController(convenienceStore(), convenienceStoreService(),
                new InputView(), new OutputView());
            return convenienceStoreController;
        }
        return convenienceStoreController;
    }
}
