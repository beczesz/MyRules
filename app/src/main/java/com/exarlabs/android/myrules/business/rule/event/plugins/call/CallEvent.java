package com.exarlabs.android.myrules.business.rule.event.plugins.call;

import com.exarlabs.android.myrules.business.rule.event.Event;

/**
 * Implementation of an Call event
 *
 * Created by atiyka on 1/22/2016.
 */
public class CallEvent extends Event {

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
