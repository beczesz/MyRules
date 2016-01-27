package com.exarlabs.android.myrules.ui.actions.plugins;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exarlabs.android.myrules.business.action.ActionManager;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.SampleFragment;
import com.exarlabs.android.myrules.ui.actions.ActionPluginFragment;

/**
 * Fragment which let's the user to configure an IntervalConditionPlugin
 * Created by becze on 1/21/2016.
 */
public class DefaultActionPluginFragment extends ActionPluginFragment {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = SampleFragment.class.getSimpleName();

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    /**
     * @return newInstance of IntervalConditionPluginFragment
     */
    public static DefaultActionPluginFragment newInstance() {
        return new DefaultActionPluginFragment();
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    private View mRootView;

    private RuleAction mAction;

    @Inject
    public ActionManager mActionManager;
    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerManager.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.default_action_plugin_layout, null);
        }
        return mRootView;
    }

    @Override
    protected void init(RuleAction action) {
        mAction = action;
    }

    @Override
    protected void refreshUI() {

    }

    /**
     * Saves the changed data when the Save button was pressed
     */
    @Override
    protected void saveChanges() {
        if(mAction.getId() != null) {
            mAction.reGenerateActionPlugin();
        }
    }

// ------------------------------------------------------------------------
// GETTERS / SETTTERS
// ------------------------------------------------------------------------
}
