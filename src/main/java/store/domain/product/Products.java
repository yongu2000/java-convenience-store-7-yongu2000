package store.domain.product;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Products {

    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public void add(Product product) {
        products.add(product);
    }

    public void add(Products products) {
        products.stream().forEach(this.products::add);
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

    public Stream<Product> stream() {
        return products.stream();
    }

}
