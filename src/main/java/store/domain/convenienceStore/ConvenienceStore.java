package store.domain.convenienceStore;

import camp.nextstep.edu.missionutils.DateTimes;
import store.domain.product.*;
import store.domain.order.OrderProduct;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ConvenienceStore {
    private final Promotions promotions;
    private final Products products;

    public ConvenienceStore(ConvenienceStoreInitialize convenienceStoreInitialize) {
        this.promotions = convenienceStoreInitialize.promotions();
        this.products = convenienceStoreInitialize.products();
    }

    @Override
    public String toString() {
        return products.toString();
    }

    public PromotionProduct findPromotionProduct(OrderProduct orderProduct) {
        return products.findPromotionProductByName(orderProduct.getName());
    }

    public Products findProduct(String productName, int quantity) {
        checkProductExistence(productName);
        checkProductQuantity(productName, quantity);
        return takeProduct(productName, quantity);
    }

    private Products takeProduct(String productName, int quantity) {
        Products takenProducts = new Products(new ArrayList<>());

        int remainingQuantity = quantity;

        Iterator<Product> productIterator = products.stream()
                .filter(product -> product.equals(productName))
                .iterator();

        while (productIterator.hasNext() && remainingQuantity > 0) {
            Product product = productIterator.next();
            int takeQuantity = Math.min(product.getQuantity(), remainingQuantity);

            if (product instanceof PromotionProduct &&
                    promotionAvailable((PromotionProduct) product,
                            LocalDate.from(DateTimes.now()))) {
                Product takenProduct = new PromotionProduct((PromotionProduct) product, takeQuantity);
                takenProducts.add(takenProduct);

                remainingQuantity -= takeQuantity;
            }
            if (product instanceof CommonProduct) {
                Product takenProduct = new CommonProduct((CommonProduct) product, takeQuantity);
                takenProducts.add(takenProduct);

                remainingQuantity -= takeQuantity;
            }
        }
        return takenProducts;
    }

    private void checkProductExistence(String productName) {
        boolean exists = products.stream().anyMatch(product -> product.equals(productName));
        if (!exists) throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
    }

    private void checkProductQuantity(String productName, int quantity) {
        int totalQuantity = products.stream()
                .filter(product -> product.equals(productName))
                .mapToInt(Product::getQuantity)
                .sum();

        if (totalQuantity < quantity) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }

    private boolean promotionAvailable(PromotionProduct product, LocalDate date) {
        return product.checkIfPromotionAvailable(date);
    }


}
