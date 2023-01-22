package io.skrzyszewski.marketdata.ticker.store;

import io.skrzyszewski.marketdata.ticker.model.Ticker;

public interface TickerStoreUpdater {

    void update(Ticker ticker);
}
