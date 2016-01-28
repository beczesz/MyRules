package com.exarlabs.android.myrules.business.condition.plugins;

import java.util.List;

import com.exarlabs.android.myrules.business.condition.ConditionPlugin;
import com.exarlabs.android.myrules.business.event.Event;
import com.exarlabs.android.myrules.model.dao.RuleConditionProperty;

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
    public void initialize(List<RuleConditionProperty> properties) {
        // do nothing
    }

    @Override
    public boolean evaluate(Event event) {
        return false;
    }
    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
