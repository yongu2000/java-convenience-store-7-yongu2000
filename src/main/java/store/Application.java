package store;

import store.domain.convenienceStore.ConvenienceStore;
import store.domain.convenienceStore.ConvenienceStoreInitializeByFile;
import store.domain.order.parser.StringToMapParser;
import store.service.ConvenienceStoreService;

public class Application {
    public static void main(String[] args) {
        ConvenienceStore convenienceStore = new ConvenienceStore(new ConvenienceStoreInitializeByFile().products());
        ConvenienceStoreService convenienceStoreService = new ConvenienceStoreService(
            convenienceStore);

        System.out.println(convenienceStore);

        System.out.println();
        StringToMapParser stringToMapParser = new StringToMapParser();

        convenienceStoreService.checkout(stringToMapParser.parse("[콜라-13]"));


        System.out.println(convenienceStore);
    }
}
