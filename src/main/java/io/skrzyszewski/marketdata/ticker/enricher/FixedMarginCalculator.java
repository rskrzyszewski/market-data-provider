package io.skrzyszewski.marketdata.ticker.enricher;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FixedMarginCalculator implements MarginCalculator {
    private final static BigDecimal commission = new BigDecimal("0.1");

    @Override
    public BigDecimal calculateBidMargin(BigDecimal baseValue) {
        return baseValue.subtract(baseValue.multiply(commission));
    }

    @Override
    public BigDecimal calculateAskMargin(BigDecimal baseValue) {
        return baseValue.add(baseValue.multiply(commission));
    }
}
