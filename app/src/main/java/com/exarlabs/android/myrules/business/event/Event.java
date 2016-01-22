package com.exarlabs.android.myrules.business.event;

/**
 * Created by becze on 12/18/2015.
 */
public interface Event {

    /**
     * Rule event types
     */
    public static class Type {
        public static final int RULE_EVENT_NUMBER = 1000;

        public static final int RULE_EVENT_SMS = 2000;
        public static final int RULE_EVENT_CALL = 3000;
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    /**
     * @return the type of the event
     */
    int getType();
}
