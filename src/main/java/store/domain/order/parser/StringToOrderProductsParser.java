package store.domain.order.parser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.domain.order.OrderProduct;
import store.domain.order.OrderProducts;

public class StringToOrderProductsParser implements Parser<OrderProducts> {

    private static final Pattern ITEM_PATTERN = Pattern.compile("\\[(.+?)-(\\d+)]");

    @Override
    public OrderProducts parse(String inputString) {
        OrderProducts orderProducts = new OrderProducts(new ArrayList<>());

        Matcher matcher = ITEM_PATTERN.matcher(inputString);
        while (matcher.find()) {
            String name = matcher.group(1);
            int quantity = Integer.parseInt(matcher.group(2));
            orderProducts.add(OrderProduct.of(name, quantity));
        }

        return orderProducts;
    }

}
