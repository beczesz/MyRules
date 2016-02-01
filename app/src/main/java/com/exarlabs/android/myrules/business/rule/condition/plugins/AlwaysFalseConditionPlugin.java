package com.exarlabs.android.myrules.business.rule.condition.plugins;

import com.exarlabs.android.myrules.business.rule.condition.ConditionPlugin;
import com.exarlabs.android.myrules.business.rule.event.Event;

/**
 * Created by becze on 12/18/2015.
 */
public class AlwaysFalseConditionPlugin extends ConditionPlugin {

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
    public AlwaysFalseConditionPlugin(int pluginType) {
        super(pluginType);
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public boolean evaluate(Event event) {
        return false;
    }

    @Override
    public String[] getRequiredPermissions() {
        return new String[] {};
    }
    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
