package store.service;

import java.time.LocalDate;
import store.domain.product.CommonProduct;
import store.domain.product.Product;
import store.domain.convenienceStore.ConvenienceStore;
import store.domain.order.Order;
import store.domain.order.OrderProduct;
import store.domain.order.OrderProducts;
import store.domain.product.PromotionProduct;

public class ConvenienceStoreService {

    private final ConvenienceStore convenienceStore;

    public ConvenienceStoreService(ConvenienceStore convenienceStore) {
        this.convenienceStore = convenienceStore;
    }

    public void checkout(Order order) {
        OrderProducts orderProducts = order.getOrderProducts();
        for (OrderProduct orderProduct : orderProducts) {
            checkoutProduct(order, orderProduct);
        }
    }

    private void checkoutProduct(Order order, OrderProduct orderProduct) {
        CommonProduct product = convenienceStore.findProduct(orderProduct);
        PromotionProduct promotionProduct = convenienceStore.findPromotionProduct(orderProduct);

        if (promotionAvailable(promotionProduct, order.getOrderDate())) {
            onlyPromotionProduct(order, orderProduct, promotionProduct);
            commonAndPromotionProduct(order, orderProduct, product, promotionProduct);
        }
        if (promotionProduct == null) {
            onlyCommonProduct(order, orderProduct, product);
        }
    }

    private boolean promotionAvailable(PromotionProduct product, LocalDate date) {
        return product != null && product.checkIfPromotionAvailable(date);
    }

    private void onlyPromotionProduct(Order order, OrderProduct orderProduct, PromotionProduct promotionProduct) {
        int promotionQuantity = promotionProduct.getQuantity();
        int orderQuantity = orderProduct.getQuantity();

        if (promotionQuantity >= orderQuantity) {
            order.addPurchasedProducts(promotionProduct, orderQuantity);
        }
    }

    private void commonAndPromotionProduct(Order order, OrderProduct orderProduct, CommonProduct product, PromotionProduct promotionProduct) {
        int promotionProductQuantity = promotionProduct.getQuantity(); // 프로모션 상품 갯수
        int orderQuantity = orderProduct.getQuantity();

        if (promotionProductQuantity < orderQuantity) {
            order.addPurchasedProducts(promotionProduct, promotionProductQuantity);
            orderQuantity -= promotionProductQuantity;
            order.addPurchasedProducts(product, orderQuantity);
        }
    }

    private void onlyCommonProduct(Order order, OrderProduct orderProduct, Product product) {
        int orderQuantity = orderProduct.getQuantity();
        order.addPurchasedProducts(product, orderQuantity);
    }
}
