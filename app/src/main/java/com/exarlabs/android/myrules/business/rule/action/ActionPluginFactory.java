package com.exarlabs.android.myrules.business.rule.action;

import com.exarlabs.android.myrules.business.rule.action.plugins.FibonacciActionPlugin;
import com.exarlabs.android.myrules.business.rule.action.plugins.MultiplyActionPlugin;
import com.exarlabs.android.myrules.business.rule.action.plugins.RejectCallActionPlugin;
import com.exarlabs.android.myrules.business.rule.action.plugins.SendSmsActionPlugin;

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
                return new FibonacciActionPlugin(pluginType);

            case Action.Type.ARITHMETRIC_ACTION_MULTIPLY:
                return new MultiplyActionPlugin(pluginType);

            case Action.Type.SEND_SMS_ACTION:
                return new SendSmsActionPlugin(pluginType);

            case Action.Type.REJECT_CALL_ACTION:
                return new RejectCallActionPlugin(pluginType);
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
