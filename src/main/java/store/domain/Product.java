package store.domain;

import java.util.Optional;

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

    @Override
    public String toString() {
        String promotionString = "";
        if (promotion != null) promotionString = promotion.getName();
        return String.format("- %s %d원 %s %s", name, price, quantity, promotionString).trim();
    }
}
