package com.exarlabs.android.myrules.business.dagger;

import com.exarlabs.android.myrules.MyRulesApplication;
import com.exarlabs.android.myrules.business.RulesEngineService;
import com.exarlabs.android.myrules.business.database.DaoManager;
import com.exarlabs.android.myrules.business.rule.action.Action;
import com.exarlabs.android.myrules.business.rule.action.ActionPlugin;
import com.exarlabs.android.myrules.business.rule.action.plugins.call.RejectCallActionPlugin;
import com.exarlabs.android.myrules.business.rule.action.plugins.sms.SendSmsActionPlugin;
import com.exarlabs.android.myrules.business.rule.condition.Condition;
import com.exarlabs.android.myrules.business.rule.condition.ConditionPlugin;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.business.rule.event.EventHandlerPlugin;
import com.exarlabs.android.myrules.business.rule.event.plugins.call.CallEventHandlerPlugin;
import com.exarlabs.android.myrules.business.rule.event.plugins.sms.SmsEventHandlerPlugin;
import com.exarlabs.android.myrules.ui.BaseActivity;
import com.exarlabs.android.myrules.ui.MainActivity;
import com.exarlabs.android.myrules.ui.SampleFragment;
import com.exarlabs.android.myrules.ui.actions.ActionCardsFragment;
import com.exarlabs.android.myrules.ui.actions.ActionDetailsFragment;
import com.exarlabs.android.myrules.ui.actions.ActionsOverviewFragment;
import com.exarlabs.android.myrules.ui.actions.ActionsSectorFragment;
import com.exarlabs.android.myrules.ui.actions.plugins.contact.SendSMSToGroupActionPluginFragment;
import com.exarlabs.android.myrules.ui.actions.plugins.debug.DefaultActionPluginFragment;
import com.exarlabs.android.myrules.ui.actions.plugins.math.MultiplyActionPluginFragment;
import com.exarlabs.android.myrules.ui.conditions.ConditionDetailsFragment;
import com.exarlabs.android.myrules.ui.conditions.ConditionTreeFragment;
import com.exarlabs.android.myrules.ui.conditions.ConditionsArrayAdapter;
import com.exarlabs.android.myrules.ui.conditions.ConditionsOverviewFragment;
import com.exarlabs.android.myrules.ui.conditions.ConditionsSectorFragment;
import com.exarlabs.android.myrules.ui.conditions.plugins.DefaultConditionPluginFragment;
import com.exarlabs.android.myrules.ui.conditions.plugins.contact.ContactIsInGroupConditionPluginFragment;
import com.exarlabs.android.myrules.ui.conditions.plugins.math.EqualConditionPluginFragment;
import com.exarlabs.android.myrules.ui.conditions.plugins.math.IntervalConditionPluginFragment;
import com.exarlabs.android.myrules.ui.debug.DebugOverviewFragment;
import com.exarlabs.android.myrules.ui.drawer.DrawerManager;
import com.exarlabs.android.myrules.ui.history.HistoryListFragment;
import com.exarlabs.android.myrules.ui.rules.RuleDetailsFragment;
import com.exarlabs.android.myrules.ui.rules.RulesArrayAdapter;
import com.exarlabs.android.myrules.ui.rules.RulesOverviewFragment;
import com.exarlabs.android.myrules.ui.util.contact.ContactsSectorFragment;

/**
 * Here are listed all the loations where injection is needed.
 * Created by becze on 9/17/2015.
 */
public interface DaggerComponentGraph {


    void inject(MyRulesApplication app);

    void inject(BaseActivity baseActivity);

    void inject(SampleFragment sampleFragment);

    void inject(RulesOverviewFragment rulesOverviewFragment);

    void inject(MainActivity baseActivity);

    void inject(DaoManager daoManager);

    void inject(DrawerManager drawerManager);

    void inject(ConditionsOverviewFragment conditionsOverviewFragment);

    void inject(ActionsOverviewFragment actionsOverviewFragment);

    void inject(HistoryListFragment historyListFragment);

    void inject(RulesEngineService ruleService);

    void inject(DebugOverviewFragment debugOverviewFragment);

    void inject(SendSmsActionPlugin sendSmsActionPlugin);

    void inject(RejectCallActionPlugin rejectCallActionPlugin);

    void inject(SmsEventHandlerPlugin smsEventHandlerPlugin);
    
    void inject(CallEventHandlerPlugin callEventHandlerPlugin);

    void inject(RuleDetailsFragment ruleDetailsFragment);

    void inject(ConditionTreeFragment conditionTreeFragment);

    void inject(ActionCardsFragment actionCardsFragment);

    void inject(ActionDetailsFragment actionDetailsFragment);

    void inject(DefaultActionPluginFragment defaultActionPluginFragment);

    void inject(MultiplyActionPluginFragment multiplyActionPluginFragment);

    void inject(ContactIsInGroupConditionPluginFragment contactIsInGroupConditionPluginFragment);

    void inject(DefaultConditionPluginFragment defaultConditionPluginFragment);

    void inject(EqualConditionPluginFragment equalConditionPluginFragment);

    void inject(IntervalConditionPluginFragment intervalConditionPluginFragment);

    void inject(SendSMSToGroupActionPluginFragment sendSMSToGroupActionPluginFragment);

    void inject(ContactsSectorFragment contactsSectorFragment);

    void inject(ConditionDetailsFragment conditionDetailsFragment);

    void inject(ConditionsSectorFragment conditionsSectorFragment);
    
    void inject(Event event);

    void inject(EventHandlerPlugin eventHandlerPlugin);
    
    void inject(ActionsSectorFragment actionsSectorFragment);

    void inject(ConditionPlugin conditionPlugin);

    void inject(Condition condition);

    void inject(ConditionsArrayAdapter conditionsArrayAdapter);

    void inject(Action action);

    void inject(ActionPlugin actionPlugin);

    void inject(RulesArrayAdapter rulesArrayAdapter);
}
