package store.domain.convenienceStore;

import camp.nextstep.edu.missionutils.DateTimes;
import store.domain.product.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

public class ConvenienceStore {
    private final Products products;
    private final MembershipDiscount membershipDiscount;

    public ConvenienceStore(Products products, MembershipDiscount membershipDiscount) {
        this.products = products;
        this.membershipDiscount = membershipDiscount;
    }

    @Override
    public String toString() {
        return products.toString();
    }

    public Products findProduct(String productName, int quantity) {
        checkProductExistence(productName);
        checkProductQuantity(productName, quantity);
        return takeProducts(productName, quantity);
    }

    private Products takeProducts(String productName, int quantity) {
        Products takenProducts = new Products(new ArrayList<>());
        int remainingQuantity = quantity;

        Iterator<Product> products = findProductByName(productName);
        while (products.hasNext() && remainingQuantity > 0) {
            remainingQuantity = processProduct(products.next(), takenProducts, remainingQuantity);
        }
        return takenProducts;
    }

    private Iterator<Product> findProductByName(String productName) {
        return products.findProductByName(productName);
    }

    public PromotionProduct findPromotionProductByName(String productName) {
        return products.findPromotionProductByName(productName);
    }

    public CommonProduct findCommonProductByName(String productName) {
        return products.findCommonProductByName(productName);
    }

    private int processProduct(Product product, Products takenProducts, int remainingQuantity) {
        int takeQuantity = Math.min(product.getQuantity(), remainingQuantity);
        Product takenProduct = createProductsWithQuantity(product, takeQuantity);

        takenProducts.add(takenProduct);
        return remainingQuantity - takeQuantity;
    }

    private Product createProductsWithQuantity(Product product, int quantity) {
        if (product instanceof PromotionProduct &&
                promotionAvailable((PromotionProduct) product, LocalDate.from(DateTimes.now()))) {
            return new PromotionProduct((PromotionProduct) product, quantity);
        }
        if (product instanceof CommonProduct) {
            return new CommonProduct((CommonProduct) product, quantity);
        }
        throw new IllegalArgumentException("[ERROR] 알 수 없는 제품 유형입니다.");
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

    public MembershipDiscount getMembershipDiscount() {
        return membershipDiscount;
    }
}
