package com.exarlabs.android.myrules.ui.debug;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.exarlabs.android.myrules.business.action.Action;
import com.exarlabs.android.myrules.business.action.ActionManager;
import com.exarlabs.android.myrules.business.action.plugins.MultiplyActionPlugin;
import com.exarlabs.android.myrules.business.action.plugins.SendSmsActionPlugin;
import com.exarlabs.android.myrules.business.condition.Condition;
import com.exarlabs.android.myrules.business.condition.ConditionManager;
import com.exarlabs.android.myrules.business.condition.ConditionTree;
import com.exarlabs.android.myrules.business.condition.plugins.IsNumberEqualConditionPlugin;
import com.exarlabs.android.myrules.business.condition.plugins.IsNumberInIntervalConditionPlugin;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.devel.DevelManager;
import com.exarlabs.android.myrules.business.event.EventHandlerPlugin;
import com.exarlabs.android.myrules.business.event.EventPluginManager;
import com.exarlabs.android.myrules.business.event.plugins.math.NumberEvent;
import com.exarlabs.android.myrules.business.event.plugins.math.NumberEventHandlerPlugin;
import com.exarlabs.android.myrules.business.rule.RuleManager;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.model.dao.RuleConditionTree;
import com.exarlabs.android.myrules.model.dao.RuleRecord;
import com.exarlabs.android.myrules.ui.BaseFragment;
import com.exarlabs.android.myrules.ui.BuildConfig;
import com.exarlabs.android.myrules.ui.R;

import butterknife.Bind;

/**
 *
 * Created by atiyka on 1/19/2016.
 */
public class DebugOverviewFragment extends BaseFragment implements OnTriggerEventListener{

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = DebugOverviewFragment.class.getSimpleName();

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    /**
     * @return newInstance of DebugOverviewFragment
     */
    public static DebugOverviewFragment newInstance() {
        return new DebugOverviewFragment();
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    @Bind(R.id.build_info)
    public TextView mDevelInfo;

    @Bind(R.id.list_view_event_plugins)
    public ListView mEventPlugins;

    @Inject
    public DevelManager mDevelManager;

    @Inject
    public EventPluginManager mEventPluginManager;

    @Inject
    public RuleManager mRuleManager;

    @Inject
    public ActionManager mActionManager;

    @Inject
    public ConditionManager mConditionManager;

    private View mRootView;
    private EventsArrayAdapter mAdapter;
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
            mRootView = inflater.inflate(R.layout.debug_overview_layout, null);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initActionBar(true, getString(R.string.debug));

        if (BuildConfig.DEBUG) {
            mDevelInfo.setText(mDevelManager.getBuildDescription());
            mDevelInfo.setVisibility(View.VISIBLE);
        }

        mAdapter = new EventsArrayAdapter(getContext());
        mAdapter.setOnTriggerEventListener(this);
        mEventPlugins.setAdapter(mAdapter);

        updateUI();
    }

    /**
     * Update the list of events
     */
    private void updateUI() {
        mAdapter.clear();
        Collection<EventHandlerPlugin> plugins = mEventPluginManager.getPlugins();

        mAdapter.addAllPlugins(plugins);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void triggerEvent(EventHandlerPlugin event) {
        if(event.getClass().equals(NumberEventHandlerPlugin.class)){
            dispatchNumberEvent();
        }
    }

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

        // create dependencies between conditions
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

        RuleAction aSms = generateNewAction(Action.Type.SEND_SMS_ACTION);
        ((SendSmsActionPlugin) aSms.getActionPlugin()).setPhoneNumber("0740507135");
        ((SendSmsActionPlugin) aSms.getActionPlugin()).setMessage("From the plugin :-)");


        mActionManager.saveActions(aFib, aMultiply, aSms);

        // Create a rule with these actions and conditions
        RuleRecord ruleRecord = new RuleRecord();

        // set the event
        ruleRecord.setRuleName("Sample Rule");
        ruleRecord.setEventCode(event.getType());
        ruleRecord.setRuleConditionTree(root);
        ruleRecord.addRuleActions(aFib, aMultiply, aSms);
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
