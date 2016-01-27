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
import com.exarlabs.android.myrules.business.action.ActionPluginFactory;
import com.exarlabs.android.myrules.business.action.plugins.MultiplyActionPlugin;
import com.exarlabs.android.myrules.business.action.plugins.SendSmsActionPlugin;
import com.exarlabs.android.myrules.business.condition.Condition;
import com.exarlabs.android.myrules.business.condition.ConditionManager;
import com.exarlabs.android.myrules.business.condition.ConditionPluginFactory;
import com.exarlabs.android.myrules.business.condition.ConditionTree;
import com.exarlabs.android.myrules.business.condition.plugins.IsNumberEqualConditionPlugin;
import com.exarlabs.android.myrules.business.condition.plugins.IsNumberInIntervalConditionPlugin;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.devel.DevelManager;
import com.exarlabs.android.myrules.business.event.Event;
import com.exarlabs.android.myrules.business.event.EventHandlerPlugin;
import com.exarlabs.android.myrules.business.event.EventPluginManager;
import com.exarlabs.android.myrules.business.event.plugins.call.CallEventHandlerPlugin;
import com.exarlabs.android.myrules.business.event.plugins.math.NumberEvent;
import com.exarlabs.android.myrules.business.event.plugins.math.NumberEventHandlerPlugin;
import com.exarlabs.android.myrules.business.event.plugins.sms.SmsEventHandlerPlugin;
import com.exarlabs.android.myrules.business.rule.Rule;
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
 * Created by atiyka on 1/19/2016.
 */
public class DebugOverviewFragment extends BaseFragment implements OnTriggerEventListener {

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
    }

    @Override
    public void onResume() {
        super.onResume();
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
        if (event.getClass().equals(NumberEventHandlerPlugin.class)) {
            dispatchNumberEvent();
        } else if (event.getClass().equals(SmsEventHandlerPlugin.class)) {
            testSmsEventRule();
        } else if (event.getClass().equals(CallEventHandlerPlugin.class)) {
            dispatchCallEvent();
        }
    }

    /**
     * we create a rule which listens for the incoming calls, rejects it, and sends back an sms
     */
    public void testCallEventRule() {
        if (mRuleManager.loadAllRules().size() != 0) {
            return;
        }

         /*
         * Create the conditions
         */
        // ToDo: to be handled an event without conditions and without building the condition tree
        RuleCondition cTrue = generateNewCondition(Condition.Type.DEBUG_ALWAYS_TRUE);
        mConditionManager.saveCondition(cTrue);

        // create dependencies between conditions
        // Build the condition tree
        RuleConditionTree.Builder builder = new ConditionTree.Builder();
        builder.add(cTrue, new RuleCondition[] { cTrue, cTrue }, ConditionTree.Operator.OR);
        RuleConditionTree root = mConditionManager.buildConditionTree(builder);

        /*
         * Create actions
         */

        RuleAction aRejectCall = generateNewAction(Action.Type.REJECT_CALL_ACTION);

        RuleAction aSms = generateNewAction(Action.Type.SEND_SMS_ACTION);
        ((SendSmsActionPlugin) aSms.getActionPlugin()).setMessage("I'll call you back later.");

        mActionManager.saveActions(aRejectCall, aSms);

        // Create a rule with these actions and conditions
        RuleRecord ruleRecord = new RuleRecord();

        // set the event
        ruleRecord.setRuleName("Reject call Rule");
        ruleRecord.setEventCode(Event.Type.RULE_EVENT_CALL);
        ruleRecord.setRuleConditionTree(root);
        ruleRecord.addRuleActions(aRejectCall, aSms);
        mRuleManager.saveRuleRecord(ruleRecord);

    }

    /**
     * creates a rule, which listens for the received SMSes and send back a response
     */
    public void testSmsEventRule() {
        if (mRuleManager.loadAllRules().size() != 0) {
            return;
        }

         /*
         * Create the conditions
         */
        // ToDo: to be handled an event without conditions and without building the condition tree
        RuleCondition cTrue = generateNewCondition(Condition.Type.DEBUG_ALWAYS_TRUE);
        mConditionManager.saveCondition(cTrue);

        // create dependencies between conditions
        // Build the condition tree
        RuleConditionTree.Builder builder = new ConditionTree.Builder();
        builder.add(cTrue, new RuleCondition[] { cTrue, cTrue }, ConditionTree.Operator.OR);
        RuleConditionTree root = mConditionManager.buildConditionTree(builder);

        /*
         * Create actions
         */

        RuleAction aSms = generateNewAction(Action.Type.SEND_SMS_ACTION);
        // ToDo: when the Trigger button was pushed more than one time, the fields in SendSmsActionPlugin will be null. ?
//        ((SendSmsActionPlugin) aSms.getActionPlugin()).setPhoneNumber("0740507135");
//        ((SendSmsActionPlugin) aSms.getActionPlugin()).setMessage("From the plugin :-)");

        mActionManager.saveActions(aSms);

        // Create a rule with these actions and conditions
        RuleRecord ruleRecord = new RuleRecord();

        // set the event
        ruleRecord.setRuleName("Respond to Sms Rule");
        ruleRecord.setEventCode(Event.Type.RULE_EVENT_SMS);
        ruleRecord.setRuleConditionTree(root);
        ruleRecord.addRuleActions(aSms);
        mRuleManager.saveRuleRecord(ruleRecord);
    }


    public void dispatchNumberEvent() {
        testSimpleArithmetricRule();
        NumberEventHandlerPlugin eventHandlerPlugin = (NumberEventHandlerPlugin) mEventPluginManager.get(NumberEventHandlerPlugin.class);
        eventHandlerPlugin.dispatchNumber(7);
    }

    public void dispatchCallEvent() {
        testSimpleArithmetricRule();
        CallEventHandlerPlugin eventHandlerPlugin = (CallEventHandlerPlugin) mEventPluginManager.get(CallEventHandlerPlugin.class);
        eventHandlerPlugin.getCall("Attila");
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

        RuleAction aMultiply10 = generateNewAction(Action.Type.ARITHMETRIC_ACTION_MULTIPLY);
        ((MultiplyActionPlugin) aMultiply10.getActionPlugin()).setValue(10);

        mActionManager.saveActions(aMultiply, aFib, aMultiply10);

        // Create a rule with these actions and conditions
        RuleRecord ruleRecord = new RuleRecord();

        // set the event
        ruleRecord.setRuleName("Sample Rule");
        ruleRecord.setEventCode(event.getType());
        ruleRecord.setState(Rule.RuleState.STATE_ACTIVE);
        ruleRecord.setRuleConditionTree(root);
        ruleRecord.addRuleActions(aMultiply, aFib, aMultiply10);
        mRuleManager.saveRuleRecord(ruleRecord);
    }


    private RuleCondition generateNewCondition(int type) {
        RuleCondition c = new RuleCondition();
        c.setType(type);
        c.setConditionName(ConditionPluginFactory.create(type).getClass().getSimpleName());
        return c;
    }

    private RuleAction generateNewAction(int type) {
        RuleAction ruleAction = new RuleAction();
        ruleAction.setType(type);
        ruleAction.setActionName(ActionPluginFactory.create(type).getClass().getSimpleName());
        return ruleAction;
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
