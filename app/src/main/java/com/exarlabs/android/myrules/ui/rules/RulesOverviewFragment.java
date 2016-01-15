package com.exarlabs.android.myrules.ui.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.exarlabs.android.myrules.business.action.Action;
import com.exarlabs.android.myrules.business.action.ActionManager;
import com.exarlabs.android.myrules.business.action.plugins.MultiplyActionPlugin;
import com.exarlabs.android.myrules.business.condition.Condition;
import com.exarlabs.android.myrules.business.condition.ConditionManager;
import com.exarlabs.android.myrules.business.condition.ConditionTree;
import com.exarlabs.android.myrules.business.condition.plugins.IsNumberEqualConditionPlugin;
import com.exarlabs.android.myrules.business.condition.plugins.IsNumberInIntervalConditionPlugin;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.devel.DevelManager;
import com.exarlabs.android.myrules.business.event.EventPluginManager;
import com.exarlabs.android.myrules.business.event.plugins.math.NumberEvent;
import com.exarlabs.android.myrules.business.event.plugins.math.NumberEventHandlerPlugin;
import com.exarlabs.android.myrules.business.rule.RuleManager;
import com.exarlabs.android.myrules.business.rule.RulesEngineService;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.model.dao.RuleConditionTree;
import com.exarlabs.android.myrules.model.dao.RuleRecord;
import com.exarlabs.android.myrules.ui.BaseFragment;
import com.exarlabs.android.myrules.ui.BuildConfig;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;
import com.software.shell.fab.ActionButton;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;

/**
 * Lists all the rules which are defined by the user.
 * Created by becze on 11/25/2015.
 */
public class RulesOverviewFragment extends BaseFragment {

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
    @Bind(R.id.build_info)
    public TextView mDevelInfo;

    @Bind(R.id.listView_rules)
    public ListView mRulesListView;

    @Bind(R.id.fab_add_rule)
    public ActionButton mAddRuleButton;

    @Bind(R.id.lbl_rules_engine_starter_icon)
    public TextView mRulesEngineStarterIcon;

    @Bind(R.id.lbl_rules_engine_starter)
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

