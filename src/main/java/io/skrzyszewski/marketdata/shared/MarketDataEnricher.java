package io.skrzyszewski.marketdata.shared;

public interface MarketDataEnricher<T> {
    T enrich(T msg);
}
