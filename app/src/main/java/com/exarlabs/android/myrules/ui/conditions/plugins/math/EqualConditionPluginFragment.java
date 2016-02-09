package com.exarlabs.android.myrules.ui.conditions.plugins.math;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.exarlabs.android.myrules.business.rule.condition.plugins.math.IsNumberEqualConditionPlugin;
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
public class EqualConditionPluginFragment extends ConditionPluginFragment {

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
    public static EqualConditionPluginFragment newInstance() {
        return new EqualConditionPluginFragment();
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    private View mRootView;

    @Bind(R.id.number_equal)
    public EditText mNumberEqual;

    private RuleCondition mCondition;
    private IsNumberEqualConditionPlugin mPlugin;
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
            mRootView = inflater.inflate(R.layout.number_equal_plugin_layout, null);
        }
        return mRootView;
    }



    @Override
    protected void init(RuleCondition condition) {

        mCondition = condition;
        /*
         * Check if the condition has the right mPlugin type, and we are in edit mode
         */
        if (condition.getId() != null && condition.getConditionPlugin() instanceof IsNumberEqualConditionPlugin) {
            mPlugin = (IsNumberEqualConditionPlugin) condition.getConditionPlugin();
        }
    }

    @Override
    protected void refreshUI() {
        if(mPlugin != null) {
            mNumberEqual.setText((int) mPlugin.getValue() + "");
        }
    }

    @Override
    protected boolean saveChanges() {
        // in edit mode, if the plugin is built with another type, it should be regenerate the plugin, to be able to set the values
        if(mCondition.getId() != null) {
            mPlugin = (IsNumberEqualConditionPlugin) mCondition.reGenerateConditionPlugin();
        }

        double value = Double.parseDouble(mNumberEqual.getText().toString());
        mPlugin.setValue(value);
        return true;
    }

// ------------------------------------------------------------------------
// GETTERS / SETTTERS
// ------------------------------------------------------------------------
}
