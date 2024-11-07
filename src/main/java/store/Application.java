package store;

import store.domain.convenienceStore.ConvenienceStore;
import store.domain.convenienceStore.ConvenienceStoreInitializeByFile;
import store.domain.order.parser.StringToMapParser;
import store.domain.order.Order;
import store.domain.order.OrderProducts;
import store.domain.order.parser.StringToOrderProductsParser;
import store.service.ConvenienceStoreService;

public class Application {
    public static void main(String[] args) {
        ConvenienceStore convenienceStore = new ConvenienceStore(new ConvenienceStoreInitializeByFile().products());
        ConvenienceStoreService convenienceStoreService = new ConvenienceStoreService(
            convenienceStore);

        System.out.println(convenienceStore);

        System.out.println();
        StringToOrderProductsParser stringToOrderProductsParser = new StringToOrderProductsParser();
        StringToMapParser stringToMapParser = new StringToMapParser();

        convenienceStoreService.checkout(stringToMapParser.parse("[콜라-13]"));

//
//        OrderProducts orderProducts = stringToOrderProductsParser.parse("[콜라-13]");
//        Order order = Order.createOrder(orderProducts);
//
//        convenienceStoreService.checkout(order);

        System.out.println(convenienceStore);
    }
}
