package store.service;

import java.util.List;
import java.util.Objects;
import store.domain.convenienceStore.ConvenienceStore;
import store.domain.order.Choice;
import store.domain.product.CommonProduct;
import store.domain.product.ProductDto;
import store.domain.product.PromotionProduct;

public class ConvenienceStoreService {

    private final ConvenienceStore convenienceStore;

    public ConvenienceStoreService(ConvenienceStore convenienceStore) {
        this.convenienceStore = convenienceStore;
    }

    public void checkout(List<ProductDto> orderProducts) {
        for (ProductDto orderProduct : orderProducts) {
            takeProducts(orderProduct.name(), orderProduct.quantity());
        }
    }

    private void takeProducts(String productName, int quantity) {
        convenienceStore.counter()
            .add(convenienceStore.findProduct(productName, quantity));
    }

    public List<ProductDto> availablePromotionProducts() {
        return filterPromotionProducts().stream()
            .filter(this::isAvailableForPromotion)
            .map(product -> ProductDto.of(product.getName(), product.getAvailablePromotionQuantity()))
            .toList();
    }

    public List<ProductDto> unavailablePromotionProducts() {
        return filterPromotionProducts().stream()
            .map(this::isUnavailableForPromotion)
            .filter(Objects::nonNull)
            .toList();
    }

    private boolean isAvailableForPromotion(PromotionProduct promotionProduct) {
        int availablePromotionQuantity = promotionProduct.getAvailablePromotionQuantity();
        PromotionProduct storeProduct = convenienceStore.counter()
            .findPromotionProductByName(promotionProduct.getName());
        return promotionAvailable(storeProduct, availablePromotionQuantity);
    }

    private boolean promotionAvailable(PromotionProduct storeProduct, int availablePromotionQuantity) {
        return storeProduct != null && storeProduct.getQuantity() > 0
            && storeProduct.getQuantity() >= availablePromotionQuantity && availablePromotionQuantity > 0;
    }

    private ProductDto isUnavailableForPromotion(PromotionProduct promotionProduct) {
        CommonProduct commonProduct = convenienceStore.counter().findCommonProductByName(promotionProduct.getName());
        if (commonProduct == null) {
            return null;
        }
        int unavailablePromotionQuantity = promotionProduct.getUnavailablePromotionQuantity();
        unavailablePromotionQuantity += commonProduct.getQuantity();
        return ProductDto.of(promotionProduct.getName(), unavailablePromotionQuantity);
    }

    private List<PromotionProduct> filterPromotionProducts() {
        return convenienceStore.counter()
            .stream()
            .filter(product -> product instanceof PromotionProduct)
            .map(PromotionProduct.class::cast)
            .filter(PromotionProduct::promotionIsApplicable)
            .toList();
    }

    public void addPromotionProductToCheckout(Choice choice, String product, int value) {
        if (choice.equals(Choice.NO)) {
            return;
        }
        PromotionProduct promotionProduct = convenienceStore.counter().findPromotionProductByName(product);
        PromotionProduct conveniencePromotionProduct = convenienceStore.findPromotionProductByName(product);

        conveniencePromotionProduct.removeQuantity(value);
        promotionProduct.addQuantity(value);
    }

    public void removeProductsFromCheckout(Choice choice, String product) {
        if (choice.equals(Choice.YES)) {
            return;
        }
        removePromotionProductFromCheckout(product);
        removeCommonProductFromCheckout(product);
    }

    private void removePromotionProductFromCheckout(String product) {
        PromotionProduct checkoutPromotionProduct = convenienceStore.counter().findPromotionProductByName(product);
        PromotionProduct conveniencePromotionProduct = convenienceStore.findPromotionProductByName(product);
        int unavailableCheckoutPromotionProductQuantity = checkoutPromotionProduct.getUnavailablePromotionQuantity();
        checkoutPromotionProduct.removeQuantity(unavailableCheckoutPromotionProductQuantity);
        conveniencePromotionProduct.addQuantity(unavailableCheckoutPromotionProductQuantity);
    }

    private void removeCommonProductFromCheckout(String product) {
        CommonProduct checkoutCommonProduct = convenienceStore.counter().findCommonProductByName(product);
        CommonProduct convenienceCommonProduct = convenienceStore.findCommonProductByName(product);
        int unavailableCheckoutCommonProductQuantity = checkoutCommonProduct.getQuantity();
        convenienceStore.counter().removeCommonProduct(checkoutCommonProduct);
        convenienceCommonProduct.addQuantity(unavailableCheckoutCommonProductQuantity);
    }
}
