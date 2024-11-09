package store.domain.product;

public record ProductDto(
        String name,
        int quantity
) {
    public static ProductDto from(Product product) {
        return new ProductDto(product.getName(), product.getQuantity());
    }

    public static ProductDto of(String name, int quantity) {
        return new ProductDto(name, quantity);
    }
}
