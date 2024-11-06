package store.domain.convenienceStore;

import store.domain.product.Products;
import store.domain.product.Promotions;

public interface ConvenienceStoreInitialize {

    Promotions promotions();
    Products products();

}
