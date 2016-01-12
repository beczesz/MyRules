package com.exarlabs.android.myrules.business.event;

/**
 * Created by becze on 12/18/2015.
 */
public interface Event {

    /**
     * Rule event types
     */
    public static class Type {
        public static final int RULE_EVENT_DEBUG = 1000;
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    /**
     * @return the type of the event
     */
    int getType();
}
