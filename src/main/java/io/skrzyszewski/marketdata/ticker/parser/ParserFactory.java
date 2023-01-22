package io.skrzyszewski.marketdata.ticker.parser;

import io.skrzyszewski.marketdata.shared.MessageParser;
import io.skrzyszewski.marketdata.ticker.model.Ticker;

import java.util.List;

public interface ParserFactory {
    MessageParser<List<Ticker>> tickerParser();
}
