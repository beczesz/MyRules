package com.exarlabs.android.myrules.business.event.plugins.math;

import com.exarlabs.android.myrules.business.event.Event;

/**
 * Example implementation of an Event which delivers an integer number
 * Created by becze on 1/11/2016.
 */
public class NumberEvent implements Event {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = NumberEvent.class.getSimpleName();


    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private int mValue;


    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public int getType() {
        return Type.RULE_EVENT_NUMBER;
    }

    @Override
    public String toString() {
        return TAG + "(Value: " + getValue() + ")";
    }


    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------


    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        mValue = value;
    }
}
