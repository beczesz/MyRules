package com.exarlabs.android.myrules.business.rule.condition;

import java.util.List;

import javax.inject.Inject;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.Buildable;
import com.exarlabs.android.myrules.business.rule.Evaluable;
import com.exarlabs.android.myrules.business.rule.RuleComponent;
import com.exarlabs.android.myrules.business.rule.condition.plugins.AlwaysFalseConditionPlugin;
import com.exarlabs.android.myrules.business.rule.condition.plugins.AlwaysTrueConditionPlugin;
import com.exarlabs.android.myrules.business.rule.condition.plugins.contact.ContactIsInGroupConditionPlugin;
import com.exarlabs.android.myrules.business.rule.condition.plugins.math.IsNumberEqualConditionPlugin;
import com.exarlabs.android.myrules.business.rule.condition.plugins.math.IsNumberInIntervalConditionPlugin;
import com.exarlabs.android.myrules.business.rule.condition.plugins.math.IsNumberPrimeConditionPlugin;
import com.exarlabs.android.myrules.business.rule.condition.plugins.time.IsTimeInIntervalConditionPlugin;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.model.GreenDaoEntity;
import com.exarlabs.android.myrules.model.dao.RuleConditionProperty;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.conditions.ConditionPluginFragment;
import com.exarlabs.android.myrules.ui.conditions.plugins.DefaultConditionPluginFragment;
import com.exarlabs.android.myrules.ui.conditions.plugins.contact.ContactIsInGroupConditionPluginFragment;
import com.exarlabs.android.myrules.ui.conditions.plugins.math.EqualConditionPluginFragment;
import com.exarlabs.android.myrules.ui.conditions.plugins.math.IntervalConditionPluginFragment;
import com.exarlabs.android.myrules.ui.conditions.plugins.time.IntervalTimeConditionPluginFragment;

/**
 * It is a rule condition abstraction which with a bridge pattern it decouples the
 * condition implementation from the type.
 * <p>
 * The concrete condition implementation will specify the type of the condition and it's properties.
 * </p>
 * Created by becze on 12/18/2015.
 */
public abstract class Condition implements GreenDaoEntity, RuleComponent, Evaluable, Buildable {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    /**
     * Encapsulates the Condition Types.
     */
    public enum Type {

        // ------------------------------------------------------------------------
        // DEBUG CONDITIONS
        // ------------------------------------------------------------------------
        CONNECTOR_CONDITION_PLUGIN(1, AlwaysTrueConditionPlugin.class, DefaultConditionPluginFragment.class,
                        R.string.condition_title_always_true_condition),

        DEBUG_ALWAYS_TRUE(1000, AlwaysTrueConditionPlugin.class, DefaultConditionPluginFragment.class,
                        R.string.condition_title_always_true_condition),
        DEBUG_ALWAYS_FALSE(1001, AlwaysFalseConditionPlugin.class, DefaultConditionPluginFragment.class,
                        R.string.condition_title_always_false_condition),

        // ------------------------------------------------------------------------
        // ARITHMETRIC CONDITIONS
        // ------------------------------------------------------------------------

        ARITHMETRIC_IS_NUMBER_EQUAL(2000, IsNumberEqualConditionPlugin.class, EqualConditionPluginFragment.class,
                        R.string.condition_title_is_number_equal_condition),
        ARITHMETRIC_IS_NUMBER_IN_INTERVAL(2001, IsNumberInIntervalConditionPlugin.class, IntervalConditionPluginFragment.class,
                        R.string.condition_title_is_number_in_interval_condition),
        ARITHMETRIC_IS_NUMBER_PRIME(2002, IsNumberPrimeConditionPlugin.class, DefaultConditionPluginFragment.class,
                        R.string.condition_title_is_number_prime_condition),


        // ------------------------------------------------------------------------
        // CONTACT CONDITIONS
        // ------------------------------------------------------------------------
        CONTACT_IS_IN_GROUP(3000, ContactIsInGroupConditionPlugin.class, ContactIsInGroupConditionPluginFragment.class,
                        R.string.condition_title_contact_is_in_group_condition),

        // ------------------------------------------------------------------------
        // TIME CONDITIONS
        // ------------------------------------------------------------------------
        TIME_IS_IN_INTERVAL(4000, IsTimeInIntervalConditionPlugin.class, IntervalTimeConditionPluginFragment.class,
                        R.string.condition_title_is_time_in_interval_condition);


        private final int mType;
        private final Class<? extends ConditionPlugin> mPlugin;
        private final Class<? extends ConditionPluginFragment> mPluginFragment;
        private final int mTitleResId;

        Type(int type, Class<? extends ConditionPlugin> plugin, Class<? extends ConditionPluginFragment> pluginFragment, int titleResId) {
            mType = type;
            mPlugin = plugin;
            mPluginFragment = pluginFragment;
            mTitleResId = titleResId;
        }

        public int getType() {
            return mType;
        }

        public Class<? extends ConditionPlugin> getPlugin() {
            return mPlugin;
        }

        public int getTitleResId() {
            return mTitleResId;
        }

        public Class<? extends ConditionPluginFragment> getPluginFragment() {
            return mPluginFragment;
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
    private ConditionPlugin mConditionPlugin;
    private boolean isBuilt;

    @Inject
    ConditionPluginManager mConditionPluginManager;


    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public Condition() {
        DaggerManager.component().inject(this);
    }


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
            ConditionPlugin conditionPlugin = getConditionPlugin();
            if (isAttached()) {
                conditionPlugin.initialize(getProperties());
                isBuilt = true;
            }
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
            mConditionPlugin = mConditionPluginManager.createNewPluginInstance(getType());
        }
        return mConditionPlugin;
    }
}
