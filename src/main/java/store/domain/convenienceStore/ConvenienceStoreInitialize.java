package store.domain.convenienceStore;

import store.domain.Products;
import store.domain.Promotions;

public interface ConvenienceStoreInitialize {

    Promotions promotions();
    Products products();

}
