package store.domain.convenienceStore;

import store.domain.Products;
import store.domain.Promotions;

public class ConvenienceStore {
    private final Promotions promotions;
    private final Products products;

    public ConvenienceStore(ConvenienceStoreInitialize convenienceStoreInitialize) {
        this.promotions = convenienceStoreInitialize.promotions();
        this.products = convenienceStoreInitialize.products();
    }

    @Override
    public String toString() {
        return products.toString();
    }
}
