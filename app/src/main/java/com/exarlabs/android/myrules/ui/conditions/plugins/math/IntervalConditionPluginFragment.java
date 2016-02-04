package com.exarlabs.android.myrules.ui.conditions.plugins.math;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.exarlabs.android.myrules.business.rule.condition.plugins.math.IsNumberInIntervalConditionPlugin;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.SampleFragment;
import com.exarlabs.android.myrules.ui.conditions.ConditionPluginFragment;

import butterknife.Bind;

/**
 * Fragment which let's the user to configure an IntervalConditionPlugin
 * Created by becze on 1/21/2016.
 */
public class IntervalConditionPluginFragment extends ConditionPluginFragment {

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
    public static IntervalConditionPluginFragment newInstance() {
        return new IntervalConditionPluginFragment();
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    private View mRootView;

    @Bind(R.id.interval_start)
    public EditText mIntervalStart;

    @Bind(R.id.interval_end)
    public EditText mIntervalEnd;

    private RuleCondition mCondition;
    private IsNumberInIntervalConditionPlugin mPlugin;
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
            mRootView = inflater.inflate(R.layout.number_interval_plugin_layout, null);
        }
        return mRootView;
    }



    @Override
    protected void init(RuleCondition condition) {

        mCondition = condition;
        /*
         * Check if the condition has the right mPlugin type, and we are in edit mode
         */
        if (condition.getId() != null && condition.getConditionPlugin() instanceof IsNumberInIntervalConditionPlugin) {
            mPlugin = (IsNumberInIntervalConditionPlugin) condition.getConditionPlugin();
        }
    }

    @Override
    protected void refreshUI() {
        if(mPlugin != null) {
            mIntervalStart.setText((int) mPlugin.getMin() + "");
            mIntervalEnd.setText((int) mPlugin.getMax() + "");
        }
    }

    @Override
    protected void saveChanges() {
        // in edit mode, if the plugin is built with another type, it should be regenerate the plugin, to be able to set the values
        if(mCondition.getId() != null)
            mCondition.rebuild();

        IsNumberInIntervalConditionPlugin plugin = (IsNumberInIntervalConditionPlugin) mCondition.getConditionPlugin();
        double min = Double.parseDouble(mIntervalStart.getText().toString());
        double max = Double.parseDouble(mIntervalEnd.getText().toString());
        plugin.setMin(min);
        plugin.setMax(max);
    }

// ------------------------------------------------------------------------
// GETTERS / SETTTERS
// ------------------------------------------------------------------------
}
