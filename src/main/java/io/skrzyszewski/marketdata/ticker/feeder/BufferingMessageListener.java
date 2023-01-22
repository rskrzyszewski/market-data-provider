package io.skrzyszewski.marketdata.ticker.feeder;

import io.skrzyszewski.marketdata.shared.MessageListener;
import io.skrzyszewski.marketdata.ticker.processor.TickerProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class BufferingMessageListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(BufferingMessageListener.class);
    private static final int QUEUE_CAPACITY = 4096;

    private final Thread messageWorker;
    private final TickerProcessor tickerProcessor;

    // note, there is one queue for both data and signaling events so that the event can
    // be processed in dispatcher thread and with that no need of thread synchronization
    private final BlockingQueue<String> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

    private volatile boolean isRunning = false;
    private long nextTimeQueueSizeCheck = 0L;

    public BufferingMessageListener(TickerProcessor tickerProcessor) {
        this.messageWorker = new Thread(this::run, "message-worker");
        this.tickerProcessor = tickerProcessor;
    }

    @Override
    public void onMessage(String message) {
        tickerProcessor.process(message);
    }

    @Override
    public boolean hasAllMessagesBeenProceed() {
        return queue.isEmpty();
    }

    protected void run() {
        while (isRunning || !hasAllMessagesBeenProceed()) {
            processMessageFromQueue();
        }
    }

    public void start() {
        logger.info("Starting the message worker");
        isRunning = true;
        messageWorker.start();
    }

    public void stop() {
        logger.info("Stopping the message worker");
        isRunning = false;
        logger.info("Processing {} messages on closeup", queue.size());
        try {
            messageWorker.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }


    private void processMessageFromQueue() {
        logQueueSize();
        try {
            String msg = queue.poll(1, TimeUnit.SECONDS);
            if (msg == null) {
                return;
            }
            onMessage(msg);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private void logQueueSize() {
        if (System.currentTimeMillis() < nextTimeQueueSizeCheck) {
            // checking `nextTimeQueueSizeCheck` is to prevent massive amounts of logs when there is some burst of incoming messages
            return;
        }
        if (queue.remainingCapacity() < QUEUE_CAPACITY / 2) {
            logger.warn("Messages queue size: {} exceeds half of its capacity. Remaining capacity: {}", queue.size(), queue.remainingCapacity());
        }

        nextTimeQueueSizeCheck = System.currentTimeMillis() + 5000;
    }

}
