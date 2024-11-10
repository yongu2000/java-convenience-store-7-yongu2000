package store.domain.convenienceStore;

import static store.view.ErrorMessage.NOT_ENOUGH_STOCK;
import static store.view.ErrorMessage.PRODUCT_NOT_EXISTS;
import static store.view.ErrorMessage.UNKNOWN_PRODUCT_TYPE;

import java.util.ArrayList;
import java.util.Iterator;
import store.domain.product.CommonProduct;
import store.domain.product.Product;
import store.domain.product.Products;
import store.domain.product.PromotionProduct;

public class ConvenienceStore {

    private final Products products;
    private final MembershipDiscount membershipDiscount;
    private Products counter;

    public ConvenienceStore(Products products, MembershipDiscount membershipDiscount, Products counter) {
        this.products = products;
        this.membershipDiscount = membershipDiscount;
        this.counter = counter;
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
        if (product instanceof PromotionProduct) {
            return new PromotionProduct((PromotionProduct) product, quantity);
        }
        if (product instanceof CommonProduct) {
            return new CommonProduct((CommonProduct) product, quantity);
        }
        throw new IllegalArgumentException(UNKNOWN_PRODUCT_TYPE.getMessage());
    }


    private void checkProductExistence(String productName) {
        boolean exists = products.stream().anyMatch(product -> product.equals(productName));
        if (!exists) {
            throw new IllegalArgumentException(PRODUCT_NOT_EXISTS.getMessage());
        }
    }

    private void checkProductQuantity(String productName, int quantity) {
        int totalQuantity = products.stream()
            .filter(product -> product.equals(productName))
            .mapToInt(Product::getQuantity)
            .sum();
        if (totalQuantity < quantity) {
            throw new IllegalArgumentException(NOT_ENOUGH_STOCK.getMessage());
        }
    }

    public MembershipDiscount getMembershipDiscount() {
        return membershipDiscount;
    }

    public Products counter() {
        return this.counter;
    }

    public void clearCounter() {
        counter = new Products(new ArrayList<>());
    }
}
