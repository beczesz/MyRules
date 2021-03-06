package com.exarlabs.android.myrules.business.rule.event.plugins.math;

import java.util.HashSet;
import java.util.Set;

import com.exarlabs.android.myrules.business.rule.event.EventHandlerPlugin;

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
        NumberEvent event = createNewEvent();
        event.setValue(i);
        dispatchEvent(event);
    }

    @Override
    public Set<String> getRequiredPermissions() {
        return new HashSet<>();

    }


    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
