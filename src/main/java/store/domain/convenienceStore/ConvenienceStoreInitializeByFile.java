package store.domain.convenienceStore;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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

public class ConvenienceStoreInitializeByFile implements ConvenienceStoreInitialize{
    List<Promotion> promotionsFromFile = new ArrayList<>();

    @Override
    public Promotions promotions() {
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader("src/main/resources/promotions.md"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            fileReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String str;
        while (true) {
            try {
                if ((str = fileReader.readLine()) == null)
                    break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String[] split = str.split(",");
            String name = split[0];
            int buy = Integer.parseInt(split[1]);
            int get = Integer.parseInt(split[2]);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate start_date = LocalDate.parse(split[3], formatter);
            LocalDate end_date = LocalDate.parse(split[4], formatter);

            promotionsFromFile.add(Promotion.of(name, buy, get, start_date, end_date));
        }
        return new Promotions(promotionsFromFile);
    }

    @Override
    public Products products() {
        BufferedReader fileReader = null;
        List<Product> productsFromFile = new ArrayList<>();

        try {
            fileReader = new BufferedReader(new FileReader("src/main/resources/products.md"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            fileReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String str;
        while (true) {
            try {
                if ((str = fileReader.readLine()) == null)
                    break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String[] split = str.split(",");
            String name = split[0];
            int price = Integer.parseInt(split[1]);
            int quantity = Integer.parseInt(split[2]);
            Promotion promotion = null;
            if (!split[3].equals("null")) {
                 promotion = promotionsFromFile.stream()
                                                    .filter(p -> p.getName().equals(split[3]))
                                                    .findFirst()
                                                    .orElse(null);
            }
            productsFromFile.add(Product.of(name, price, quantity, promotion));
        }
        return new Products(productsFromFile);
    }
}
