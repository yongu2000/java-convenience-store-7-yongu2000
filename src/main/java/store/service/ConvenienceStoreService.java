package store.service;

import java.util.*;


import store.domain.convenienceStore.ConvenienceStore;
import store.domain.order.Choice;
import store.domain.product.CommonProduct;
import store.domain.product.ProductDto;
import store.domain.product.Products;
import store.domain.product.PromotionProduct;

public class ConvenienceStoreService {

    private final ConvenienceStore convenienceStore;

    public ConvenienceStoreService(ConvenienceStore convenienceStore) {
        this.convenienceStore = convenienceStore;
    }

    public void checkout(List<ProductDto> orderProducts) {
        for (ProductDto orderProduct : orderProducts) {
            takeProducts(convenienceStore.counter(), orderProduct.name(), orderProduct.quantity());
        }
    }

    private void takeProducts(Products products, String productName, int quantity) {
        products.add(convenienceStore.findProduct(productName, quantity));
    }

    public List<ProductDto> availablePromotionProducts(Products checkoutProducts) {
        List<ProductDto> availablePromotionProducts = new ArrayList<>();
        for (PromotionProduct promotionProduct : filterPromotionProducts(checkoutProducts)) {
            addAvailablePromotionProduct(availablePromotionProducts, promotionProduct);
        }

        return availablePromotionProducts;
    }

    private List<PromotionProduct> filterPromotionProducts(Products checkoutProducts) {
        return checkoutProducts.stream()
            .filter(PromotionProduct.class::isInstance)
            .map(PromotionProduct.class::cast)
            .filter(PromotionProduct::promotionIsApplicable)
            .toList();
    }

    private void addAvailablePromotionProduct(List<ProductDto> availablePromotionProducts, PromotionProduct promotionProduct) {
        int availablePromotionQuantity = promotionProduct.getAvailablePromotionQuantity();
        PromotionProduct storeProduct = convenienceStore.findPromotionProductByName(promotionProduct.getName());

        if (storeProduct != null && storeProduct.getQuantity() > 0 && storeProduct.getQuantity() >= availablePromotionQuantity) {
            availablePromotionProducts.add(ProductDto.of(promotionProduct.getName(), availablePromotionQuantity));
        }
    }

    public void addPromotionProductToCheckout(Choice choice, Products checkoutProducts, String product, int value) {
        if (choice.equals(Choice.NO)) return;
        PromotionProduct promotionProduct = checkoutProducts.findPromotionProductByName(product);
        PromotionProduct conveniencePromotionProduct = convenienceStore.findPromotionProductByName(product);

        conveniencePromotionProduct.removeQuantity(value);
        promotionProduct.addQuantity(value);
    }

    public List<ProductDto> unavailablePromotionProducts(Products checkoutProducts) {
        List<ProductDto> unavailablePromotionProducts = new ArrayList<>();

        for (PromotionProduct promotionProduct : filterPromotionProducts(checkoutProducts)) {
            addUnavailablePromotionProduct(unavailablePromotionProducts, checkoutProducts, promotionProduct);
        }

        return unavailablePromotionProducts;
    }

    private void addUnavailablePromotionProduct(List<ProductDto> unavailablePromotionProducts, Products checkoutProducts,
        PromotionProduct promotionProduct) {
        CommonProduct commonProduct = checkoutProducts.findCommonProductByName(promotionProduct.getName());
        if (commonProduct == null) return;
        int unavailablePromotionQuantity = promotionProduct.getUnavailablePromotionQuantity();
        unavailablePromotionQuantity += commonProduct.getQuantity();

        unavailablePromotionProducts.add(ProductDto.of(promotionProduct.getName(), unavailablePromotionQuantity));
    }

    public void removeProductsFromCheckout(Choice choice, Products checkoutProducts, String product) {
        if (choice.equals(Choice.YES)) return;
        removePromotionProductFromCheckout(checkoutProducts, product);
        removeCommonProductFromCheckout(checkoutProducts, product);
    }

    private void removePromotionProductFromCheckout(Products checkoutProducts, String product) {
        PromotionProduct checkoutPromotionProduct = checkoutProducts.findPromotionProductByName(product);
        PromotionProduct conveniencePromotionProduct = convenienceStore.findPromotionProductByName(product);
        int unavailableCheckoutPromotionProductQuantity = checkoutPromotionProduct.getUnavailablePromotionQuantity();
        checkoutPromotionProduct.removeQuantity(unavailableCheckoutPromotionProductQuantity);
        conveniencePromotionProduct.addQuantity(unavailableCheckoutPromotionProductQuantity);
    }

    private void removeCommonProductFromCheckout(Products checkoutProducts, String product) {
        CommonProduct checkoutCommonProduct = checkoutProducts.findCommonProductByName(product);
        CommonProduct convenienceCommonProduct = convenienceStore.findCommonProductByName(product);
        int unavailableCheckoutCommonProductQuantity = checkoutCommonProduct.getQuantity();
        checkoutProducts.removeCommonProduct(checkoutCommonProduct);
        convenienceCommonProduct.addQuantity(unavailableCheckoutCommonProductQuantity);

    }

}
