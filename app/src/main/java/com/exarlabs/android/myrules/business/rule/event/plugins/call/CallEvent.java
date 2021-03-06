package com.exarlabs.android.myrules.business.rule.event.plugins.call;

import com.exarlabs.android.myrules.business.rule.event.plugins.ContactEvent;

/**
 * Implementation of an Call event
 * <p>
 * Created by atiyka on 1/22/2016.
 */
public class CallEvent extends ContactEvent {

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


    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return TAG + "(Caller: " + getContact() + ")";
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------

}
