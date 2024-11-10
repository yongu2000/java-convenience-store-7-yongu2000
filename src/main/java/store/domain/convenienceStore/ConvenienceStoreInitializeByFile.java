package store.domain.convenienceStore;

import static store.view.ErrorMessage.CANNOT_FIND_PROMOTION;
import static store.view.ErrorMessage.CANNOT_READ_FILE;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

    private static final String DELIMITER = ",";
    private static final String NO_PROMOTION = "null";

    private static final int PROMOTION_NAME = 0;
    private static final int PROMOTION_BUY = 1;
    private static final int PROMOTION_GET = 2;
    private static final int PROMOTION_START_DATE = 3;
    private static final int PROMOTION_END_DATE = 4;

    private static final int PRODUCT_NAME = 0;
    private static final int PRODUCT_PRICE = 1;
    private static final int PRODUCT_QUANTITY = 2;
    private static final int PRODUCT_PROMOTION = 3;
    
    @Override
    public Products products() {
        List<Promotion> promotionsFromFile = loadPromotionsFromFile();
        List<Product> productsFromFile = loadProductsFromFile(promotionsFromFile);
        List<Product> products = makeCommonProduct(productsFromFile);
        return new Products(products);
    }

    private List<Promotion> loadPromotionsFromFile() {
        try (BufferedReader reader = openFile(PROMOTIONS_FILE_PATH)) {
            reader.readLine();
            return parsePromotions(reader);
        } catch (IOException e) {
            throw new RuntimeException(CANNOT_READ_FILE.getMessage());
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
        try (BufferedReader reader = openFile(PRODUCTS_FILE_PATH)) {
            reader.readLine();
            return parseProducts(reader, promotions);
        } catch (IOException e) {
            throw new RuntimeException(CANNOT_READ_FILE.getMessage());
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
        String[] promotion = promotionString.split(DELIMITER);
        return Promotion.of(
            promotion[PROMOTION_NAME],
            Integer.parseInt(promotion[PROMOTION_BUY]),
            Integer.parseInt(promotion[PROMOTION_GET]),
            LocalDate.parse(promotion[PROMOTION_START_DATE], DATE_FORMATTER),
            LocalDate.parse(promotion[PROMOTION_END_DATE], DATE_FORMATTER)
        );
    }

    private Product parseProduct(String line, List<Promotion> promotions) {
        String[] product = line.split(DELIMITER);
        return createProduct(product[PRODUCT_NAME],
            Integer.parseInt(product[PRODUCT_PRICE]),
            Integer.parseInt(product[PRODUCT_QUANTITY]),
            product[PRODUCT_PROMOTION],
            promotions);
    }

    private Product createProduct(String name, int price, int quantity, String promotionString,
        List<Promotion> promotions) {
        if (promotionString.equals(NO_PROMOTION)) {
            return new CommonProduct(name, price, quantity);
        }
        Promotion promotion = findPromotion(promotionString, promotions);
        return new PromotionProduct(name, price, quantity, promotion);
    }

    private Promotion findPromotion(String promotionString, List<Promotion> promotions) {
        return promotions.stream()
            .filter(promotion -> promotion.toString().equals(promotionString))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(CANNOT_FIND_PROMOTION.getMessage()));
    }

    private List<Product> makeCommonProduct(List<Product> productsFromFile) {
        List<Product> orderedProducts = new ArrayList<>();
        for (Product product : productsFromFile) {
            orderedProducts.add(product);
            CommonProduct commonProduct = new CommonProduct(product.getName(), product.getPrice(), 0);
            if (product instanceof PromotionProduct && !productsFromFile.contains(commonProduct)) {
                orderedProducts.add(commonProduct);
            }
        }
        return orderedProducts;
    }
}