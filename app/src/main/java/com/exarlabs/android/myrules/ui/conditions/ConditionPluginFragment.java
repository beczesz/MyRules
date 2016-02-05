package com.exarlabs.android.myrules.ui.conditions;

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
    protected abstract void init(RuleCondition condition);

    /**
     * Refreshes the UI with the plugin data. This is called after onViewCreated
     */
    protected abstract void refreshUI();


    /**
     * Saves the changes into the condition
     */
    protected abstract void saveChanges();

    @Override
    public void onResume() {
        super.onResume();
        refreshUI();
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
