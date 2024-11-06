package store.domain.convenienceStore;

import store.domain.Product;
import store.domain.Products;
import store.domain.Promotions;
import store.domain.order.Order;
import store.domain.order.OrderProduct;
import store.domain.order.OrderProducts;

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

    public Product findPromotionProduct(OrderProduct orderProduct) {
        return products.findPromotionProductByName(orderProduct.getName());
    }

    public Product findProduct(OrderProduct orderProduct) {
        return products.findProductByName(orderProduct.getName());
    }
}
