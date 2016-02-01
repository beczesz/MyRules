package com.exarlabs.android.myrules.business.rule.condition.plugins;

import com.exarlabs.android.myrules.business.rule.condition.ConditionPlugin;
import com.exarlabs.android.myrules.business.rule.event.Event;

/**
 * Created by becze on 12/18/2015.
 */
public class AlwaysTrueConditionPlugin extends ConditionPlugin {

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
    public AlwaysTrueConditionPlugin(int pluginType) {
        super(pluginType);
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public boolean evaluate(Event event) {
        return true;
    }

    @Override
    public String toString() {
        return "Always returns True";
    }

    @Override
    public String[] getRequiredPermissions() {
        return new String[] {};
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
