package store.domain.convenienceStore;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import store.domain.product.CommonProduct;
import store.domain.product.Product;
import store.domain.product.Products;
import store.domain.product.Promotion;
import store.domain.product.PromotionProduct;

public class ConvenienceStoreInitializeByFile implements ConvenienceStoreInitialize {

    private static final String PROMOTIONS_FILE_PATH = "src/main/resources/promotions.md";
    private static final String PRODUCTS_FILE_PATH = "src/main/resources/products.md";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Products products() {
        List<Promotion> promotionsFromFile = loadPromotionsFromFile();
        List<Product> productsFromFile = loadProductsFromFile(promotionsFromFile);
        return new Products(productsFromFile);
    }

    private List<Promotion> loadPromotionsFromFile() {
        try (BufferedReader reader = openFile(ConvenienceStoreInitializeByFile.PROMOTIONS_FILE_PATH)) {
            reader.readLine();
            return parsePromotions(reader);
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] 파일을 읽을 수 없습니다: " + ConvenienceStoreInitializeByFile.PROMOTIONS_FILE_PATH);
        }
    }

    private List<Promotion> parsePromotions(BufferedReader reader) throws IOException {
        List<Promotion> promotions = new ArrayList<>();
        String promotionString;
        while ((promotionString = reader.readLine()) != null) {
            promotions.add(parsePromotion(promotionString));
        }
        return promotions;
    }

    private List<Product> loadProductsFromFile(List<Promotion> promotions) {
        try (BufferedReader reader = openFile(ConvenienceStoreInitializeByFile.PRODUCTS_FILE_PATH)) {
            reader.readLine();
            return parseProducts(reader, promotions);
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] 파일을 읽을 수 없습니다: " + ConvenienceStoreInitializeByFile.PRODUCTS_FILE_PATH);
        }
    }

    private List<Product> parseProducts(BufferedReader reader, List<Promotion> promotions) throws IOException {
        List<Product> products = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            products.add(parseProduct(line, promotions));
        }
        return products;
    }

    private BufferedReader openFile(String filePath) throws IOException {
        return new BufferedReader(new FileReader(filePath));
    }

    private Promotion parsePromotion(String promotionString) {
        String[] split = promotionString.split(",");
        return Promotion.of(
                split[0],
                Integer.parseInt(split[1]),
                Integer.parseInt(split[2]),
                LocalDate.parse(split[3], DATE_FORMATTER),
                LocalDate.parse(split[4], DATE_FORMATTER)
        );
    }

    private Product parseProduct(String line, List<Promotion> promotions) {
        String[] split = line.split(",");
        String name = split[0];
        int price = Integer.parseInt(split[1]);
        int quantity = Integer.parseInt(split[2]);
        return createProduct(name, price, quantity, split[3], promotions);
    }

    private Product createProduct(String name, int price, int quantity, String promotionString, List<Promotion> promotions) {
        if (promotionString.equals("null")) {
            return new CommonProduct(name, price, quantity);
        }
        Promotion promotion = findPromotion(promotionString, promotions);
        return new PromotionProduct(name, price, quantity, promotion);
    }

    private Promotion findPromotion(String promotionString, List<Promotion> promotions) {
        return promotions.stream()
                .filter(promotion -> promotion.toString().equals(promotionString))
                .findFirst()
                .orElse(null);
    }
}