package io.skrzyszewski.marketdata;

import io.skrzyszewski.marketdata.ticker.feeder.BufferingMessageListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@RequiredArgsConstructor
@Component
public class BufferingMessageListenerRunner {
    private final BufferingMessageListener messageListener;


    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        messageListener.start();
    }

    @PreDestroy
    void destroy() {
        messageListener.stop();
    }
}
