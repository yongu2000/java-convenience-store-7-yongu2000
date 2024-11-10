package store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.convenienceStore.ConvenienceStore;
import store.domain.convenienceStore.MembershipDiscountByRate;
import store.domain.order.Choice;
import store.domain.product.CommonProduct;
import store.domain.product.Product;
import store.domain.product.ProductDto;
import store.domain.product.Products;
import store.domain.product.Promotion;
import store.domain.product.PromotionProduct;

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
        convenienceStore = new ConvenienceStore(products, new MembershipDiscountByRate(),
            new Products(new ArrayList<>()));
        convenienceStoreService = new ConvenienceStoreService(convenienceStore);
        orderProducts = new ArrayList<>();
    }

    @DisplayName("프로모션 상품만 구입 목록에 담기")
    @Test
    void 프로모션_상품만_구입_목록에_담기() {
        orderProducts.add(ProductDto.of("콜라", 3));
        convenienceStoreService.checkout(orderProducts);
        assertThat(convenienceStore.toString().replaceAll("\\s", "")).contains("-콜라1,000원7개탄산2+1");
    }

    @DisplayName("일반 상품만 구입 목록에 담기")
    @Test
    void 일반_상품만_구입_목록에_담기() {
        orderProducts.add(ProductDto.of("에너지바", 3));
        convenienceStoreService.checkout(orderProducts);
        assertThat(convenienceStore.toString().replaceAll("\\s", "")).contains("-에너지바2,000원2개");
    }

    @DisplayName("프로모션 상품, 일반 상품 같이 구입 목록에 담기")
    @Test
    void 프로모션_일반_상품_목록에_담기() {
        orderProducts.add(ProductDto.of("콜라", 13));
        convenienceStoreService.checkout(orderProducts);
        assertThat(convenienceStore.toString().replaceAll("\\s", "")).contains("-콜라1,000원재고없음탄산2+1", "-콜라1,000원7개");
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

        convenienceStoreService.checkout(orderProducts);
        List<ProductDto> productDtos = convenienceStoreService.availablePromotionProducts();

        assertThat(productDtos).hasSize(2)
            .contains(ProductDto.of("콜라", 1), ProductDto.of("사이다", 1));
    }

    @DisplayName("프로모션 재고 없는 상품은 목록에 미포함")
    @Test
    void 프로모션_재고_없는_상품_목룍_미포함() {
        orderProducts.add(ProductDto.of("콜라", 11));
        orderProducts.add(ProductDto.of("사이다", 10));

        convenienceStoreService.checkout(orderProducts);
        List<ProductDto> availablePromotionProducts = convenienceStoreService.availablePromotionProducts();
        assertThat(availablePromotionProducts).hasSize(0);
    }

    @DisplayName("프로모션 적용 가능한 상품 추가하기")
    @Test
    void 프로모션_적용_가능한_상품_추가() {
        orderProducts.add(ProductDto.of("콜라", 2));
        orderProducts.add(ProductDto.of("사이다", 2));

        convenienceStoreService.checkout(orderProducts);
        List<ProductDto> availablePromotionProducts = convenienceStoreService.availablePromotionProducts();
        availablePromotionProducts.forEach(product -> {
            convenienceStoreService.addPromotionProductToCheckout(Choice.YES, product.name(), product.quantity());
        });
        assertThat(convenienceStore.toString().replaceAll("\\s", "")).contains("-콜라1,000원7개탄산2+1", "-사이다1,000원7개탄산2+1");
        assertThat(convenienceStore.counter().toString().replaceAll("\\s", "")).contains(
            "-콜라1,000원3개탄산2+1-사이다1,000원3개탄산2+1");
    }

    @DisplayName("프로모션 적용이 안되는 상품 목록 생성")
    @Test
    void 프로모션_상품_적용_불가능한_상품_목룍() {
        orderProducts.add(ProductDto.of("콜라", 13));
        orderProducts.add(ProductDto.of("사이다", 15));
        orderProducts.add(ProductDto.of("오렌지주스", 4));

        convenienceStoreService.checkout(orderProducts);
        List<ProductDto> availablePromotionProducts = convenienceStoreService.availablePromotionProducts();
        availablePromotionProducts.forEach(product -> {
            convenienceStoreService.addPromotionProductToCheckout(Choice.YES, product.name(), product.quantity());
        });
        List<ProductDto> unavailablePromotionProducts = convenienceStoreService.unavailablePromotionProducts();
        assertThat(unavailablePromotionProducts).hasSize(2)
            .contains(ProductDto.of("콜라", 4), ProductDto.of("사이다", 6));
    }

    @DisplayName("프로모션 적용 불가능한 상품 제거하기")
    @Test
    void 프로모션_적용_불가능한_상품_제거() {
        orderProducts.add(ProductDto.of("콜라", 13));
        orderProducts.add(ProductDto.of("사이다", 15));
        orderProducts.add(ProductDto.of("오렌지주스", 4));

        convenienceStoreService.checkout(orderProducts);
        List<ProductDto> availablePromotionProducts = convenienceStoreService.availablePromotionProducts();
        availablePromotionProducts.forEach(product -> {
            convenienceStoreService.addPromotionProductToCheckout(Choice.YES, product.name(), product.quantity());
        });
        List<ProductDto> unavailablePromotionProducts = convenienceStoreService.unavailablePromotionProducts();
        unavailablePromotionProducts.forEach(product -> {
            convenienceStoreService.removeProductsFromCheckout(Choice.NO, product.name());
        });

        assertThat(convenienceStore.toString().replaceAll("\\s", "")).contains(
            "-콜라1,000원1개탄산2+1", "-사이다1,000원1개탄산2+1", "-오렌지주스1,800원5개MD추천상품");
        assertThat(convenienceStore.counter().toString().replaceAll("\\s", "")).contains(
            "-콜라1,000원9개탄산2+1", "-사이다1,000원9개탄산2+1", "-오렌지주스1,800원4개MD추천상품");
    }

}