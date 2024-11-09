package store.domain.product;

import store.domain.order.TotalPrice;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    public CommonProduct findCommonProductByName(String productName) {
        return (CommonProduct) products.stream().filter(p -> p.equals(productName) && p instanceof CommonProduct).findFirst().orElse(null);
    }

    public void removeCommonProduct(Product product) {
        products.removeIf(p -> (p instanceof CommonProduct && p.equals(product)));
    }

    public Stream<Product> stream() {
        return products.stream();
    }


}
