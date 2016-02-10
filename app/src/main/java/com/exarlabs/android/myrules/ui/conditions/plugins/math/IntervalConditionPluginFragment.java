package com.exarlabs.android.myrules.ui.conditions.plugins.math;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.exarlabs.android.myrules.business.rule.condition.ConditionPlugin;
import com.exarlabs.android.myrules.business.rule.condition.plugins.math.IsNumberInIntervalConditionPlugin;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.ui.R;
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

    private static final String TAG = IntervalConditionPluginFragment.class.getSimpleName();

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

    @Bind (R.id.interval_start)
    public EditText mIntervalStart;

    @Bind (R.id.interval_end)
    public EditText mIntervalEnd;

    @Bind (R.id.is_outside)
    public CheckBox mIsOutside;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        super.init(condition);
    }

    @Override
    protected void refreshUI() {
        ConditionPlugin plugin = getPlugin();
        if (plugin != null && plugin instanceof IsNumberInIntervalConditionPlugin) {
            IsNumberInIntervalConditionPlugin intervalPlugin = (IsNumberInIntervalConditionPlugin) plugin;

            if (intervalPlugin.getMin() != intervalPlugin.getMax()) {
                mIntervalStart.setText(intervalPlugin.getMin() + "");
                mIntervalEnd.setText(intervalPlugin.getMax() + "");
                mIsOutside.setChecked(intervalPlugin.isOutside());
            }
        }
    }

    @Override
    protected boolean saveChanges() {
        super.saveChanges();

        if (!validateInput()) {
            return false;
        }
        IsNumberInIntervalConditionPlugin plugin = (IsNumberInIntervalConditionPlugin) getPlugin();
        double min = Double.parseDouble(mIntervalStart.getText().toString());
        double max = Double.parseDouble(mIntervalEnd.getText().toString());
        boolean isOut = mIsOutside.isChecked();

        plugin.setMin(min);
        plugin.setMax(max);
        plugin.setOutside(isOut);
        return true;
    }

    /**
     * Checks if the input values are corrects
     *
     * @return true if everything is ok.
     */
    private boolean validateInput() {
        double min = Double.parseDouble(mIntervalStart.getText().toString());
        double max = Double.parseDouble(mIntervalEnd.getText().toString());

        if (min == max) {
            mIntervalEnd.setError(getString(R.string.msg_err_not_an_interval));
            return false;
        }
        if(min > max){
            mIntervalEnd.setError(getString(R.string.msg_err_bad_interval));
            return false;
        }

        return true;
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
