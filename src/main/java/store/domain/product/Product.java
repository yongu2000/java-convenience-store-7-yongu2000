package store.domain.product;

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

    public boolean equals(String productName) {
        return productName.equals(name);
    }

    public int getQuantity() {
        return quantity;
    }

    public void removeQuantity(int value) {
        quantity -= value;
    }

    public void setQuantity(int value) {
        quantity = value;
    }

    public void addQuantity(int value) {
        quantity += value;
    };
}
