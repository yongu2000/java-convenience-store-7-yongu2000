package store.domain.product;

import java.util.List;
import java.util.stream.Collectors;

public class Products {

    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public void add(Product product) {
        products.add(product);
    }

    @Override
    public String toString() {
        return products.stream()
            .map(Product::toString)
            .collect(Collectors.joining("\n"));
    }

    public PromotionProduct findPromotionProductByName(String productName) {
        return (PromotionProduct) products.stream().filter(p -> p.equals(productName) && p instanceof PromotionProduct).findFirst().orElse(null);
    }

    public CommonProduct findProductByName(String productName) {
        return (CommonProduct) products.stream().filter(p -> p.equals(productName) && p instanceof CommonProduct).findFirst().orElse(null);
    }
}
