package io.skrzyszewski.marketdata.ticker.processor;

import io.skrzyszewski.marketdata.shared.MessageParser;
import io.skrzyszewski.marketdata.ticker.enricher.MarginEnricher;
import io.skrzyszewski.marketdata.ticker.model.Ticker;
import io.skrzyszewski.marketdata.ticker.store.TickerStoreUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TickerProcessor {
    private final MessageParser<List<Ticker>> parser;
    private final MarginEnricher marginEnricher;
    private final TickerStoreUpdater  storeUpdater;

    public void process(String message) {
         parser.parse(message).stream()
                .map(marginEnricher::enrich)
                .forEach(storeUpdater::update);
    }
}