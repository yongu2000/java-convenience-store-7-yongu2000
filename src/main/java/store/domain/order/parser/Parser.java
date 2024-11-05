package store.domain.order.parser;

public interface Parser<T> {
    T parse(String inputString);
}
