package com.exarlabs.android.myrules.business.event.plugins.math;

import com.exarlabs.android.myrules.business.event.Event;
import com.exarlabs.android.myrules.business.event.EventFactory;
import com.exarlabs.android.myrules.business.event.EventHandlerPlugin;

/**
 * Just a timer which displatches an event in every second.
 * Created by becze on 1/11/2016.
 */
public class NumberEventHandlerPlugin extends EventHandlerPlugin {

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

    public NumberEventHandlerPlugin() {
        super();
    }



    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------


    @Override
    protected boolean initPlugin() {
        return false;
    }

    /**
     * Dispatches a number event with the given integer
     *
     * @param i
     */
    public void dispatchNumber(int i) {
        NumberEvent event = (NumberEvent) EventFactory.create(Event.Type.RULE_EVENT_NUMBER);
        event.setValue(i);
        dispatchEvent(event);
    }


    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
