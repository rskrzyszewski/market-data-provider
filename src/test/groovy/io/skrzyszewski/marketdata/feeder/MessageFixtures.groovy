package io.skrzyszewski.marketdata.feeder

class MessageFixtures {

    def eurUsdMessage = content('/messages/eur-usd-message.csv')
    def gbpUsdMessages = content('/messages/gbp-usd-messages.csv')

    static content(path) {
        MessageFixtures.getResource(path).getText("UTF-8")
    }
}
