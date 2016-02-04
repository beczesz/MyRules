package com.exarlabs.android.myrules.ui.conditions.plugins.time;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.exarlabs.android.myrules.business.rule.condition.plugins.math.IsNumberInIntervalConditionPlugin;
import com.exarlabs.android.myrules.business.rule.condition.plugins.time.IsTimeInIntervalConditionPlugin;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.conditions.ConditionPluginFragment;
import com.exarlabs.android.myrules.util.TimeUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Fragment which let's the user to configure an IntervalTimeConditionPlugin
 * Created by becze on 1/21/2016.
 */
public class IntervalTimeConditionPluginFragment extends ConditionPluginFragment {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = IntervalTimeConditionPluginFragment.class.getSimpleName();

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    /**
     * @return newInstance of IntervalTimeConditionPluginFragment
     */
    public static IntervalTimeConditionPluginFragment newInstance() {
        return new IntervalTimeConditionPluginFragment();
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    private View mRootView;

//    private int minTime;
//    private int maxTime;

    @Bind(R.id.min_time)
    public EditText mIntervalTimeStart;

    @Bind(R.id.max_time)
    public EditText mIntervalTimeEnd;

    @Bind(R.id.is_outside_time)
    public CheckBox mIsOutside;

    private RuleCondition mCondition;
    private IsTimeInIntervalConditionPlugin mPlugin;
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
            mRootView = inflater.inflate(R.layout.time_interval_plugin_layout, null);
            ButterKnife.bind(this, mRootView);
        }
        mIntervalTimeStart.setOnClickListener(v -> {
            int min = TimeUtil.stringToMinutes(mIntervalTimeStart.getText().toString());
            if (min == -1) min = 0;
            new TimePickerDialog(getActivity(), (view, hourOfDay, minute) -> {
                mIntervalTimeStart.setText(TimeUtil.hourMinuteToString(hourOfDay, minute));
            }, min / 60, min % 60, false).show();
        });
        mIntervalTimeEnd.setOnClickListener(v -> {
            int max = TimeUtil.stringToMinutes(mIntervalTimeEnd.getText().toString());
            if (max == -1) max = 0;
            new TimePickerDialog(getActivity(), (view, hourOfDay, minute) -> {
                mIntervalTimeEnd.setText(TimeUtil.hourMinuteToString(hourOfDay, minute));
            }, max / 60, max % 60, false).show();
        });
        return mRootView;
    }


    @Override
    protected void init(RuleCondition condition) {

        mCondition = condition;
        /*
         * Check if the condition has the right mPlugin type, and we are in edit mode
         */
        if (condition.getId() != null && condition.getConditionPlugin() instanceof IsTimeInIntervalConditionPlugin) {
            mPlugin = (IsTimeInIntervalConditionPlugin) condition.getConditionPlugin();
        }
    }

    @Override
    protected void refreshUI() {
        if (mPlugin != null) {
            mIntervalTimeStart.setText(TimeUtil.minutesToString((int) mPlugin.getMin()));
            mIntervalTimeEnd.setText(TimeUtil.minutesToString((int) mPlugin.getMax()));
            mIsOutside.setChecked(mPlugin.isOutside());
        }
    }

    @Override
    protected void saveChanges() {
        // in edit mode, if the plugin is built with another type, it should be regenerate the plugin, to be able to set the values
        if (mCondition.getId() != null) mCondition.rebuild();

        IsNumberInIntervalConditionPlugin plugin = (IsNumberInIntervalConditionPlugin) mCondition.getConditionPlugin();
        double min = TimeUtil.stringToMinutes(mIntervalTimeStart.getText().toString());
        double max = TimeUtil.stringToMinutes(mIntervalTimeEnd.getText().toString());
        boolean isOut = mIsOutside.isChecked();
        plugin.setMin(min);
        plugin.setMax(max);
        plugin.setOutside(isOut);
    }

//

// ------------------------------------------------------------------------
// GETTERS / SETTTERS
// ------------------------------------------------------------------------
}
