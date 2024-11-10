package store.domain.order;

import static store.view.ErrorMessage.*;

import java.util.Arrays;
import store.view.ErrorMessage;

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
                .orElseThrow(() -> new IllegalArgumentException(INVALID_CHOICE_FORMAT.getMessage()));
    }
}
