package store.domain.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.convenienceStore.ConvenienceStore;
import store.domain.convenienceStore.MembershipDiscountByRate;
import store.domain.product.*;
import store.service.ConvenienceStoreService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

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
        Product promotionProduct2 = new PromotionProduct("사이다", 1000, 3, promotion);
        Product promotionProduct3 = new PromotionProduct("오렌지주스", 1800, 2, promotion2);
        Product commonProduct = new CommonProduct("콜라", 1000, 3);
        Product commonProduct2 = new CommonProduct("에너지바", 2000, 1);

        productList.add(promotionProduct);
        productList.add(commonProduct);
        productList.add(promotionProduct2);
        productList.add(promotionProduct3);
        productList.add(commonProduct2);

        products = new Products(productList);
    }

    @DisplayName("구매한 상품의 총구매액 반환")
    @Test
    void 구매한_상품의_총구매액() {
        Order order = Order.createOrder(products, Choice.YES);
        Products receiptTotal = order.getReceiptTotal();

        List<Product> productList = new ArrayList<>();
        Product product = new ReceiptProduct("콜라", 13000, 13);
        Product product2 = new ReceiptProduct("사이다", 3000, 3);
        Product product3 = new ReceiptProduct("오렌지주스", 3600, 2);
        Product product4 = new ReceiptProduct("에너지바", 2000, 1);
        productList.add(product);
        productList.add(product2);
        productList.add(product3);
        productList.add(product4);

        Products expectedProducts = new Products(productList);

        assertThat(receiptTotal.stream().count()).isEqualTo(4);
        assertThat(receiptTotal).isEqualTo(expectedProducts);
    }

    @DisplayName("구매한 상품의 행사할인액")
    @Test
    void 구매한_상품의_행사할인액() {
        Order order = Order.createOrder(products, Choice.YES);
        Products totalPromotionPrice = order.getReceiptPromotion();

        List<Product> productList = new ArrayList<>();
        Product product = new ReceiptProduct("콜라", 3000, 3);
        Product product2 = new ReceiptProduct("사이다", 1000, 1);
        Product product3 = new ReceiptProduct("오렌지주스", 1800, 1);
        productList.add(product);
        productList.add(product2);
        productList.add(product3);

        Products expectedProducts = new Products(productList);
        assertThat(totalPromotionPrice.stream().count()).isEqualTo(3);
        assertThat(totalPromotionPrice).isEqualTo(expectedProducts);
    }

    @DisplayName("구매한 상품의 멤버십할인액")
    @Test
    void 구매한_상품의_멤버십할인액_할인율_사용() {
        Order order = Order.createOrder(products, Choice.YES);
        int membershipDiscountPrice = order.getMembershipDiscountPrice(new MembershipDiscountByRate());
        assertThat(membershipDiscountPrice).isEqualTo(1800);
    }

}