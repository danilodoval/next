package com.next.challenge.core.context;

/**
 * Holds important information about context and the Next Challenge system.
 */
public interface EventContext extends Context {

    String getEventName();

    String getCorrelationId();

    Object getPayload();
}
