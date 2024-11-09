package store.domain.product;

import java.util.Objects;

public abstract class Product {

    protected final String name;
    protected final int price;
    protected int quantity;

    public Product(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("- %s %d원 %s", name, price, quantity).trim();
    }

    public String getName(){
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return price == product.price && quantity == product.quantity && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, quantity);
    }

    public boolean equals(String productName) {
        return productName.equals(name);
    }

    public int getQuantity() {
        return quantity;
    }

    public void removeQuantity(int value) {
        quantity -= value;
    }

    public void addQuantity(int value) {
        quantity += value;
    };

    public int getPrice() {
        return price;
    }
}
