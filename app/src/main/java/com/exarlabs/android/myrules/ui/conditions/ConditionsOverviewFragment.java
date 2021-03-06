package com.exarlabs.android.myrules.ui.conditions;

import java.util.List;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.devel.DevelManager;
import com.exarlabs.android.myrules.business.rule.condition.ConditionManager;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.ui.BaseFragment;
import com.exarlabs.android.myrules.ui.BuildConfig;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;
import com.software.shell.fab.ActionButton;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Provides an overview to the user of all the conditions.
 * Created by becze on 11/25/2015.
 */
public class ConditionsOverviewFragment extends BaseFragment implements OnConditionEditListener {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = ConditionsOverviewFragment.class.getSimpleName();

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    /**
     * @return newInstance of SampleFragment
     */
    public static ConditionsOverviewFragment newInstance() {
        return new ConditionsOverviewFragment();
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    @Bind(R.id.build_info)
    public TextView mDevelInfo;

    @Bind(R.id.listView_conditions)
    public ListView mConditionsListView;

    @Bind(R.id.fab_add_condition)
    public ActionButton mAddConditionButton;

    private View mRootView;

    @Inject
    public DevelManager mDevelManager;

    @Inject
    public ConditionManager mConditionManager;

    @Inject
    public NavigationManager mNavigationManager;

    private ConditionsArrayAdapter mAdapter;

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
            mRootView = inflater.inflate(R.layout.conditions_overview_layout, null);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initActionBarWithHomeButton(getString(R.string.my_conditions));

        mAdapter = new ConditionsArrayAdapter(getContext());
        mAdapter.setOnConditionEditListener(this);

        mConditionsListView.setAdapter(mAdapter);

        if (BuildConfig.DEBUG) {
            mDevelInfo.setText(mDevelManager.getBuildDescription());
            mDevelInfo.setVisibility(View.VISIBLE);
        }

        mAddConditionButton.playShowAnimation();
        updateUI();
    }

    /**
     * Update the list of rules
     */
    private void updateUI() {
        mAdapter.clear();
        List<RuleCondition> ruleConditions = mConditionManager.loadAllNonConnectorConditions();
        mAdapter.addAll(ruleConditions);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConditionEdit(Long conditionId) {
        showConditionDetailsFragment(conditionId);
    }

    @OnClick(R.id.fab_add_condition)
    public void showConditionDetailsFragment(){
        showConditionDetailsFragment((long) -1);
    }

    public void showConditionDetailsFragment(long id){
        mNavigationManager.startConditionsDetails(id);
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
