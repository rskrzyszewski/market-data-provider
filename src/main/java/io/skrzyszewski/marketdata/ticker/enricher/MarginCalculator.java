package io.skrzyszewski.marketdata.ticker.enricher;

import java.math.BigDecimal;

public interface MarginCalculator {

    BigDecimal calculateBidMargin(BigDecimal baseValue);
    BigDecimal calculateAskMargin(BigDecimal baseValue);
}
