package store.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.convenienceStore.ConvenienceStore;
import store.domain.product.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

class ConvenienceStoreServiceTest {

    private ConvenienceStore convenienceStore;

    @BeforeEach
    void init() {
        List<Product> productList = new ArrayList<>();
        Promotion promotion = Promotion.of("탄산2+1",
                2,
                1,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31));
        Product promotionProduct = new PromotionProduct("콜라", 1000, 10, promotion);
        Product commonProduct = new CommonProduct("콜라", 1000, 10);
        Product commonProduct2 = new CommonProduct("에너지바", 2000, 5);

        productList.add(promotionProduct);
        productList.add(commonProduct);
        productList.add(commonProduct2);


        Products products = new Products(productList);

        convenienceStore = new ConvenienceStore(products);
    }

    @DisplayName("프로모션 상품만 구입 목록에 담기")
    @Test
    void 프로모션_상품만_구입_목록에_담기() {
        ConvenienceStoreService convenienceStoreService = new ConvenienceStoreService(convenienceStore);
        Map<String, Integer> orderProducts = new LinkedHashMap<>();
        orderProducts.put("콜라", 3);
        convenienceStoreService.checkout(orderProducts);
        assertThat(convenienceStore.toString())
                .isEqualToIgnoringWhitespace("- 콜라 1000원 7 탄산2+1 - 콜라 1000원 10 - 에너지바 2000원 5");
    }

    @DisplayName("프로모션 상품만 구입 목록에 담기")
    @Test
    void 일반_상품만_구입_목록에_담기() {
        ConvenienceStoreService convenienceStoreService = new ConvenienceStoreService(convenienceStore);
        Map<String, Integer> orderProducts = new LinkedHashMap<>();
        orderProducts.put("에너지바", 3);
        convenienceStoreService.checkout(orderProducts);
        assertThat(convenienceStore.toString())
                .isEqualToIgnoringWhitespace("- 콜라 1000원 10 탄산2+1 - 콜라 1000원 10 - 에너지바 2000원 2");
    }

    @DisplayName("프로모션 상품, 일반 상품 같이 구입 목록에 담기")
    @Test
    void 프로모션_일반_상품_목록에_담기() {
        ConvenienceStoreService convenienceStoreService = new ConvenienceStoreService(convenienceStore);
        Map<String, Integer> orderProducts = new LinkedHashMap<>();
        orderProducts.put("콜라", 13);
        convenienceStoreService.checkout(orderProducts);
        assertThat(convenienceStore.toString())
                .isEqualToIgnoringWhitespace("- 콜라 1000원 0 탄산2+1 - 콜라 1000원 7 - 에너지바 2000원 5");
    }

    @DisplayName("존재하지 않는 상품 입력시 오류")
    @Test
    void 존재하지_않는_상품_입력시_오류() {
        ConvenienceStoreService convenienceStoreService = new ConvenienceStoreService(convenienceStore);
        Map<String, Integer> orderProducts = new LinkedHashMap<>();
        orderProducts.put("안녕하세요", 13);
        assertThatThrownBy(() -> convenienceStoreService.checkout(orderProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");

    }

    @DisplayName("상품 재고 초과시 오류")
    @Test
    void 상품_재고_초과시_오류() {
        ConvenienceStoreService convenienceStoreService = new ConvenienceStoreService(convenienceStore);
        Map<String, Integer> orderProducts = new LinkedHashMap<>();
        orderProducts.put("콜라", 21);
        assertThatThrownBy(() -> convenienceStoreService.checkout(orderProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");

    }

}