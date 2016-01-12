package com.exarlabs.android.myrules.business.condition;

import com.exarlabs.android.myrules.business.condition.plugins.AlwaysFalseConditionPlugin;
import com.exarlabs.android.myrules.business.condition.plugins.AlwaysTrueConditionPlugin;

/**
 * Factory pattern implementation for the condition plugins.
 * Created by becze on 12/18/2015.
 */
public class ConditionPluginFactory {

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
     * Creator for the condition plugins.
     *
     * @param pluginType
     * @return a new condition plugin, if no plugin found a default AlwaysTrueConditionPlugin is created.
     */
    public static ConditionPlugin create(int pluginType) {
        switch (pluginType) {
            default:
            case Condition.Type.DEBUG_ALWAYS_TRUE:
                return new AlwaysTrueConditionPlugin();

            case Condition.Type.DEBUG_ALWAYS_FALSE:
                return new AlwaysFalseConditionPlugin();
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
