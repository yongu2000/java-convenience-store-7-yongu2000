package store.view;

public enum ErrorMessage {
    CANNOT_READ_FILE("[ERROR] 파일을 읽을 수 없습니다"),
    INVALID_CHOICE_FORMAT("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요."),
    INVALID_PRODUCT_FORMAT("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    UNKNOWN_PRODUCT_TYPE("[ERROR] 알 수 없는 제품 유형입니다."),
    PRODUCT_NOT_EXISTS("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."),
    NOT_ENOUGH_STOCK("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.")
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
