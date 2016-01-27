package com.exarlabs.android.myrules.ui.actions;

import com.exarlabs.android.myrules.business.action.Action;
import com.exarlabs.android.myrules.ui.actions.plugins.DefaultActionPluginFragment;
import com.exarlabs.android.myrules.ui.actions.plugins.MultiplyActionPluginFragment;

/**
 * Factory pattern implementation for the condition plugin fragments.
 * Created by becze on 12/18/2015.
 */
public class ActionPluginFragmentFactory {

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
    public static ActionPluginFragment create(int pluginType) {
        switch (pluginType) {
            default:
                return DefaultActionPluginFragment.newInstance();

            case Action.Type.ARITHMETRIC_ACTION_MULTIPLY:
                return MultiplyActionPluginFragment.newInstance();

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
