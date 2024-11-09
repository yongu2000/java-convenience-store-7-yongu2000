package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.convenienceStore.ConvenienceStore;
import store.domain.convenienceStore.MembershipDiscountByRate;
import store.domain.order.Choice;
import store.domain.product.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class ConvenienceStoreServiceTest {

    ConvenienceStoreService convenienceStoreService;
    private ConvenienceStore convenienceStore;
    List<ProductDto> orderProducts;
    Products products;

    @BeforeEach
    void init() {
        List<Product> productList = new ArrayList<>();
        Promotion promotion = Promotion.of("탄산2+1",
                2,
                1,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31));
        Promotion promotion2 = Promotion.of("MD추천상품",
            1,
            1,
            LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 12, 31));
        Product promotionProduct = new PromotionProduct("콜라", 1000, 10, promotion);
        Product promotionProduct2 = new PromotionProduct("사이다", 1000, 10, promotion);
        Product promotionProduct3 = new PromotionProduct("오렌지주스", 1800, 9, promotion2);
        Product commonProduct = new CommonProduct("콜라", 1000, 10);
        Product commonProduct2 = new CommonProduct("사이다", 1000, 10);
        Product commonProduct3 = new CommonProduct("에너지바", 2000, 5);

        productList.add(promotionProduct);
        productList.add(commonProduct);
        productList.add(promotionProduct2);
        productList.add(commonProduct2);
        productList.add(promotionProduct3);
        productList.add(commonProduct3);

        products = new Products(productList);
        convenienceStore = new ConvenienceStore(products, new MembershipDiscountByRate());
        convenienceStoreService = new ConvenienceStoreService(convenienceStore);
        orderProducts = new ArrayList<>();
    }

    @DisplayName("프로모션 상품만 구입 목록에 담기")
    @Test
    void 프로모션_상품만_구입_목록에_담기() {
        orderProducts.add(ProductDto.of("콜라", 3));
        convenienceStoreService.checkout(orderProducts);
        assertThat(convenienceStore.toString())
                .isEqualToIgnoringWhitespace("- 콜라 1,000원 7개 탄산2+1 - 콜라 1,000원 10개 - 사이다 1,000원 10개 탄산2+1 - 사이다 1,000원 10개 - 오렌지주스 1,800원 9개 MD추천상품 - 에너지바 2,000원 5개");
    }

    @DisplayName("프로모션 상품만 구입 목록에 담기")
    @Test
    void 일반_상품만_구입_목록에_담기() {
        orderProducts.add(ProductDto.of("에너지바", 3));
        convenienceStoreService.checkout(orderProducts);
        assertThat(convenienceStore.toString())
                .isEqualToIgnoringWhitespace("- 콜라 1,000원 10개 탄산2+1 - 콜라 1,000원 10개 - 사이다 1,000원 10개 탄산2+1 - 사이다 1,000원 10개 - 오렌지주스 1,800원 9개 MD추천상품 - 에너지바 2,000원 2개");

    }

    @DisplayName("프로모션 상품, 일반 상품 같이 구입 목록에 담기")
    @Test
    void 프로모션_일반_상품_목록에_담기() {
        orderProducts.add(ProductDto.of("콜라", 13));
        convenienceStoreService.checkout(orderProducts);
        assertThat(convenienceStore.toString())
                .isEqualToIgnoringWhitespace("- 콜라 1,000원 재고없음 탄산2+1 - 콜라 1,000원 7개 - 사이다 1,000원 10개 탄산2+1 - 사이다 1,000원 10개 - 오렌지주스 1,800원 9개 MD추천상품 - 에너지바 2,000원 5개");

    }

    @DisplayName("존재하지 않는 상품 입력시 오류")
    @Test
    void 존재하지_않는_상품_입력시_오류() {
        orderProducts.add(ProductDto.of("안녕하세요", 13));
        assertThatThrownBy(() -> convenienceStoreService.checkout(orderProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");

    }

    @DisplayName("상품 재고 초과시 오류")
    @Test
    void 상품_재고_초과시_오류() {
        orderProducts.add(ProductDto.of("콜라", 21));
        assertThatThrownBy(() -> convenienceStoreService.checkout(orderProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");

    }

    @DisplayName("프로모션 적용 가능한 상품 목록 생성")
    @Test
    void 프로모션_상품_적용_가능한_상품_목룍() {
        orderProducts.add(ProductDto.of("콜라", 2));
        orderProducts.add(ProductDto.of("사이다", 5));

        Products checkoutProducts = convenienceStoreService.checkout(orderProducts);
        List<ProductDto> productDtos = convenienceStoreService.availablePromotionProducts(checkoutProducts);

        assertThat(productDtos).hasSize(2)
            .contains(ProductDto.of("콜라", 1), ProductDto.of("사이다", 1));
    }

    @DisplayName("프로모션 재고 없는 상품은 목록에 미포함")
    @Test
    void 프로모션_재고_없는_상품_목룍_미포함() {
        orderProducts.add(ProductDto.of("콜라", 11));
        orderProducts.add(ProductDto.of("사이다", 10));

        Products checkoutProducts = convenienceStoreService.checkout(orderProducts);
        List<ProductDto> availablePromotionProducts = convenienceStoreService.availablePromotionProducts(checkoutProducts);
        assertThat(availablePromotionProducts).hasSize(0);
    }

    @DisplayName("프로모션 적용 가능한 상품 추가하기")
    @Test
    void 프로모션_적용_가능한_상품_추가() {
        orderProducts.add(ProductDto.of("콜라", 2));
        orderProducts.add(ProductDto.of("사이다", 2));

        Products checkoutProducts = convenienceStoreService.checkout(orderProducts);
        List<ProductDto> availablePromotionProducts = convenienceStoreService.availablePromotionProducts(checkoutProducts);
        availablePromotionProducts.forEach(product -> {
            convenienceStoreService.addPromotionProductToCheckout(Choice.YES, checkoutProducts, product.name(), product.quantity());
        });
        assertThat(convenienceStore.toString())
                .isEqualToIgnoringWhitespace("- 콜라 1,000원 7개 탄산2+1 - 콜라 1,000원 10개 - 사이다 1,000원 7개 탄산2+1 - 사이다 1,000원 10개 - 오렌지주스 1,800원 9개 MD추천상품 - 에너지바 2,000원 5개");

        assertThat(checkoutProducts.toString())
            .isEqualToIgnoringWhitespace("- 콜라 1,000원 3개 탄산2+1 - 사이다 1,000원 3개 탄산2+1");
    }

    @DisplayName("프로모션 적용이 안되는 상품 목록 생성")
    @Test
    void 프로모션_상품_적용_불가능한_상품_목룍() {
        orderProducts.add(ProductDto.of("콜라", 13));
        orderProducts.add(ProductDto.of("사이다", 15));
        orderProducts.add(ProductDto.of("오렌지주스", 4));

        Products checkoutProducts = convenienceStoreService.checkout(orderProducts);
        List<ProductDto> availablePromotionProducts = convenienceStoreService.availablePromotionProducts(checkoutProducts);
        availablePromotionProducts.forEach(product-> {
            convenienceStoreService.addPromotionProductToCheckout(Choice.YES, checkoutProducts, product.name(), product.quantity());
        });
        List<ProductDto> unavailablePromotionProducts = convenienceStoreService.unavailablePromotionProducts(checkoutProducts);
        assertThat(unavailablePromotionProducts).hasSize(2)
            .contains(ProductDto.of("콜라", 4), ProductDto.of("사이다", 6));
    }

    @DisplayName("프로모션 적용 불가능한 상품 제거하기")
    @Test
    void 프로모션_적용_불가능한_상품_제거() {
        orderProducts.add(ProductDto.of("콜라", 13));
        orderProducts.add(ProductDto.of("사이다", 15));
        orderProducts.add(ProductDto.of("오렌지주스", 4));

        Products checkoutProducts = convenienceStoreService.checkout(orderProducts);
        List<ProductDto> availablePromotionProducts = convenienceStoreService.availablePromotionProducts(checkoutProducts);
        availablePromotionProducts.forEach(product -> {
            convenienceStoreService.addPromotionProductToCheckout(Choice.YES, checkoutProducts, product.name(), product.quantity());
        });
        List<ProductDto> unavailablePromotionProducts = convenienceStoreService.unavailablePromotionProducts(checkoutProducts);
        unavailablePromotionProducts.forEach(product -> {
            convenienceStoreService.removeProductsFromCheckout(Choice.NO, checkoutProducts, product.name());
        });

        assertThat(convenienceStore.toString())
                .isEqualToIgnoringWhitespace("- 콜라 1,000원 1개 탄산2+1 - 콜라 1,000원 10개 - 사이다 1,000원 1개 탄산2+1 - 사이다 1,000원 10개 - 오렌지주스 1,800원 5개 MD추천상품 - 에너지바 2,000원 5개");
        assertThat(checkoutProducts.toString())
            .isEqualToIgnoringWhitespace("- 콜라 1,000원 9개 탄산2+1 - 사이다 1,000원 9개 탄산2+1 - 오렌지주스 1,800원 4개 MD추천상품");
    }

}