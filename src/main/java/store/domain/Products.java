package store.domain;

import java.util.List;
import java.util.stream.Collectors;
import store.domain.order.OrderProduct;

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

    public Product findPromotionProduct(OrderProduct orderProduct) {
        return products.stream().filter(p -> p.equals(orderProduct) && p.hasPromotion()).findFirst().orElse(null);
    }

    public Product findProduct(OrderProduct orderProduct) {
        return products.stream().filter(p -> p.equals(orderProduct) && !p.hasPromotion()).findFirst().orElse(null);
    }
}
