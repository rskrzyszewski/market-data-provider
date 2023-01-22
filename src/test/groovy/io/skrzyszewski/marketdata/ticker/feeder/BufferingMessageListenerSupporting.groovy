package io.skrzyszewski.marketdata.ticker.feeder

import groovy.util.logging.Slf4j
import io.skrzyszewski.marketdata.shared.MessageListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import spock.util.concurrent.PollingConditions

@Slf4j
@Component
class BufferingMessageListenerSupporting implements MessageListener {

    def conditions = new PollingConditions(timeout: 20)

    @Autowired
    MessageListener messageListener


    void onMessage(String message) {
        messageListener.onMessage(message)
    }

    boolean hasAllMessagesBeenProceed() {
        return messageListener.hasAllMessagesBeenProceed()
    }

    def messagesAreProceed() {
        //ensure that there are no race condition
        sleepExactly(100)

        conditions.eventually {
            assert messageListener.hasAllMessagesBeenProceed()
        }
    }

    def sleepExactly(long milliseconds) {
        if (milliseconds < 1000) {
            log.info("Sleep $milliseconds milliseconds")
            Thread.sleep(milliseconds)
            return
        }
        log.info("Sleep 1s")
        Thread.sleep(1000)
        sleepExactly(milliseconds - 1000)
    }
}
