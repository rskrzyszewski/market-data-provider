package io.skrzyszewski.marketdata.ticker.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder(toBuilder = true)
public record Ticker(long sequenceId,
                     String instrumentSymbol,
                     BigDecimal bidPrice,
                     BigDecimal askPrice,
                     //TODO change a timestamp to a long type in order to reduce sending size
                     String timestamp) {
}
