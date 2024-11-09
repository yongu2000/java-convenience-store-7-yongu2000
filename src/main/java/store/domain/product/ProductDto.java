package store.domain.product;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record ProductDto(
        String name,
        int quantity
) {
    private static final Pattern ITEM_PATTERN = Pattern.compile("\\[(.+?)-(\\d+)]");

    public static List<ProductDto> from(String inputString) {
        List<ProductDto> orderProducts = new ArrayList<>();
        Matcher matcher = ITEM_PATTERN.matcher(inputString);
        while (matcher.find()) {
            String name = matcher.group(1);
            int quantity = Integer.parseInt(matcher.group(2));
            orderProducts.add(ProductDto.of(name, quantity));
        }
        return orderProducts;
    }

    public static ProductDto of(String name, int quantity) {
        return new ProductDto(name, quantity);
    }
}
