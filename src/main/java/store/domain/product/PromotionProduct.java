package store.domain.product;

import java.time.LocalDate;

public class PromotionProduct extends Product{
    private final Promotion promotion;

    public PromotionProduct(String name, int price, int quantity, Promotion promotion) {
        super(name, price, quantity);
        this.promotion = promotion;
    }

    public PromotionProduct(PromotionProduct product, int quantity) {
        super(product.name, product.price, quantity);
        this.promotion = product.promotion;
        product.removeQuantity(quantity);
    }

    @Override
    public String toString() {
        return String.format("- %s %d원 %s %s", name, price, quantity, promotion).trim();
    }

    public boolean checkIfPromotionAvailable(LocalDate orderDate) {
        return promotion.checkAvailable(orderDate);
    }

    public int applyPromotionQuantity(int orderProductQuantity) {
        return promotion.getPromotionQuantity(orderProductQuantity);
    }


    public int getAvailablePromotionQuantity() {
        return promotion.getPromotionQuantity(quantity);
    }

    public int getUnavailablePromotionQuantity() {
        return promotion.getUnavailablePromotionQuantity(quantity);
    }
}
