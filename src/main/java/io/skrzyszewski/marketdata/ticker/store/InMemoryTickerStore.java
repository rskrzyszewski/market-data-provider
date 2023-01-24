package io.skrzyszewski.marketdata.ticker.store;

import io.skrzyszewski.marketdata.ticker.model.Ticker;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryTickerStore implements TickerStoreReader, TickerStoreUpdater {

    //TODO check if possible switching to the HashMap implementation.
    // The ephemeralStorage is updated by a single thread and is read by multiple threads
    // Read first https://stackoverflow.com/questions/41952171/modifying-hash-map-from-a-single-thread-and-reading-from-multiple-threads
    private final Map<String, Ticker> ephemeralStorage = new ConcurrentHashMap<>();

    @Override
    public Optional<Ticker> getByInstrumentSymbol(String instrumentSymbol) {
        Ticker foundTicker = ephemeralStorage.get(instrumentSymbol);
        return foundTicker != null ? Optional.of(foundTicker) : Optional.empty();
    }

    @Override
    public Collection<Ticker> getAll() {
        return ephemeralStorage.values();
    }

    @Override
    public void update(Ticker ticker) {
        ephemeralStorage.put(ticker.instrumentSymbol(), ticker);
    }
}
