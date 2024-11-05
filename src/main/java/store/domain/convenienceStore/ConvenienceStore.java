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

    public void checkout(Order order) {
        OrderProducts orderProducts = order.getOrderProducts();
        for (OrderProduct orderProduct : orderProducts) {

            Product promotionProduct = products.findPromotionProduct(orderProduct);
            Product product = products.findProduct(orderProduct);

            int productQuantity = product.getQuantity(); // 일반 상품 갯수
            int orderProductQuantity = orderProduct.getQuantity(); // 주문 상품 갯수

            if(promotionProduct != null && promotionProduct.checkIfPromotionAvailable(order.getOrderDate())) { // 프로모션 적용 가능하면
                int promotionProductQuantity = promotionProduct.getQuantity(); // 프로모션 상품 갯수

                int promotionAppliedProductQuantity = promotionProduct.applyPromotionQuantity(orderProductQuantity);
                int promotionNotAppliedProductQuantity = orderProduct.getQuantity() - promotionAppliedProductQuantity;

                if (promotionProductQuantity < orderProductQuantity) {
                    promotionProduct.removeQuantity(promotionProductQuantity);
                    order.addPurchasedPromotionProducts(promotionProduct, promotionProductQuantity);

                    orderProductQuantity -= promotionProductQuantity;
                    product.removeQuantity(orderProductQuantity);
                    order.addPurchasedProducts(product, orderProductQuantity);
                }

                if (promotionProductQuantity >= orderProductQuantity) { // 프로모션 상품 재고가 남을 경우 가장 먼저 채우기
                    promotionProduct.removeQuantity(orderProductQuantity);
                    order.addPurchasedPromotionProducts(promotionProduct, orderProductQuantity);
                }
                // if () 프로모션 상품이 남았고, 딱 떨어지지 않으면 경우 몇개 더 사면 프로모션 적용 가능한지
                // if () 프로모션 상품 재고가 부족할 경우 몇개가 프로모션 적용 불가능한지
            }

            if (promotionProduct == null) {
                product.removeQuantity(orderProductQuantity);
                order.addPurchasedProducts(product, productQuantity);
            }

        }
    }
}
