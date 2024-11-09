package store.domain.product;

public class CommonProduct extends Product {

    public CommonProduct(String name, int price, int quantity) {
        super(name, price, quantity);
    }

    public CommonProduct(CommonProduct product, int quantity) {
        super(product.name, product.price, quantity);
        product.removeQuantity(quantity);
    }

    public int getTotalPrice() {
        return price * quantity;
    }
}
