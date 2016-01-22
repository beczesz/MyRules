package com.exarlabs.android.myrules.business.dagger;

import com.exarlabs.android.myrules.MyRulesApplication;
import com.exarlabs.android.myrules.business.action.plugins.RejectCallActionPlugin;
import com.exarlabs.android.myrules.business.action.plugins.SendSmsActionPlugin;
import com.exarlabs.android.myrules.business.database.DaoManager;
import com.exarlabs.android.myrules.business.RulesEngineService;
import com.exarlabs.android.myrules.business.event.plugins.sms.SmsEventHandlerPlugin;
import com.exarlabs.android.myrules.ui.BaseActivity;
import com.exarlabs.android.myrules.ui.MainActivity;
import com.exarlabs.android.myrules.ui.SampleFragment;
import com.exarlabs.android.myrules.ui.actions.ActionsAddActionFragment;
import com.exarlabs.android.myrules.ui.actions.ActionsOverviewFragment;
import com.exarlabs.android.myrules.ui.conditions.ConditionsAddConditionFragment;
import com.exarlabs.android.myrules.ui.conditions.ConditionsOverviewFragment;
import com.exarlabs.android.myrules.ui.debug.DebugOverviewFragment;
import com.exarlabs.android.myrules.ui.drawer.DrawerManager;
import com.exarlabs.android.myrules.ui.events.EventsOverviewFragment;
import com.exarlabs.android.myrules.ui.history.HistoryListFragment;
import com.exarlabs.android.myrules.ui.rules.RulesAddRuleFragment;
import com.exarlabs.android.myrules.ui.rules.RulesOverviewFragment;

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

    void inject(EventsOverviewFragment eventsOverviewFragment);

    void inject(ActionsOverviewFragment actionsOverviewFragment);

    void inject(HistoryListFragment historyListFragment);

    void inject(RulesEngineService ruleService);

    void inject(RulesAddRuleFragment rulesAddRuleFragment);

    void inject(ActionsAddActionFragment actionsAddActionFragment);

    void inject(ConditionsAddConditionFragment conditionsAddConditionFragment);

    void inject(DebugOverviewFragment debugOverviewFragment);

    void inject(SendSmsActionPlugin sendSmsActionPlugin);

    void inject(RejectCallActionPlugin rejectCallActionPlugin);

    void inject(SmsEventHandlerPlugin smsEventHandlerPlugin);
}
