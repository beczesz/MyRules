package com.exarlabs.android.myrules.ui.actions.plugins.math;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.action.ActionPlugin;
import com.exarlabs.android.myrules.business.rule.action.plugins.math.MultiplyActionPlugin;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.SampleFragment;
import com.exarlabs.android.myrules.ui.actions.ActionPluginFragment;

import butterknife.Bind;

/**
 * Fragment which let's the user to configure a multiplier
 * <p>
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

    @Bind (R.id.number_multiply)
    public EditText mNumber;

    private View mRootView;

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
            mRootView = inflater.inflate(R.layout.action_multiply_plugin_layout, null);
        }
        return mRootView;
    }

    @Override
    protected void init(RuleAction action) {
        super.init(action);
    }

    @Override
    protected void refreshUI() {
        ActionPlugin plugin = getPlugin();
        if (plugin != null && plugin instanceof MultiplyActionPlugin) {
            MultiplyActionPlugin multiplyPlugin = (MultiplyActionPlugin) plugin;
            mNumber.setText((int) multiplyPlugin.getValue() + "");
        }
    }

    /**
     * Saves the changed data when the Save button was pressed
     */
    @Override
    protected boolean saveChanges() {
        super.saveChanges();

        double value = Double.parseDouble(mNumber.getText().toString());
        ((MultiplyActionPlugin) getPlugin()).setValue(value);

        return true;
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
