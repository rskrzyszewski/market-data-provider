package io.skrzyszewski.marketdata.shared;

public interface MessageParser<T> {
    T parse(String msg);
}
