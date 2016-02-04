package com.exarlabs.android.myrules.business.rule.action;

import java.util.List;

import javax.inject.Inject;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.Buildable;
import com.exarlabs.android.myrules.business.rule.RuleComponent;
import com.exarlabs.android.myrules.business.rule.Runnable;
import com.exarlabs.android.myrules.business.rule.action.plugins.call.RejectCallActionPlugin;
import com.exarlabs.android.myrules.business.rule.action.plugins.math.FibonacciActionPlugin;
import com.exarlabs.android.myrules.business.rule.action.plugins.math.MultiplyActionPlugin;
import com.exarlabs.android.myrules.business.rule.action.plugins.sms.ForwardSmsActionPlugin;
import com.exarlabs.android.myrules.business.rule.action.plugins.sms.SendSmsActionPlugin;
import com.exarlabs.android.myrules.business.rule.condition.ConditionTree;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.model.GreenDaoEntity;
import com.exarlabs.android.myrules.model.dao.RuleActionProperty;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.actions.ActionPluginFragment;
import com.exarlabs.android.myrules.ui.actions.plugins.contact.ForwardSMSToGroupActionPluginFragment;
import com.exarlabs.android.myrules.ui.actions.plugins.contact.SendSMSToGroupActionPluginFragment;
import com.exarlabs.android.myrules.ui.actions.plugins.debug.DefaultActionPluginFragment;
import com.exarlabs.android.myrules.ui.actions.plugins.debug.ToastIncomingEventAction;
import com.exarlabs.android.myrules.ui.actions.plugins.math.MultiplyActionPluginFragment;

/**
 * Base abstract class for all the rule actions. It applies a bridge pattern to decouple
 * the implementation from the Action type.
 * <p>
 * <p>
 * The actual Action implementation will be done by the plugins, as the condition implementation.
 *
 * @see {@link ConditionTree}
 * </p>
 * Created by becze on 1/11/2016.
 */
public abstract class Action implements GreenDaoEntity, RuleComponent, Buildable, Runnable {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    /**
     * Encapsulates the Condition Types.
     */
    public enum Type {

        // ------------------------------------------------------------------------
        // DEBUG ACTIONS
        // ------------------------------------------------------------------------
        DEBUG_TOAST_INCOMING_EVENT(1, ToastIncomingEventAction.class, DefaultActionPluginFragment.class,
                                   R.string.action_title_toast_incoming_event_action),

        // ------------------------------------------------------------------------
        // ARITHMETRIC ACTIONS
        // ------------------------------------------------------------------------

        ARITHMETRIC_ACTION_MULTIPLY(2001, MultiplyActionPlugin.class, MultiplyActionPluginFragment.class, R.string.action_title_multiply_action),
        ARITHMETRIC_ACTION_FIBONACCI(2002, FibonacciActionPlugin.class, DefaultActionPluginFragment.class, R.string.action_title_fibonacci_action),

        // ------------------------------------------------------------------------
        // SMS ACTIONS
        // ------------------------------------------------------------------------

        SEND_SMS_ACTION(3002, SendSmsActionPlugin.class, SendSMSToGroupActionPluginFragment.class, R.string.action_title_send_sms_action),
        FORWARD_SMS_ACTION(3003, ForwardSmsActionPlugin.class, ForwardSMSToGroupActionPluginFragment.class, R.string.action_title_send_sms_action),

        // ------------------------------------------------------------------------
        // CALL ACTIONS
        // ------------------------------------------------------------------------

        REJECT_CALL_ACTION(4002, RejectCallActionPlugin.class, DefaultActionPluginFragment.class, R.string.action_title_reject_call_action);


        // ------------------------------------------------------------------------
        // FIELDS
        // ------------------------------------------------------------------------
        private final int mType;
        private final Class<? extends ActionPlugin> mPlugin;
        private final Class<? extends ActionPluginFragment> mPluginFragment;
        private final int mTitleResId;


        Type(int type, Class<? extends ActionPlugin> plugin, Class<? extends ActionPluginFragment> pluginFragment, int titleResId) {
            mType = type;
            mPlugin = plugin;
            mPluginFragment = pluginFragment;
            mTitleResId = titleResId;
        }

        public int getType() {
            return mType;
        }

        public Class<? extends ActionPlugin> getPlugin() {
            return mPlugin;
        }

        public Class<? extends ActionPluginFragment> getPluginFragment() {
            return mPluginFragment;
        }

        public int getTitleResId() {
            return mTitleResId;
        }
    }

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    // condition plugin which encapsulates the implementation.
    private ActionPlugin mActionPlugin;
    private boolean isBuilt;

    @Inject
    ActionPluginManager mActionPluginManager;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public Action() {
        DaggerManager.component().inject(this);
    }


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    /**
     * @return the list of properties for this condition
     */
    public abstract List<RuleActionProperty> getProperties();

    /**
     * Run the action.
     *
     * @param event
     *
     * @return
     */
    public boolean run(Event event) {
        return getActionPlugin().run(event);
    }

    /**
     * Builds the action if it is not yet built.
     */
    public void build() {
        if (!isBuilt) {
            ActionPlugin actionPlugin = getActionPlugin();
            if (isAttached()) {
                actionPlugin.initialize(getProperties());
                isBuilt = true;
            }
        }
    }

    /**
     * Rebuilds the action
     */
    public void rebuild() {
        isBuilt = false;
        mActionPlugin = null;
        build();
    }


    /**
     * Regenerates the action plugin
     */
    public void reGenerateActionPlugin() {
        mActionPlugin = null;
        getActionPlugin();
    }

    @Override
    public String toString() {
        return isAttached() ? getActionPlugin().toString() : "Unsaved " + this;
    }


    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------

    public boolean isBuilt() {
        return isBuilt;
    }

    /**
     * @return the plugin for this condition. It is created once when the first time is called.
     */
    public ActionPlugin getActionPlugin() {
        if (mActionPlugin == null) {
            // First build this condition
            mActionPlugin = mActionPluginManager.createNewPluginInstance(getType());
        }
        return mActionPlugin;
    }
}
