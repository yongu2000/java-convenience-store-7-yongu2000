package store.domain;

import java.time.LocalDate;
import store.domain.order.OrderProduct;

public class Product {

    private final String name;
    private final int price;
    private int quantity;
    private final Promotion promotion;

    private Product(String name, int price, int quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public static Product of(String name, int price, int quantity, Promotion promotion) {
        return new Product(name, price, quantity, promotion);
    }

    public static Product of(Product product, int quantity) {
        product.removeQuantity(quantity);
        return new Product(product.name, product.price, quantity, product.promotion);
    }

    @Override
    public String toString() {
        String promotionString = "";
        if (promotion != null) promotionString = promotion.toString();
        return String.format("- %s %d원 %s %s", name, price, quantity, promotionString).trim();
    }

    public boolean hasPromotion() {
        return promotion != null;
    }

    public boolean equals(String productName) {
        return productName.equals(name);
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean checkIfPromotionAvailable(LocalDate orderDate) {
        return promotion.checkAvailable(orderDate);
    }

    public void removeQuantity(int value) {
        quantity -= value;
    }

    public void setQuantity(int value) {
        quantity = value;
    }

    public int applyPromotionQuantity(int orderProductQuantity) {
        return promotion.getPromotionQuantity(orderProductQuantity);
    }
}
