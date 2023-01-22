package io.skrzyszewski.marketdata.shared;

public interface MessageListener {
    void onMessage(String message);
    boolean hasAllMessagesBeenProceed();
}
