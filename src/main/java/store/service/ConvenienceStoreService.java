package store.service;

import java.util.*;


import store.domain.convenienceStore.ConvenienceStore;
import store.domain.order.Choice;
import store.domain.product.CommonProduct;
import store.domain.product.Products;
import store.domain.product.PromotionProduct;

public class ConvenienceStoreService {

    private final ConvenienceStore convenienceStore;

    public ConvenienceStoreService(ConvenienceStore convenienceStore) {
        this.convenienceStore = convenienceStore;
    }

    public Products checkout(Map<String, Integer> orderProducts) {
        Products products = new Products(new ArrayList<>());
        for (Map.Entry<String, Integer> orderProduct : orderProducts.entrySet()) {
            String productName = orderProduct.getKey();
            int quantity = orderProduct.getValue();
            takeProducts(products, productName, quantity);
        }
        return products;
    }

    private void takeProducts(Products products, String productName, int quantity) {
        products.add(convenienceStore.findProduct(productName, quantity));
    }

    public Map<String, Integer> availablePromotionProducts(Products checkoutProducts) {
        Map<String, Integer> availablePromotionProducts = new LinkedHashMap<>();

        for (PromotionProduct promotionProduct : filterPromotionProducts(checkoutProducts)) {
            addAvailablePromotionProduct(availablePromotionProducts, promotionProduct);
        }

        return availablePromotionProducts;
    }

    private List<PromotionProduct> filterPromotionProducts(Products checkoutProducts) {
        return checkoutProducts.stream()
            .filter(PromotionProduct.class::isInstance)
            .map(PromotionProduct.class::cast)
            .toList();
    }

    private void addAvailablePromotionProduct(Map<String, Integer> availablePromotionProducts, PromotionProduct promotionProduct) {
        int availablePromotionQuantity = promotionProduct.getAvailablePromotionQuantity();
        PromotionProduct storeProduct = convenienceStore.findPromotionProductByName(promotionProduct.getName());

        if (storeProduct != null && storeProduct.getQuantity() > 0 && storeProduct.getQuantity() >= availablePromotionQuantity) {
            availablePromotionProducts.put(promotionProduct.getName(), availablePromotionQuantity);
        }
    }

    public void addPromotionProductToCheckout(Choice choice, Products checkoutProducts, String product, int value) {
        if (choice.equals(Choice.NO)) return;
        PromotionProduct promotionProduct = checkoutProducts.findPromotionProductByName(product);
        PromotionProduct conveniencePromotionProduct = convenienceStore.findPromotionProductByName(product);

        conveniencePromotionProduct.removeQuantity(value);
        promotionProduct.addQuantity(value);
    }

    public Map<String, Integer> unavailablePromotionProducts(Products checkoutProducts) {
        Map<String, Integer> unavailablePromotionProducts = new LinkedHashMap<>();

        for (PromotionProduct promotionProduct : filterPromotionProducts(checkoutProducts)) {
            addUnavailablePromotionProduct(unavailablePromotionProducts, checkoutProducts, promotionProduct);
        }

        return unavailablePromotionProducts;
    }

    private void addUnavailablePromotionProduct(Map<String, Integer> unavailablePromotionProducts, Products checkoutProducts,
        PromotionProduct promotionProduct) {
        CommonProduct commonProduct = checkoutProducts.findCommonProductByName(promotionProduct.getName());
        if (commonProduct == null) return;
        int unavailablePromotionQuantity = promotionProduct.getUnavailablePromotionQuantity();
        unavailablePromotionQuantity += commonProduct.getQuantity();

        unavailablePromotionProducts.put(promotionProduct.getName(), unavailablePromotionQuantity);
    }

    public void removeProductsFromCheckout(Choice choice, Products checkoutProducts, String product) {
        if (choice.equals(Choice.NO)) return;
        removePromotionProductFromCheckout(checkoutProducts, product);
        removeCommonProductFromCheckout(checkoutProducts, product);
    }

    private void removePromotionProductFromCheckout(Products checkoutProducts, String product) {
        PromotionProduct checkoutPromotionProduct = checkoutProducts.findPromotionProductByName(product);
        PromotionProduct conveniencePromotionProduct = convenienceStore.findPromotionProductByName(product);
        int unavailablePromotionQuantity = checkoutPromotionProduct.getUnavailablePromotionQuantity();

        checkoutPromotionProduct.removeQuantity(unavailablePromotionQuantity);
        conveniencePromotionProduct.addQuantity(unavailablePromotionQuantity);
    }

    private void removeCommonProductFromCheckout(Products checkoutProducts, String product) {
        CommonProduct checkoutCommonProduct = checkoutProducts.findCommonProductByName(product);
        CommonProduct convenienceCommonProduct = convenienceStore.findCommonProductByName(product);

        checkoutProducts.remove(checkoutCommonProduct);
        convenienceCommonProduct.addQuantity(checkoutCommonProduct.getQuantity());
    }


}
