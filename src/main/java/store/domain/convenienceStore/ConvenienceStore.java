package store.domain.convenienceStore;

import store.domain.product.CommonProduct;
import store.domain.product.Product;
import store.domain.product.Products;
import store.domain.product.PromotionProduct;
import store.domain.product.Promotions;
import store.domain.order.OrderProduct;

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

    public PromotionProduct findPromotionProduct(OrderProduct orderProduct) {
        return products.findPromotionProductByName(orderProduct.getName());
    }

    public CommonProduct findProduct(OrderProduct orderProduct) {
        return products.findProductByName(orderProduct.getName());
    }
}
