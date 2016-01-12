package com.exarlabs.android.myrules.business.event;

import com.exarlabs.android.myrules.business.condition.Condition;
import com.exarlabs.android.myrules.business.condition.ConditionPlugin;
import com.exarlabs.android.myrules.business.condition.plugins.AlwaysFalseConditionPlugin;
import com.exarlabs.android.myrules.business.condition.plugins.AlwaysTrueConditionPlugin;
import com.exarlabs.android.myrules.business.event.plugins.debug.DebugEvent;

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
            case Event.Type.RULE_EVENT_DEBUG:
                return new DebugEvent();
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
