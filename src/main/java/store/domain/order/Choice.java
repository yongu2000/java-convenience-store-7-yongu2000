package store.domain.order;

public enum Choice {
    YES("Y"),
    NO("N");

    private final String value;

    Choice(String value) {
        this.value = value;
    }

    public boolean equals(String input) {
        return input.equals(value);
    }
}
