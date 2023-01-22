package io.skrzyszewski.marketdata.ticker.parser.jdk;


import io.skrzyszewski.marketdata.shared.MessageParser;
import io.skrzyszewski.marketdata.ticker.model.Ticker;
import io.skrzyszewski.marketdata.ticker.parser.ParserFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JdkParserFactory implements ParserFactory {

    @Override
    public MessageParser<List<Ticker>> tickerParser() {
        return new TickerParser();
    }

}
