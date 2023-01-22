package io.skrzyszewski.marketdata.ticker.web;

import io.skrzyszewski.marketdata.ticker.model.Ticker;
import io.skrzyszewski.marketdata.ticker.store.TickerStoreReader;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/tickers")
public class TickerController {

    private final TickerStoreReader tickerStoreReader;

    @GetMapping("/bookTickers")
    Collection<Ticker> listBookTickers(@RequestParam(required = false) String instrumentSymbol) {
        if (instrumentSymbol == null) {
            return tickerStoreReader.getAll();
        }
        return filterBy(instrumentSymbol);
    }

    private List<Ticker> filterBy(String instrumentSymbol) {
        Optional<Ticker> ticker = tickerStoreReader.getByInstrumentSymbol(instrumentSymbol);
        return ticker.map(List::of)
                .orElseGet(List::of);
    }
}
