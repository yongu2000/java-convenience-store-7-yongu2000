package store.service;

import java.time.LocalDate;
import store.domain.Product;
import store.domain.convenienceStore.ConvenienceStore;
import store.domain.order.Order;
import store.domain.order.OrderProduct;
import store.domain.order.OrderProducts;

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
        Product product = convenienceStore.findProduct(orderProduct);
        Product promotionProduct = convenienceStore.findPromotionProduct(orderProduct);

        if (promotionAvailable(promotionProduct, order.getOrderDate())) {
            onlyPromotionProduct(order, orderProduct, promotionProduct);
            commonAndPromotionProduct(order, orderProduct, product, promotionProduct);
        }
        if (promotionProduct == null) {
            onlyCommonProduct(order, orderProduct, product);
        }
    }

    private void onlyCommonProduct(Order order, OrderProduct orderProduct, Product product) {
        int orderQuantity = orderProduct.getQuantity();
        order.addPurchasedProducts(product, orderQuantity);
    }

    private void commonAndPromotionProduct(Order order, OrderProduct orderProduct, Product product, Product promotionProduct) {
        int promotionProductQuantity = promotionProduct.getQuantity(); // 프로모션 상품 갯수
        int orderQuantity = orderProduct.getQuantity();

        if (promotionProductQuantity < orderQuantity) {
            order.addPurchasedPromotionProducts(promotionProduct, promotionProductQuantity);
            orderQuantity -= promotionProductQuantity;
            order.addPurchasedProducts(product, orderQuantity);
        }
    }

    private void onlyPromotionProduct(Order order, OrderProduct orderProduct, Product promotionProduct) {
        int promotionQuantity = promotionProduct.getQuantity();
        int orderQuantity = orderProduct.getQuantity();

        if (promotionQuantity >= orderQuantity) {
            order.addPurchasedPromotionProducts(promotionProduct, orderQuantity);
        }
    }

    private boolean promotionAvailable(Product product, LocalDate date) {
        return product != null && product.checkIfPromotionAvailable(date);
    }
}
