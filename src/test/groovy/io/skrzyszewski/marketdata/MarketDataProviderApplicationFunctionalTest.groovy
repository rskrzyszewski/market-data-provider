package io.skrzyszewski.marketdata

import groovy.util.logging.Slf4j
import io.skrzyszewski.marketdata.feeder.MessageFixtures
import io.skrzyszewski.marketdata.ticker.feeder.BufferingMessageListenerSupporting
import io.skrzyszewski.marketdata.ticker.model.Ticker
import io.skrzyszewski.marketdata.ticker.store.TickerStoreReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Timeout

import java.util.concurrent.TimeUnit

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@Slf4j
@Timeout(value = 30, unit = TimeUnit.SECONDS)
@SpringBootTest(webEnvironment = NONE)
class MarketDataProviderApplicationFunctionalTest extends Specification {

    def fixtures = new MessageFixtures()

    @Autowired
    BufferingMessageListenerSupporting messageListenerSupporting

    @Autowired
    TickerStoreReader storeReader


    def 'should store processed messages'() {
        given:
        def messages = [fixtures.eurUsdMessage, fixtures.gbpUsdMessages]

        when: "messages are sent"
        messages.forEach { message -> messageListenerSupporting.onMessage(message) }
        and:
        messageListenerSupporting.messagesAreProceed()

        then:
        storeReader.getAll() as Set == [Ticker.builder()
                                                .sequenceId(106)
                                                .instrumentSymbol('EUR/USD')
                                                .bidPrice(0.99000)
                                                .askPrice(1.32000)
                                                .timestamp('01-06-2020 12:01:01:001')
                                                .build(),
                                        Ticker.builder()
                                                .sequenceId(109)
                                                .instrumentSymbol('GBP/USD')
                                                .bidPrice(1.12491)
                                                .askPrice(1.38171)
                                                .timestamp('01-06-2020 12:01:02:100')
                                                .build()] as Set
    }


}
