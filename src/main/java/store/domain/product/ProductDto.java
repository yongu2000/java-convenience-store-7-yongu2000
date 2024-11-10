package store.domain.product;

import static store.view.ErrorMessage.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public record ProductDto(
        String name,
        int price,
        int quantity
) {
    private static final Pattern ITEM_PATTERN = Pattern.compile("\\[(.+?)-(\\d+)]");

    public static ProductDto of(String name, int price, int quantity) {
        return new ProductDto(name, price, quantity);
    }

    public static ProductDto of(String name, int quantity) {
        return new ProductDto(name, 0, quantity);
    }

    public static List<ProductDto> from(String inputString) {
        validateInputString(inputString);
        return parseStringToProductDto(inputString);
    }

    private static List<ProductDto> parseStringToProductDto(String inputString) {
        List<ProductDto> orderProducts = new ArrayList<>();
        Matcher matcher = ITEM_PATTERN.matcher(inputString);
        while (matcher.find()) {
            String name = matcher.group(1);
            int quantity = Integer.parseInt(matcher.group(2));
            orderProducts.add(ProductDto.of(name, quantity));
        }
        return orderProducts;
    }

    private static void validateInputString(String inputString) {
        String[] stringProducts =  Stream.of(inputString.split(",")).map(String::trim).toArray(String[]::new);
        Arrays.stream(stringProducts).forEach(stringProduct -> {
            if (!ITEM_PATTERN.matcher(stringProduct).matches()) throw new IllegalArgumentException(INVALID_PRODUCT_FORMAT.getMessage());
        });
    }
}
