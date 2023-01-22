package io.skrzyszewski.marketdata.ticker.store;

import io.skrzyszewski.marketdata.ticker.model.Ticker;

import java.util.Collection;
import java.util.Optional;

public interface TickerStoreReader {
    Optional<Ticker> getByInstrumentSymbol(String instrumentSymbol);

    Collection<Ticker> getAll();
}
