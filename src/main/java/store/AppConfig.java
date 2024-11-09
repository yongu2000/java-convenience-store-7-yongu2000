package store;

import store.controller.ConvenienceStoreController;
import store.domain.convenienceStore.ConvenienceStore;
import store.domain.convenienceStore.ConvenienceStoreInitializeByFile;
import store.domain.convenienceStore.MembershipDiscountByRate;
import store.service.ConvenienceStoreService;
import store.view.InputView;
import store.view.OutputView;

public class AppConfig {

    public ConvenienceStore convenienceStore() {
        return new ConvenienceStore(new ConvenienceStoreInitializeByFile().products(), new MembershipDiscountByRate());
    }

    public ConvenienceStoreService convenienceStoreService() {
        return new ConvenienceStoreService(convenienceStore());
    }

    public ConvenienceStoreController convenienceStoreController() {
        return new ConvenienceStoreController(convenienceStore(), convenienceStoreService(), new InputView(), new OutputView());
    }
}
