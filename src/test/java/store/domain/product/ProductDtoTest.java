package store.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ProductDtoTest {

    @DisplayName("문자열로부터 ProductDto 생성")
    @Test
    void 문자열로부터_Product_Dto_생성() {
        List<ProductDto> from = ProductDto.from("[콜라-2], [사이다-3]");

        assertThat(from).contains(ProductDto.of("콜라", 2), ProductDto.of("사이다", 3));
    }

    @DisplayName("잘못된 문자열 입력시 에러 출력")
    @Test
    void 잘못된_문자열_입력시_에러() {

        assertThatThrownBy(() -> ProductDto.from("[콜라-2], 사이다-3]"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
    }

}