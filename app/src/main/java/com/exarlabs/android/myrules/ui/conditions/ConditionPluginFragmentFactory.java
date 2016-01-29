package com.exarlabs.android.myrules.ui.conditions;

import com.exarlabs.android.myrules.business.rule.condition.Condition;
import com.exarlabs.android.myrules.ui.conditions.plugins.DefaultConditionPluginFragment;
import com.exarlabs.android.myrules.ui.conditions.plugins.math.EqualConditionPluginFragment;
import com.exarlabs.android.myrules.ui.conditions.plugins.math.IntervalConditionPluginFragment;
import com.exarlabs.android.myrules.ui.conditions.plugins.contact.ContactIsInGroupConditionPluginFragment;

/**
 * Factory pattern implementation for the condition plugin fragments.
 * Created by becze on 12/18/2015.
 */
public class ConditionPluginFragmentFactory {

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
     * Creator for the condition plugins frgaments.
     *
     * @param pluginType
     * @return a new condition plugin fragment, if no plugin found a default AlwaysTrueConditionPlugin is created.
     */
    public static ConditionPluginFragment create(int pluginType) {
        switch (pluginType) {
            default:
                return DefaultConditionPluginFragment.newInstance();

            case Condition.Type.ARITHMETRIC_IS_NUMBER_IN_INTERVAL:
                return IntervalConditionPluginFragment.newInstance();

            case Condition.Type.ARITHMETRIC_IS_NUMBER_EQUAL:
                return EqualConditionPluginFragment.newInstance();

            // Contacts
            case Condition.Type.CONTACT_IS_IN_GROUP:
                return ContactIsInGroupConditionPluginFragment.newInstance();

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
