package io.skrzyszewski.marketdata.ticker.parser.jdk;

import io.skrzyszewski.marketdata.shared.MessageParser;
import io.skrzyszewski.marketdata.ticker.model.Ticker;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

public class TickerParser implements MessageParser<List<Ticker>> {
    @Override
    public List<Ticker> parse(String message) {
        //assume the incoming message is valid
        return Stream.of(message.split("\\n"))
                .map(this::toTicker)
                .toList();
    }

    private Ticker toTicker(String message) {
        int firstCommaIdx = message.indexOf(',');
        int secondCommaIdx = message.indexOf(',', firstCommaIdx + 1);
        int thirdCommaIdx = message.indexOf(',', secondCommaIdx + 1);
        int fourthCommaIdx = message.indexOf(',', thirdCommaIdx + 1);

        //TODO It is probably worth moving it to ThreadLocal to reduce the pressure on GC. The entire flow is processed by a single thread
        final var builder = Ticker.builder();
        builder.sequenceId(Long.parseLong(message.substring(0, firstCommaIdx)));
        builder.instrumentSymbol(message.substring(firstCommaIdx + 1, secondCommaIdx));
        builder.bidPrice(new BigDecimal(message.substring(secondCommaIdx + 1, thirdCommaIdx)));
        builder.askPrice(new BigDecimal(message.substring(thirdCommaIdx + 1, fourthCommaIdx)));
        builder.timestamp(message.substring(fourthCommaIdx + 1));

        return builder.build();
    }
}
