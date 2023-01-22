package io.skrzyszewski.marketdata.ticker.enricher;

import io.skrzyszewski.marketdata.shared.MarketDataEnricher;
import io.skrzyszewski.marketdata.ticker.model.Ticker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MarginEnricher implements MarketDataEnricher<Ticker> {

    private final MarginCalculator marginCalculator;

    @Override
    public Ticker enrich(Ticker ticker) {
        return ticker.toBuilder()
                .bidPrice(marginCalculator.calculateBidMargin(ticker.bidPrice()))
                .askPrice(marginCalculator.calculateAskMargin(ticker.askPrice()))
                .build();
    }
}
