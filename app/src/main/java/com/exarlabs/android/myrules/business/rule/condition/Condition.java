package com.exarlabs.android.myrules.business.rule.condition;

import java.util.List;

import com.exarlabs.android.myrules.business.rule.Buildable;
import com.exarlabs.android.myrules.business.rule.Evaluable;
import com.exarlabs.android.myrules.business.rule.RuleComponent;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.model.GreenDaoEntity;
import com.exarlabs.android.myrules.model.dao.RuleConditionProperty;

/**
 * It is a rule condition abstraction which with a bridge pattern it decouples the
 * condition implementation from the type.
 * <p>
 * The concrete condition implementation will specify the type of the condition and it's properties.
 * </p>
 * Created by becze on 12/18/2015.
 */
public abstract class Condition implements GreenDaoEntity, RuleComponent, Evaluable, Buildable{

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    /**
     * Encapsulates the Condition Types.
     */
    public static class Type {
        // Debug condition types
        public static final int DEBUG_ALWAYS_TRUE = 100;
        public static final int DEBUG_ALWAYS_FALSE = 101;

        // Arithmetric conditions
        public static final int ARITHMETRIC_IS_NUMBER_EQUAL = 201;
        public static final int ARITHMETRIC_IS_NUMBER_IN_INTERVAL = 202;
        public static final int ARITHMETRIC_IS_NUMBER_PRIME = 203;


        // Contacts conditions
        public static final int CONTACT_IS_IN_GROUP = 301;
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
    private ConditionPlugin mConditionPlugin;
    private boolean isBuilt;


    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    /**
     * @return the list of properties for this condition
     */
    public abstract List<RuleConditionProperty> getProperties();


    /**
     * Builds the condition if it is not yet built.
     */
    public void build() {
        if (!isBuilt) {
            getConditionPlugin().initialize(getProperties());
            isBuilt = true;
        }
    }

    /**
     * Rebuilds the action
     */
    public void rebuild() {
        isBuilt = false;
        mConditionPlugin = null;
        build();
    }

    /**
     * Regenerates the action plugin
     */
    public void reGenerateConditionPlugin() {
        mConditionPlugin = null;
        getConditionPlugin();
    }

    /**
     * Evaluates the condition and recursively all of the child-conditions based on the event.
     *
     * @param event
     * @return true if the condition is true otherwise false.
     */
    public boolean evaluate(Event event) {
        // If for some reason it is not yet built then build it
        if (!isBuilt) {
            build();
        }

        // evaluate this condition
        return getConditionPlugin().evaluate(event);
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
    public ConditionPlugin getConditionPlugin() {
        if (mConditionPlugin == null) {
            // First build this condition
            mConditionPlugin = ConditionPluginFactory.create(getType());
        }
        return mConditionPlugin;
    }
}
