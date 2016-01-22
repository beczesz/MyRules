package com.exarlabs.android.myrules.business.event;

import com.exarlabs.android.myrules.business.event.plugins.math.NumberEvent;
import com.exarlabs.android.myrules.business.event.plugins.sms.SmsEvent;

/**
 * Factory pattern implementation for the events.
 * Created by becze on 12/18/2015.
 */
public class EventFactory {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    /**
     * Creator for the event plugins.
     *
     * @param type
     * @return a new event
     */
    public static Event create(int eventType) {
        switch (eventType) {
            default:
            case Event.Type.RULE_EVENT_NUMBER:
                return new NumberEvent();
            case Event.Type.RULE_EVENT_SMS:
                return new SmsEvent();
        }
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
