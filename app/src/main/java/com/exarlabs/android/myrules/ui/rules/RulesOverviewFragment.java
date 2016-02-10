package com.exarlabs.android.myrules.ui.rules;

import java.util.List;

import javax.inject.Inject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.exarlabs.android.myrules.business.RulesEngineService;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.devel.DevelManager;
import com.exarlabs.android.myrules.business.rule.RuleManager;
import com.exarlabs.android.myrules.business.rule.action.ActionManager;
import com.exarlabs.android.myrules.business.rule.condition.ConditionManager;
import com.exarlabs.android.myrules.business.rule.event.EventPluginManager;
import com.exarlabs.android.myrules.model.dao.RuleRecord;
import com.exarlabs.android.myrules.ui.BaseFragment;
import com.exarlabs.android.myrules.ui.BuildConfig;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;
import com.software.shell.fab.ActionButton;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Lists all the rules which are defined by the user.
 * Created by becze on 11/25/2015.
 */
public class RulesOverviewFragment extends BaseFragment implements OnRuleEditListener {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = RulesOverviewFragment.class.getSimpleName();

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    /**
     * @return newInstance of SampleFragment
     */
    public static RulesOverviewFragment newInstance() {
        return new RulesOverviewFragment();
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    @Bind (R.id.build_info)
    public TextView mDevelInfo;

    @Bind (R.id.listView_rules)
    public ListView mRulesListView;

    @Bind (R.id.fab_add_rule)
    public ActionButton mAddRuleButton;

    @Bind (R.id.lbl_rules_engine_starter_icon)
    public TextView mRulesEngineStarterIcon;

    @Bind (R.id.lbl_rules_engine_starter)
    public TextView mRulesEngineStarterLabel;

    private View mRootView;

    @Inject
    public DevelManager mDevelManager;

    @Inject
    public ActionManager mActionManager;

    @Inject
    public RuleManager mRuleManager;

    @Inject
    public ConditionManager mConditionManager;

    @Inject
    public NavigationManager mNavigationManager;

    @Inject
    public EventPluginManager mEventPluginManager;

    private RulesArrayAdapter mAdapter;
    private List<RuleRecord> mRuleRecords;
    private BroadcastReceiver mRulesEngineStatusBarChangesReceiver;
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

        IntentFilter filter = new IntentFilter();
        filter.addAction(RulesEngineService.Status.ENGINE_RUNNING);
        filter.addAction(RulesEngineService.Status.ENGINE_STOPPED);

        mRulesEngineStatusBarChangesReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                updateRuleEngineStatusBar(RulesEngineService.isRunning());
            }
        };
        getActivity().registerReceiver(mRulesEngineStatusBarChangesReceiver, filter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.rules_overview_layout, null);
        }
        return mRootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initActionBarWithHomeButton(getString(R.string.my_rules));

        mAdapter = new RulesArrayAdapter(getContext());
        mAdapter.setOnRuleEditListener(this);
        mRulesListView.setAdapter(mAdapter);


        if (BuildConfig.DEBUG) {
            mDevelInfo.setText(mDevelManager.getBuildDescription());
            mDevelInfo.setVisibility(View.VISIBLE);
        }

        mAddRuleButton.playShowAnimation();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mRulesEngineStatusBarChangesReceiver);
    }

    /**
     * Update the list of rules
     */
    private void updateUI() {

        // Update the status bar
        updateRuleEngineStatusBar(RulesEngineService.isRunning());

        mAdapter.clear();
        mRuleRecords = mRuleManager.loadAllRules();
        mAdapter.addAll(mRuleRecords);
        mAdapter.notifyDataSetChanged();
    }


    @OnClick ({ R.id.lbl_rules_engine_starter, R.id.lbl_rules_engine_starter_icon })
    public void starStopRulesEngine() {
        if (!RulesEngineService.isRunning()) {
            getActivity().startService(new Intent(getActivity(), RulesEngineService.class));
        } else {
            getActivity().stopService(new Intent(getActivity(), RulesEngineService.class));
        }
    }

    private void updateRuleEngineStatusBar(boolean isRunning) {
        // Update the icons
        mRulesEngineStarterLabel.setText(isRunning ? R.string.lbl_stop_rules_engine : R.string.lbl_start_rules_engine);

        int color = isRunning ? R.color.red : R.color.green;
        mRulesEngineStarterIcon.setTextColor(getResources().getColor(color));
        mRulesEngineStarterIcon.setText(isRunning ? R.string.lbl_stop_rules_engine_icon : R.string.lbl_start_rules_engine_icon);
    }

    @OnClick (R.id.fab_add_rule)
    public void showRuleDetailsFragment() {
        showRuleDetailsFragment((long) -1);
    }

    private void showRuleDetailsFragment(long id) {
        mNavigationManager.startRuleDetailsFragment(id);
    }

    @Override
    public void onRuleEdit(Long ruleId) {
        showRuleDetailsFragment(ruleId);
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
