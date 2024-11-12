package store.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChoiceTest {

    @DisplayName("Y 입력시 Choice.YES 반환")
    @Test
    void Y_입력시_YES_반환() {
        Choice choice = Choice.ofString("Y");
        assertThat(choice).isEqualTo(Choice.YES);
    }

    @DisplayName("N 입력시 Choice.NO 반환")
    @Test
    void N_입력시_NO_반환() {
        Choice choice = Choice.ofString("N");
        assertThat(choice).isEqualTo(Choice.NO);
    }

    @DisplayName("잘못된 입력의 경우 에러")
    @Test
    void 잘못된_입력_에러_반환() {
        assertThatThrownBy(() -> Choice.ofString(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");

        assertThatThrownBy(() -> Choice.ofString("a"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");

        assertThatThrownBy(() -> Choice.ofString("123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");

        assertThatThrownBy(() -> Choice.ofString("!@#$"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");

    }

}