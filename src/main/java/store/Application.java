package store;

import store.controller.ConvenienceStoreController;

public class Application {
    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();
        ConvenienceStoreController controller = appConfig.convenienceStoreController();
        controller.run();
    }
}
