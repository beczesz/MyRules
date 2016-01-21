package com.exarlabs.android.myrules.ui.conditions;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.exarlabs.android.myrules.business.condition.ConditionManager;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.ui.BaseFragment;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Displays the detials of a condition
 * Created by becze on 1/21/2016.
 */
public class ConditionDetailsFragment extends BaseFragment {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String KEY_CONDITION_ID = "CONDITION_ID";
    private static final String KEY_CONDITION_TYPE = "CONDITION_TYPE";

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    public static ConditionDetailsFragment newInstance(long conditionID, int conditionType) {
        Bundle args = new Bundle();

        args.putInt(KEY_CONDITION_TYPE, conditionType);
        if (conditionID != -1) {
            args.putLong(KEY_CONDITION_ID, conditionID);
        }

        ConditionDetailsFragment fragment = new ConditionDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ConditionDetailsFragment newInstance(int conditionType) {
        return newInstance(-1, conditionType);
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    private View mRootView;

    @Bind(R.id.condition_name)
    public EditText mConditionName;

    @Bind(R.id.condition_plugin_fragment_container)
    public FrameLayout mConditionPluginFragmentContainer;

    @Inject
    public ConditionManager mConditionManager;

    @Inject
    public NavigationManager mNavigationManager;

    private RuleCondition mRuleCondition;
    private ConditionPluginFragment mConditionPluginFragment;
    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public ConditionDetailsFragment() {
    }


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerManager.component().inject(this);

        // get out the condition id or type
        long conditionId = getArguments().containsKey(KEY_CONDITION_ID) ? (long) getArguments().get(KEY_CONDITION_ID) : -1;
        int conditionType = getArguments().containsKey(KEY_CONDITION_TYPE) ? (int) getArguments().get(KEY_CONDITION_TYPE) : -1;

        // Get the condition if we have a valid id
        if (conditionId != -1) {
            mRuleCondition = mConditionManager.loadCondition(conditionId);
            mRuleCondition.build();
        }

        if (mRuleCondition == null) {
            mRuleCondition = new RuleCondition();
        }

        // Load the plugin fragment based on the condition type
        mConditionPluginFragment = ConditionPluginFragmentFactory.create(conditionType);
        mConditionPluginFragment.init(mRuleCondition);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.condition_details_layout, null);
        }
        return mRootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // add the fragment to the container.
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.condition_plugin_fragment_container, mConditionPluginFragment).commit();

        // Initialize with the name of the condition
        mConditionName.setText(mRuleCondition.getConditionName());
    }

    @OnClick(R.id.button_save)
    public void save() {
        mConditionPluginFragment.saveChanges();
        mRuleCondition.setConditionName(mConditionName.getText().toString());
        mConditionManager.saveCondition(mRuleCondition);
        mNavigationManager.navigateBack(getActivity());
    }

    @OnClick(R.id.button_cancel)
    public void cancel() {
        mNavigationManager.navigateBack(getActivity());
    }




    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
