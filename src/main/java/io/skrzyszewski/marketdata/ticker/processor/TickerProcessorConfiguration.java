package io.skrzyszewski.marketdata.ticker.processor;

import io.skrzyszewski.marketdata.ticker.enricher.MarginEnricher;
import io.skrzyszewski.marketdata.ticker.parser.ParserFactory;
import io.skrzyszewski.marketdata.ticker.store.TickerStoreUpdater;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TickerProcessorConfiguration {

    @Bean
    TickerProcessor tickerProcessor(
            ParserFactory parserFactory,
            MarginEnricher marginEnricher,
            TickerStoreUpdater tickerStoreUpdater) {
        return new TickerProcessor(parserFactory.tickerParser(), marginEnricher, tickerStoreUpdater);
    }
}
