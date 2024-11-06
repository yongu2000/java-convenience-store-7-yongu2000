package store.domain;

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

    public Product findPromotionProductByName(String productName) {
        return products.stream().filter(p -> p.equals(productName) && p.hasPromotion()).findFirst().orElse(null);
    }

    public Product findProductByName(String productName) {
        return products.stream().filter(p -> p.equals(productName) && !p.hasPromotion()).findFirst().orElse(null);
    }
}
