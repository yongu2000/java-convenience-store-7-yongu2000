package store.domain.convenienceStore;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import store.domain.Product;
import store.domain.Products;
import store.domain.Promotion;
import store.domain.Promotions;

public class ConvenienceStoreInitializeByFile implements ConvenienceStoreInitialize {

    private static final String PROMOTIONS_FILE_PATH = "src/main/resources/promotions.md";
    private static final String PRODUCTS_FILE_PATH = "src/main/resources/products.md";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Promotions promotions() {
        List<Promotion> promotionsFromFile = loadPromotionsFromFile(PROMOTIONS_FILE_PATH);
        return new Promotions(promotionsFromFile);
    }

    @Override
    public Products products() {
        List<Promotion> promotionsFromFile = loadPromotionsFromFile(PROMOTIONS_FILE_PATH);
        List<Product> productsFromFile = loadProductsFromFile(PRODUCTS_FILE_PATH, promotionsFromFile);
        return new Products(productsFromFile);
    }

    private List<Promotion> loadPromotionsFromFile(String filePath) {
        List<Promotion> promotions = new ArrayList<>();
        try (BufferedReader reader = openFile(filePath)) {
            reader.readLine(); // name,buy,get,start_date,end_date 스킵
            String line;
            while ((line = reader.readLine()) != null) {
                promotions.add(parsePromotion(line));
            }
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] 파일을 읽을 수 없습니다: " + filePath);
        }
        return promotions;
    }

    private List<Product> loadProductsFromFile(String filePath, List<Promotion> promotions) {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = openFile(filePath)) {
            reader.readLine(); // name,price,quantity,promotion 스킵
            String line;
            while ((line = reader.readLine()) != null) {
                products.add(parseProduct(line, promotions));
            }
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] 파일을 읽을 수 없습니다: " + filePath);
        }
        return products;
    }

    private BufferedReader openFile(String filePath) throws IOException {
        return new BufferedReader(new FileReader(filePath));
    }

    private Promotion parsePromotion(String line) {
        String[] split = line.split(",");
        String name = split[0];
        int buy = Integer.parseInt(split[1]);
        int get = Integer.parseInt(split[2]);
        LocalDate startDate = LocalDate.parse(split[3], DATE_FORMATTER);
        LocalDate endDate = LocalDate.parse(split[4], DATE_FORMATTER);
        return Promotion.of(name, buy, get, startDate, endDate);
    }

    private Product parseProduct(String line, List<Promotion> promotions) {
        String[] split = line.split(",");
        String name = split[0];
        int price = Integer.parseInt(split[1]);
        int quantity = Integer.parseInt(split[2]);

        Promotion promotion = null;
        if (!split[3].equals("null")) {
            promotion = promotions.stream()
                .filter(p -> p.getName().equals(split[3]))
                .findFirst()
                .orElse(null);
        }
        return Product.of(name, price, quantity, promotion);
    }
}