package store.domain.product;

import camp.nextstep.edu.missionutils.DateTimes;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PromotionTest {

    @DisplayName("기간에 해당하지 않는 프로모션 적용")
    @Test
    void 기간에_해당하지_않는_프로모션_적용시() {
        Promotion promotion = Promotion.of("탄산2+1",
                2,
                1,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 2, 15));
        PromotionProduct product = new PromotionProduct("콜라", 1000, 1, promotion);

        boolean promotionAvailable = product.promotionIsApplicable();
        Assertions.assertThat(promotionAvailable).isEqualTo(false);
    }
}