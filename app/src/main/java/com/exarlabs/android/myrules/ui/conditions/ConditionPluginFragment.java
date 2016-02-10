package com.exarlabs.android.myrules.ui.conditions;

import com.exarlabs.android.myrules.business.rule.condition.ConditionPlugin;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.ui.BaseFragment;

/**
 * ConditionPluginFragment is  the UI implementation of a ConditionPlugin.
 * It has the responsability to
 *    - render the UI
 *    - initialize the UI with an exiting condition
 *    - save the plugin data to a condition
 * Created by becze on 1/21/2016.
 */
public abstract class ConditionPluginFragment extends BaseFragment {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private RuleCondition mCondition;
    private ConditionPlugin mPlugin;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    /**
     * Initilizes the fragment with a condition.
     * @param condition
     */
    protected void init(RuleCondition condition){
        mCondition = condition;
        mPlugin = condition.getConditionPlugin();

    }

    /**
     * Refreshes the UI with the plugin data. This is called after onViewCreated
     */
    protected abstract void refreshUI();


    /**
     * Saves the changes into the condition
     */
    protected boolean saveChanges(){
        // in edit mode, if the plugin is built with another type, it should be regenerate the plugin, to be able to set the values
        if (mCondition.getId() != null) {
            mPlugin = mCondition.reGenerateConditionPlugin();
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUI();
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------


    public ConditionPlugin getPlugin() {
        return mPlugin;
    }

    public RuleCondition getCondition() {
        return mCondition;
    }
}
