package store.service;

import java.util.ArrayList;
import java.util.Map;


import store.domain.convenienceStore.ConvenienceStore;
import store.domain.product.Products;

public class ConvenienceStoreService {

    private final ConvenienceStore convenienceStore;

    public ConvenienceStoreService(ConvenienceStore convenienceStore) {
        this.convenienceStore = convenienceStore;
    }

    public Products checkout(Map<String, Integer> orderProducts) {
        Products products = new Products(new ArrayList<>());
        for (Map.Entry<String, Integer> orderProduct : orderProducts.entrySet()) {
            String productName = orderProduct.getKey();
            int quantity = orderProduct.getValue();
            products.add(convenienceStore.findProduct(productName, quantity));
        }
        return products;
    }

}
