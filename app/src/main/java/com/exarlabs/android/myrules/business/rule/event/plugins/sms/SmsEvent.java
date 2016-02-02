package com.exarlabs.android.myrules.business.rule.event.plugins.sms;

import com.exarlabs.android.myrules.business.rule.event.Event;

/**
 * Implementation of an SMS event
 *
 * Created by atiyka on 1/21/2016.
 */
public class SmsEvent extends Event {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = SmsEvent.class.getSimpleName();


    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private String mSender;
    private String mMessage;


    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return TAG + "(Sender: " + mSender + ", Message: " + mMessage +")";
    }


    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------

    public void setSender(String sender) {
        this.mSender = sender;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public String getSender() {
        return mSender;
    }

    public String getMessage() {
        return mMessage;
    }
}
