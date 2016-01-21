package com.exarlabs.android.myrules.business.event.plugins.sms;

import com.exarlabs.android.myrules.business.event.Event;
import com.exarlabs.android.myrules.business.event.EventFactory;
import com.exarlabs.android.myrules.business.event.EventHandlerPlugin;

/**
 * Just a timer which displatches an event in every second.
 * Created by becze on 1/11/2016.
 */
public class SmsEventHandlerPlugin extends EventHandlerPlugin {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public SmsEventHandlerPlugin() {
        super();
    }


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    public void dispatchSms(String sender, String message) {
        SmsEvent event = (SmsEvent) EventFactory.create(Event.Type.RULE_EVENT_SMS);
        event.setSender(sender);
        event.setMessage(message);
        dispatchEvent(event);
    }


    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
