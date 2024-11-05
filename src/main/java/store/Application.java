package store;

import store.domain.convenienceStore.ConvenienceStore;
import store.domain.convenienceStore.ConvenienceStoreInitializeByFile;
import store.domain.order.Order;
import store.domain.order.OrderProducts;
import store.domain.order.parser.StringToOrderProductsParser;

public class Application {
    public static void main(String[] args) {
        ConvenienceStore convenienceStore = new ConvenienceStore(new ConvenienceStoreInitializeByFile());
        System.out.println(convenienceStore);
        StringToOrderProductsParser stringToOrderProductsParser = new StringToOrderProductsParser();
        OrderProducts orderProducts = stringToOrderProductsParser.parse("[사이다-2],[감자칩-1]");
        Order order = Order.createOrder(orderProducts);

    }
}
