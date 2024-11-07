package store.domain.order.parser;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringToMapParser implements Parser<Map<String, Integer>>{
    private static final Pattern ITEM_PATTERN = Pattern.compile("\\[(.+?)-(\\d+)]");

    @Override
    public Map<String, Integer> parse(String inputString) {
        Map<String, Integer> orderProducts = new LinkedHashMap<>();

        Matcher matcher = ITEM_PATTERN.matcher(inputString);
        while (matcher.find()) {
            String name = matcher.group(1);
            int quantity = Integer.parseInt(matcher.group(2));
            orderProducts.put(name, quantity);
        }
        return orderProducts;
    }
}
