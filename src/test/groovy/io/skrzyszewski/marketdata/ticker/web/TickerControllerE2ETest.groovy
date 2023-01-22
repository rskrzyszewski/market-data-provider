package io.skrzyszewski.marketdata.ticker.web

import groovy.json.JsonSlurper
import io.skrzyszewski.marketdata.feeder.MessageFixtures
import io.skrzyszewski.marketdata.ticker.feeder.BufferingMessageListenerSupporting
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@SpringBootTest
@AutoConfigureMockMvc
class TickerControllerE2ETest extends Specification {
    def fixtures = new MessageFixtures()
    def slurper = new JsonSlurper()

    @Autowired
    MockMvc mockMvc

    @Autowired
    BufferingMessageListenerSupporting messageListenerSupporting

    def "should return all book tickers"() {
        given: "messages have been stored"
        def messages = [fixtures.eurUsdMessage, fixtures.gbpUsdMessages]
        messages.forEach { message -> messageListenerSupporting.onMessage(message) }
        messageListenerSupporting.messagesAreProceed()

        when:
        def response = performGetRequest("/v1/tickers/bookTickers")

        then:
        response.status == HttpStatus.OK.value()

        toJson(response.contentAsString) == toJson("""
        [
           {
              "sequenceId":109,
              "instrumentSymbol":"GBP/USD",
              "bidPrice":1.12491,
              "askPrice":1.38171,
              "timestamp":"01-06-2020 12:01:02:100"
           },
           {
              "sequenceId":106,
              "instrumentSymbol":"EUR/USD",
              "bidPrice":0.99000,
              "askPrice":1.32000,
              "timestamp":"01-06-2020 12:01:01:001"
           }
        ]
       """)
    }

    def "should return a book ticker for EUR/USD"() {
        given: "messages have been stored"
        def messages = [fixtures.eurUsdMessage, fixtures.gbpUsdMessages]
        messages.forEach { message -> messageListenerSupporting.onMessage(message) }
        messageListenerSupporting.messagesAreProceed()

        when:
        def response = performGetRequest("/v1/tickers/bookTickers?instrumentSymbol=EUR/USD")

        then:
        response.status == HttpStatus.OK.value()

        toJson(response.contentAsString) == toJson("""
        [
           {
              "sequenceId":106,
              "instrumentSymbol":"EUR/USD",
              "bidPrice":0.99000,
              "askPrice":1.32000,
              "timestamp":"01-06-2020 12:01:01:001"
           }
        ]
       """)
    }

    def "should return empty book ticker for a non-existing instrument"() {
        given: "messages have been stored"
        def messages = [fixtures.eurUsdMessage, fixtures.gbpUsdMessages]
        messages.forEach { message -> messageListenerSupporting.onMessage(message) }
        messageListenerSupporting.messagesAreProceed()

        when:
        def response = performGetRequest("/v1/tickers/bookTickers?instrumentSymbol=AAA/BBB")

        then:
        response.status == HttpStatus.OK.value()

        toJson(response.contentAsString) == toJson("""
        []
       """)
    }

    protected MockHttpServletResponse performGetRequest(String path) {
        return mockMvc.perform(get(path))
                .andReturn().response
    }

    def toJson(text) {
        return slurper.parseText(text)
    }
}
