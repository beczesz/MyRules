package com.exarlabs.android.myrules.business.event.plugins.call;

import com.exarlabs.android.myrules.business.event.Event;

/**
 * Implementation of an Call event
 *
 * Created by atiyka on 1/22/2016.
 */
public class CallEvent implements Event {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = CallEvent.class.getSimpleName();

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private String mCaller;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public int getType() {
        return Type.RULE_EVENT_CALL;
    }

    @Override
    public String toString() {
        return TAG + "(Caller: " + mCaller +")";
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------


    public void setCaller(String caller) {
        this.mCaller = caller;
    }

    public String getCaller() {
        return mCaller;
    }
}
