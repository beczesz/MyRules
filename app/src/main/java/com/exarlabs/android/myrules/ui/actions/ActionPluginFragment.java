package com.exarlabs.android.myrules.ui.actions;

import android.os.Bundle;
import android.view.View;

import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.ui.BaseFragment;

/**
 * ActionPluginFragment is  the UI implementation of an ActionPlugin.
 * It has the responsability to
 *    - render the UI
 *    - initialize the UI with an exiting action
 *    - save the plugin data to an action
 * Created by atiyka on 1/26/2016.
 */
public abstract class ActionPluginFragment extends BaseFragment {

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
     * Initilizes the fragment with an action.
     * @param action
     */
    protected abstract void init(RuleAction action);

    /**
     * Refreshes the UI with the plugin data. This is called after onViewCreated
     */
    protected abstract void refreshUI();

    /**
     * Saves the changes into the action
     */
    protected abstract void saveChanges(String name, int actionType);

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshUI();
    }


    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
