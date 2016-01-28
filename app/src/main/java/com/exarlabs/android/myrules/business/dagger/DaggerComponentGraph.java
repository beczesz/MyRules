package com.exarlabs.android.myrules.business.dagger;

import com.exarlabs.android.myrules.MyRulesApplication;
import com.exarlabs.android.myrules.business.RulesEngineService;
import com.exarlabs.android.myrules.business.action.ActionCardsFragment;
import com.exarlabs.android.myrules.business.action.plugins.RejectCallActionPlugin;
import com.exarlabs.android.myrules.business.action.plugins.SendSmsActionPlugin;
import com.exarlabs.android.myrules.business.database.DaoManager;
import com.exarlabs.android.myrules.business.event.plugins.call.CallEventHandlerPlugin;
import com.exarlabs.android.myrules.business.event.plugins.sms.SmsEventHandlerPlugin;
import com.exarlabs.android.myrules.ui.BaseActivity;
import com.exarlabs.android.myrules.ui.MainActivity;
import com.exarlabs.android.myrules.ui.SampleFragment;
import com.exarlabs.android.myrules.ui.actions.ActionDetailsFragment;
import com.exarlabs.android.myrules.ui.actions.ActionsOverviewFragment;
import com.exarlabs.android.myrules.ui.actions.plugins.DefaultActionPluginFragment;
import com.exarlabs.android.myrules.ui.actions.plugins.MultiplyActionPluginFragment;
import com.exarlabs.android.myrules.ui.actions.plugins.contact.SendSMSToGroupActionPlugin;
import com.exarlabs.android.myrules.ui.conditions.ConditionDetailsFragment;
import com.exarlabs.android.myrules.ui.conditions.ConditionTreeFragment;
import com.exarlabs.android.myrules.ui.conditions.ConditionsOverviewFragment;
import com.exarlabs.android.myrules.ui.conditions.plugins.DefaultConditionPluginFragment;
import com.exarlabs.android.myrules.ui.conditions.plugins.math.EqualConditionPluginFragment;
import com.exarlabs.android.myrules.ui.conditions.plugins.math.IntervalConditionPluginFragment;
import com.exarlabs.android.myrules.ui.conditions.plugins.contact.ContactIsInGroupConditionPluginFragment;
import com.exarlabs.android.myrules.ui.debug.DebugOverviewFragment;
import com.exarlabs.android.myrules.ui.drawer.DrawerManager;
import com.exarlabs.android.myrules.ui.history.HistoryListFragment;
import com.exarlabs.android.myrules.ui.rules.RuleDetailsFragment;
import com.exarlabs.android.myrules.ui.rules.RulesAddRuleFragment;
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

    void inject(RulesAddRuleFragment rulesAddRuleFragment);

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

    void inject(SendSMSToGroupActionPlugin sendSMSToGroupActionPlugin);

    void inject(ContactsSectorFragment contactsSectorFragment);

    void inject(ConditionDetailsFragment conditionDetailsFragment);
}
