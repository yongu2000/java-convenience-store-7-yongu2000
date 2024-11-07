package store.service;

import java.util.*;
import java.util.stream.Collectors;


import store.domain.convenienceStore.ConvenienceStore;
import store.domain.order.Choice;
import store.domain.product.CommonProduct;
import store.domain.product.Product;
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

    public Map<String, Integer> availablePromotionProducts(ConvenienceStore convenienceStore, Products checkoutProducts) {
        Map<String, Integer> availablePromotionProducts = new LinkedHashMap<>();
        List<PromotionProduct> promotionProducts = checkoutProducts.stream()
                .filter(PromotionProduct.class::isInstance)
                .map(PromotionProduct.class::cast)
                .toList();

        for (PromotionProduct promotionProduct : promotionProducts) {
            int availablePromotionQuantity = promotionProduct.getAvailablePromotionQuantity();
            PromotionProduct storeProduct = convenienceStore.findPromotionProductByName(promotionProduct.getName());

            if (storeProduct != null && storeProduct.getQuantity() > 0 && storeProduct.getQuantity() >= availablePromotionQuantity) {
                availablePromotionProducts.put(promotionProduct.getName(), availablePromotionQuantity);
            }
        }

        return availablePromotionProducts;
    }

    public void addPromotionProduct(Choice choice, Products checkoutProducts, String product, int value) {
        if (choice.equals(Choice.NO)) return;
        PromotionProduct promotionProduct = checkoutProducts.findPromotionProductByName(
            product);
        PromotionProduct conveniencePromotionProduct = convenienceStore.findPromotionProductByName(
            product);

        conveniencePromotionProduct.removeQuantity(value);
        promotionProduct.addQuantity(value);
    }
}
