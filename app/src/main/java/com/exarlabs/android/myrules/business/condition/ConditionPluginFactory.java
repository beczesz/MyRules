package com.exarlabs.android.myrules.business.condition;

import com.exarlabs.android.myrules.business.condition.plugins.AlwaysFalseConditionPlugin;
import com.exarlabs.android.myrules.business.condition.plugins.AlwaysTrueConditionPlugin;
import com.exarlabs.android.myrules.business.condition.plugins.IsNumberEqualConditionPlugin;
import com.exarlabs.android.myrules.business.condition.plugins.IsNumberInIntervalConditionPlugin;
import com.exarlabs.android.myrules.business.condition.plugins.IsNumberPrimeConditionPlugin;

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

            case Condition.Type.ARITHMETRIC_IS_NUMBER_EQUAL:
                return new IsNumberEqualConditionPlugin();

            case Condition.Type.ARITHMETRIC_IS_NUMBER_IN_INTERVAL:
                return new IsNumberInIntervalConditionPlugin();

            case Condition.Type.ARITHMETRIC_IS_NUMBER_PRIME:
                return new IsNumberPrimeConditionPlugin();
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
