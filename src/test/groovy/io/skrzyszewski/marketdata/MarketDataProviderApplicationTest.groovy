package io.skrzyszewski.marketdata

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class MarketDataProviderApplicationTest extends Specification {

    def 'should load context'() {
        expect:
        true
    }

}
