package com.exarlabs.android.myrules.business.rule.action;

import java.util.List;

import com.exarlabs.android.myrules.business.rule.condition.ConditionTree;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.model.GreenDaoEntity;
import com.exarlabs.android.myrules.model.dao.RuleActionProperty;

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
public abstract class Action implements GreenDaoEntity{

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    /**
     * Encapsulates the Condition Types.
     */
    public static class Type {
        // Debug condition types
        public static final int ARITHMETRIC_ACTION_MULTIPLY = 201;
        public static final int ARITHMETRIC_ACTION_FIBONACCI = 202;


        public static final int SEND_SMS_ACTION = 301;

        public static final int REJECT_CALL_ACTION = 401;
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

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    /**
     * @return the type of the condition
     */
    public abstract int getType();

    /**
     * @return the list of properties for this condition
     */
    public abstract List<RuleActionProperty> getProperties();

    /**
     * Run the action.
     *
     * @param event
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
            getActionPlugin().initialize(getProperties());
            isBuilt = true;
        }
    }

    /**
     * Rebuilds the action
     */
    public void rebuild(){
        isBuilt = false;
        mActionPlugin = null;
        build();
    }

    /**
     * Regenerates the action plugin
     */
    public void reGenerateActionPlugin(){
        mActionPlugin = null;
        getActionPlugin();
    }

    @Override
    public String toString() {
        return isAttached() ? getActionPlugin().toString() : "Unsaved " + this ;
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
            mActionPlugin = ActionPluginFactory.create(getType());
        }
        return mActionPlugin;
    }
}
