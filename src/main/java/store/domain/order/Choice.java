package store.domain.order;

import java.util.Arrays;

public enum Choice {
    YES("Y"),
    NO("N");

    private final String value;

    Choice(String value) {
        this.value = value;
    }

    public static Choice ofString(String input) {
        return Arrays.stream(Choice.values())
                .filter(choice -> input.equals(choice.value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요."));
    }
}
