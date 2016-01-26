package com.exarlabs.android.myrules.ui.actions.plugins;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.exarlabs.android.myrules.business.action.ActionManager;
import com.exarlabs.android.myrules.business.action.plugins.MultiplyActionPlugin;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.SampleFragment;
import com.exarlabs.android.myrules.ui.actions.ActionPluginFragment;

import butterknife.Bind;

/**
 * Fragment which let's the user to configure a multiplier
 *
 * Created by atiyka on 1/26/2016.
 */
public class MultiplyActionPluginFragment extends ActionPluginFragment {

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
    public static MultiplyActionPluginFragment newInstance() {
        return new MultiplyActionPluginFragment();
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    @Bind(R.id.number_multiply)
    public EditText mNumber;

    private View mRootView;

    private RuleAction mAction;
    private MultiplyActionPlugin mMultiplyActionPlugin;

    @Inject
    public ActionManager mActionManager;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------
    public MultiplyActionPluginFragment(){
        DaggerManager.component().inject(this);
    }

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
            mRootView = inflater.inflate(R.layout.action_multiply_plugin_layout, null);
        }
        return mRootView;
    }

    @Override
    protected void init(RuleAction action) {
        mAction = action;
        /*
         * Check if the action has the right plugin type
         */
        if (action.getActionPlugin() instanceof MultiplyActionPlugin) {
            mMultiplyActionPlugin = (MultiplyActionPlugin) action.getActionPlugin();
        }

    }

    @Override
    protected void refreshUI() {
        if(mMultiplyActionPlugin != null)
            mNumber.setText((int) mMultiplyActionPlugin.getValue() + "");
    }

    @Override
    protected void saveChanges(String name, int actionType) {
        // updates if it is an existing action
        if (mAction != null && mAction.getActionPlugin() instanceof MultiplyActionPlugin) {
            mAction.setActionName(name);
            mAction.setType(actionType);

            double value = Double.parseDouble(mNumber.getText().toString());
            ((MultiplyActionPlugin) mAction.getActionPlugin()).setValue(value);
            mActionManager.saveAction(mAction);

            mAction.rebuild();
        }else{
            // else creates a new one
            // TODO: delete the existing if it is another type of action
            mAction = new RuleAction();
            mAction.setActionName(name);
            mAction.setType(actionType);

            double value = Double.parseDouble(mNumber.getText().toString());
            ((MultiplyActionPlugin) mAction.getActionPlugin()).setValue(value);

            mActionManager.saveAction(mAction);
        }
    }

// ------------------------------------------------------------------------
// GETTERS / SETTTERS
// ------------------------------------------------------------------------
}
