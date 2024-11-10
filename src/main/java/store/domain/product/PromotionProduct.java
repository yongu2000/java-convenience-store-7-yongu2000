package store.domain.product;

public class PromotionProduct extends Product {

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

    public int getAvailablePromotionQuantity() {
        return promotion.getAvailablePromotionQuantity(quantity);
    }

    public int getUnavailablePromotionQuantity() {
        return promotion.getUnavailablePromotionQuantity(quantity);
    }

    public int getMembershipDiscountIncludedPrice() {
        return getUnavailablePromotionQuantity() * price;
    }

    public int getPromotionDiscountQuantity() {
        return promotion.getAppliedPromotionQuantity(quantity);
    }

    public boolean promotionIsApplicable() {
        return promotion.isApplicable();
    }

    @Override
    public String toString() {
        if (quantity > 0) {
            return String.format("- %s %,d원 %s개 %s", name, price, quantity, promotion).trim();
        }
        return String.format("- %s %,d원 재고 없음 %s", name, price, promotion).trim();
    }
}
