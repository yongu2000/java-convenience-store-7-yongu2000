package store;

import store.domain.convenienceStore.ConvenienceStore;
import store.domain.convenienceStore.ConvenienceStoreInitializeByFile;

public class Application {
    public static void main(String[] args) {
        ConvenienceStore convenienceStore = new ConvenienceStore(new ConvenienceStoreInitializeByFile());
        System.out.println(convenienceStore);

    }
}
