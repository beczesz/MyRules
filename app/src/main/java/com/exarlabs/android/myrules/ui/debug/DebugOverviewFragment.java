package com.exarlabs.android.myrules.ui.debug;

import java.util.ArrayList;
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

import com.exarlabs.android.myrules.business.rule.condition.ConditionPluginManager;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.devel.DevelManager;
import com.exarlabs.android.myrules.business.rule.Rule;
import com.exarlabs.android.myrules.business.rule.RuleManager;
import com.exarlabs.android.myrules.business.rule.action.Action;
import com.exarlabs.android.myrules.business.rule.action.ActionManager;
import com.exarlabs.android.myrules.business.rule.action.ActionPluginManager;
import com.exarlabs.android.myrules.business.rule.action.plugins.math.MultiplyActionPlugin;
import com.exarlabs.android.myrules.business.rule.action.plugins.sms.SendSmsActionPlugin;
import com.exarlabs.android.myrules.business.rule.condition.Condition;
import com.exarlabs.android.myrules.business.rule.condition.ConditionManager;
import com.exarlabs.android.myrules.business.rule.condition.ConditionTree;
import com.exarlabs.android.myrules.business.rule.condition.plugins.math.IsNumberEqualConditionPlugin;
import com.exarlabs.android.myrules.business.rule.condition.plugins.math.IsNumberInIntervalConditionPlugin;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.business.rule.event.EventPluginManager;
import com.exarlabs.android.myrules.business.rule.event.plugins.call.CallEventHandlerPlugin;
import com.exarlabs.android.myrules.business.rule.event.plugins.math.NumberEvent;
import com.exarlabs.android.myrules.business.rule.event.plugins.math.NumberEventHandlerPlugin;
import com.exarlabs.android.myrules.model.contact.Contact;
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
    @Bind (R.id.build_info)
    TextView mDevelInfo;

    @Bind (R.id.list_view_event_plugins)
    ListView mEventPlugins;

    @Inject
    DevelManager mDevelManager;

    @Inject
    EventPluginManager mEventPluginManager;

    @Inject
    RuleManager mRuleManager;

    @Inject
    ActionManager mActionManager;

    @Inject
    ConditionManager mConditionManager;

    @Inject
    ConditionPluginManager mConditionPluginManager;

    @Inject
    ActionPluginManager mActionPluginManager;

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

        initActionBarWithHomeButton(getString(R.string.debug));

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
        mAdapter.addAllPlugins(Event.Type.values());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void triggerEvent(Event.Type event) {

        switch (event) {
            case RULE_EVENT_NUMBER:
                dispatchNumberEvent();
                break;

            case RULE_EVENT_SMS:
                testSmsEventRule();
                break;

            case RULE_EVENT_CALL:
                dispatchCallEvent();
                break;
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
        RuleCondition cTrue = mConditionManager.getDefaultCondition();
        mConditionManager.saveCondition(cTrue);

        // create dependencies between conditions
        // Build the condition tree
        RuleConditionTree.Builder builder = new ConditionTree.Builder();
        builder.add(cTrue, new RuleCondition[] { cTrue, cTrue }, ConditionTree.Operator.OR);
        RuleConditionTree root = mConditionManager.buildConditionTree(builder);

        /*
         * Create actions
         */

        RuleAction aRejectCall = generateNewAction(Action.Type.REJECT_CALL_ACTION.getType());

        RuleAction aSms = generateNewAction(Action.Type.SEND_SMS_ACTION.getType());
        ((SendSmsActionPlugin) aSms.getActionPlugin()).setMessage("I'll call you back later.");

        mActionManager.saveActions(aRejectCall, aSms);

        // Create a rule with these actions and conditions
        RuleRecord ruleRecord = new RuleRecord();

        // set the event
        ruleRecord.setRuleName("Reject call Rule");
        ruleRecord.setEventCode(Event.Type.RULE_EVENT_CALL.getType());
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
        RuleCondition cTrue = mConditionManager.getDefaultCondition();
        mConditionManager.saveCondition(cTrue);

        // create dependencies between conditions
        // Build the condition tree
        RuleConditionTree.Builder builder = new ConditionTree.Builder();
        builder.add(cTrue, new RuleCondition[] { cTrue, cTrue }, ConditionTree.Operator.OR);
        RuleConditionTree root = mConditionManager.buildConditionTree(builder);

        /*
         * Create actions
         */

        RuleAction aSms = generateNewAction(Action.Type.SEND_SMS_ACTION.getType());
        // ToDo: when the Trigger button was pushed more than one time, the fields in SendSmsActionPlugin will be null. ?
        //        ((SendSmsActionPlugin) aSms.getActionPlugin()).setPhoneNumber("0740507135");
        //        ((SendSmsActionPlugin) aSms.getActionPlugin()).setMessage("From the plugin :-)");

        mActionManager.saveActions(aSms);

        // Create a rule with these actions and conditions
        RuleRecord ruleRecord = new RuleRecord();

        // set the event
        ruleRecord.setRuleName("Respond to Sms Rule");
        ruleRecord.setEventCode(Event.Type.RULE_EVENT_SMS.getType());
        ruleRecord.setRuleConditionTree(root);
        ruleRecord.addRuleActions(aSms);
        mRuleManager.saveRuleRecord(ruleRecord);
    }


    public void dispatchNumberEvent() {
        testSimpleArithmetricRule();
        NumberEventHandlerPlugin eventHandlerPlugin = (NumberEventHandlerPlugin) mEventPluginManager.getPluginInstance(Event.Type.RULE_EVENT_NUMBER);
        eventHandlerPlugin.dispatchNumber(7);
    }

    public void dispatchCallEvent() {
        testSimpleArithmetricRule();
        CallEventHandlerPlugin eventHandlerPlugin = (CallEventHandlerPlugin) mEventPluginManager.getPluginInstance(Event.Type.RULE_EVENT_CALL);
        eventHandlerPlugin.onIncomingCall(new Contact(-1, "Attila", "0740507135"));
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
        RuleCondition cTrue = mConditionManager.getDefaultCondition();
        RuleCondition cTrue1 = mConditionManager.getDefaultCondition();

        RuleCondition cInterval = generateNewCondition(Condition.Type.ARITHMETRIC_IS_NUMBER_IN_INTERVAL.getType());
        ((IsNumberInIntervalConditionPlugin) cInterval.getConditionPlugin()).setMin(5.0);
        ((IsNumberInIntervalConditionPlugin) cInterval.getConditionPlugin()).setMax(500.0);

        RuleCondition cPrime = generateNewCondition(Condition.Type.ARITHMETRIC_IS_NUMBER_PRIME.getType());

        RuleCondition cEqual = generateNewCondition(Condition.Type.ARITHMETRIC_IS_NUMBER_EQUAL.getType());
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

        RuleAction aMultiply = generateNewAction(Action.Type.ARITHMETRIC_ACTION_MULTIPLY.getType());
        ((MultiplyActionPlugin) aMultiply.getActionPlugin()).setValue(5);

        RuleAction aFib = generateNewAction(Action.Type.ARITHMETRIC_ACTION_FIBONACCI.getType());

        RuleAction aMultiply10 = generateNewAction(Action.Type.ARITHMETRIC_ACTION_MULTIPLY.getType());
        ((MultiplyActionPlugin) aMultiply10.getActionPlugin()).setValue(10);

        mActionManager.saveActions(aMultiply, aFib, aMultiply10);

        // Create a rule with these actions and conditions
        RuleRecord ruleRecord = new RuleRecord();

        // set the event
        ruleRecord.setRuleName("Sample Rule");
        ruleRecord.setEventCode(event.getType());
        ruleRecord.setState(Rule.State.STATE_ACTIVE);
        ruleRecord.setRuleConditionTree(root);
        ruleRecord.addRuleActions(aMultiply, aFib, aMultiply10);
        mRuleManager.saveRuleRecord(ruleRecord);
    }


    private RuleCondition generateNewCondition(int type) {
        RuleCondition c = new RuleCondition();
        c.setType(type);
        c.setConditionName(getResources().getString(mConditionPluginManager.getFromConditionTypeCode(type).getTitleResId()));
        return c;
    }

    private RuleAction generateNewAction(int type) {
        RuleAction ruleAction = new RuleAction();
        ruleAction.setType(type);
        ruleAction.setActionName(getResources().getString(mActionPluginManager.getFromActionTypeCode(type).getTitleResId()));
        return ruleAction;
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
