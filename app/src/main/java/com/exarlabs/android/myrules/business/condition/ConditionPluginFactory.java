package com.exarlabs.android.myrules.business.condition;

import com.exarlabs.android.myrules.business.condition.plugins.AlwaysFalseConditionPlugin;
import com.exarlabs.android.myrules.business.condition.plugins.AlwaysTrueConditionPlugin;
import com.exarlabs.android.myrules.business.condition.plugins.math.IsNumberEqualConditionPlugin;
import com.exarlabs.android.myrules.business.condition.plugins.math.IsNumberInIntervalConditionPlugin;
import com.exarlabs.android.myrules.business.condition.plugins.math.IsNumberPrimeConditionPlugin;
import com.exarlabs.android.myrules.business.condition.plugins.contact.ContactIsInGroupConditionPlugin;

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

            // Contacts
            case Condition.Type.CONTACT_IS_IN_GROUP:
                return new ContactIsInGroupConditionPlugin();
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
