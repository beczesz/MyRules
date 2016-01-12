package com.exarlabs.android.myrules.business.event.plugins.debug;

import com.exarlabs.android.myrules.business.event.Event;

/**
 * Example event implementation
 * Created by becze on 1/11/2016.
 */
public class DebugEvent implements Event {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = DebugEvent.class.getSimpleName();


    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private String mDebugEventData1 = "SampleData1";
    private String mDebugEventData2 = "SampleData2";

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public int getType() {
        return Type.RULE_EVENT_DEBUG;
    }

    @Override
    public String toString() {
        return TAG + "\n" + mDebugEventData1 + "\n" + mDebugEventData2;
    }


    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------


    public String getDebugEventData1() {
        return mDebugEventData1;
    }

    public void setDebugEventData1(String debugEventData1) {
        mDebugEventData1 = debugEventData1;
    }

    public String getDebugEventData2() {
        return mDebugEventData2;
    }

    public void setDebugEventData2(String debugEventData2) {
        mDebugEventData2 = debugEventData2;
    }
}