    private ArrayAdapter<String> mAdapter;
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
            mRootView = inflater.inflate(R.layout.rules_overview_layout, null);
        }
        return mRootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initActionBar(true, getString(R.string.my_rules));

        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, new ArrayList<>());
        mRulesListView.setAdapter(mAdapter);

        if (BuildConfig.DEBUG) {
            mDevelInfo.setText(mDevelManager.getBuildDescription());
            mDevelInfo.setVisibility(View.VISIBLE);
        }

        mAddRuleButton.playShowAnimation();
        updateUI();


    }

    /**
     * Update the list of rules
     */
    private void updateUI() {

        // Updatet the statusbar
        updateRuleEngineStatusBar(RulesEngineService.isRunning());

        mAdapter.clear();
        List<RuleRecord> rules = mRuleManager.loadAllRules();
        List<String> ruleNames = new ArrayList<>();
        Observable.from(rules).map(rule -> rule.getRuleName()).subscribe(ruleName -> ruleNames.add(ruleName));

        mAdapter.addAll(ruleNames);
        mAdapter.notifyDataSetChanged();
    }


    @OnClick(R.id.fab_add_rule)
    public void showAddRuleFragment() {
        mNavigationManager.startAddRuleFragment();
    }

    @OnClick({ R.id.lbl_rules_engine_starter, R.id.lbl_rules_engine_starter_icon })
    public void starStopRulesEngine() {
        if (!RulesEngineService.isRunning()) {
            getActivity().startService(new Intent(getActivity(), RulesEngineService.class));
        } else {
            getActivity().stopService(new Intent(getActivity(), RulesEngineService.class));
        }

        updateRuleEngineStatusBar(!RulesEngineService.isRunning());
    }

    private void updateRuleEngineStatusBar(boolean isRunning) {
        // Update the icons
        mRulesEngineStarterLabel.setText(isRunning ? R.string.lbl_stop_rules_engine : R.string.lbl_start_rules_engine);

        int color = isRunning ? R.color.red : R.color.green;
        mRulesEngineStarterIcon.setTextColor(getResources().getColor(color));
        mRulesEngineStarterIcon.setText(isRunning ? R.string.lbl_stop_rules_engine_icon : R.string.lbl_start_rules_engine_icon);
    }

    @OnClick(R.id.temp_number_event_displatcher)
    public void dispatchNumberEvent() {
        testSimpleArithmetricRule();
        NumberEventHandlerPlugin eventHandlerPlugin = (NumberEventHandlerPlugin) mEventPluginManager.get(NumberEventHandlerPlugin.class);
        eventHandlerPlugin.dispatchNumber(7);
    }

    /**
     * We create a rule which responds to Number events and with some conditions it calculates fibonacci and multiplications.
     */
    public void testSimpleArithmetricRule() {

        if (mRuleManager.loadAllRules().size() != 0) {
            return;
        }

        // Create the event
        NumberEvent event = new NumberEvent();


        /*
         * Create the conditions
         */
        RuleCondition cTrue = generateNewCondition(Condition.Type.DEBUG_ALWAYS_TRUE);
        RuleCondition cTrue1 = generateNewCondition(Condition.Type.DEBUG_ALWAYS_TRUE);

        RuleCondition cInterval = generateNewCondition(Condition.Type.ARITHMETRIC_IS_NUMBER_IN_INTERVAL);
        ((IsNumberInIntervalConditionPlugin) cInterval.getConditionPlugin()).setMin(5);
        ((IsNumberInIntervalConditionPlugin) cInterval.getConditionPlugin()).setMax(500);

        RuleCondition cPrime = generateNewCondition(Condition.Type.ARITHMETRIC_IS_NUMBER_PRIME);

        RuleCondition cEqual = generateNewCondition(Condition.Type.ARITHMETRIC_IS_NUMBER_EQUAL);
        ((IsNumberEqualConditionPlugin) cEqual.getConditionPlugin()).setValue(1);

        List<RuleCondition> ruleConditions = new ArrayList<>();
        Collections.addAll(ruleConditions, cTrue, cTrue1, cInterval, cPrime, cEqual);
        mConditionManager.saveConditions(ruleConditions);

        // create dependencies beween conditions
        // Build the condition tree
        RuleConditionTree.Builder builder = new ConditionTree.Builder();
        builder.add(cTrue, new RuleCondition[] { cEqual, cTrue1 }, ConditionTree.Operator.OR);
        builder.add(cTrue1, new RuleCondition[] { cInterval, cPrime }, ConditionTree.Operator.AND);
        RuleConditionTree root = mConditionManager.buildConditionTree(builder);

        /*
         * Create actions
         */

        RuleAction aMultiply = generateNewAction(Action.Type.ARITHMETRIC_ACTION_MULTIPLY);
        ((MultiplyActionPlugin) aMultiply.getActionPlugin()).setValue(5);

        RuleAction aFib = generateNewAction(Action.Type.ARITHMETRIC_ACTION_FIBONACCI);
        mActionManager.saveActions(aFib, aMultiply);

        // Create a rule with these actions and conditions
        RuleRecord ruleRecord = new RuleRecord();

        // set the event
        ruleRecord.setEventCode(event.getType());
        ruleRecord.setRuleConditionTree(root);
        ruleRecord.addRuleActions(aFib, aMultiply);
        mRuleManager.saveRuleRecord(ruleRecord);
    }


    private RuleCondition generateNewCondition(int type) {
        RuleCondition c = new RuleCondition();
        c.setType(type);
        return c;
    }

    private RuleAction generateNewAction(int type) {
        RuleAction ruleAction = new RuleAction();
        ruleAction.setType(type);
        return ruleAction;
    }


    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
