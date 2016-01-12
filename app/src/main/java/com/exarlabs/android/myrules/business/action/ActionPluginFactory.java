package com.exarlabs.android.myrules.business.action;

import com.exarlabs.android.myrules.business.action.plugins.FibonacciActionPlugin;
import com.exarlabs.android.myrules.business.action.plugins.MultiplyActionPlugin;

/**
 * Factory pattern implementation for the action plugins.
 * Created by becze on 12/18/2015.
 */
public class ActionPluginFactory {

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
     * Creator for the action plugins.
     *
     * @param pluginType
     * @return a new action plugin, if no plugin found a default AlwaysTrueConditionPlugin is created.
     */
    public static ActionPlugin create(int pluginType) {
        switch (pluginType) {
            default:
            case Action.Type.ARITHMETRIC_ACTION_FIBONACCI:
                return new FibonacciActionPlugin();

            case Action.Type.ARITHMETRIC_ACTION_MULTIPLY:
                return new MultiplyActionPlugin();

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
