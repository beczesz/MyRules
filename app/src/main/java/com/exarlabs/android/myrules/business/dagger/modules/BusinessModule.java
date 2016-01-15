package com.exarlabs.android.myrules.business.dagger.modules;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.exarlabs.android.myrules.business.action.ActionManager;
import com.exarlabs.android.myrules.business.condition.ConditionManager;
import com.exarlabs.android.myrules.business.database.DaoManager;
import com.exarlabs.android.myrules.business.event.EventPluginManager;
import com.exarlabs.android.myrules.business.event.RuleEventManager;
import com.exarlabs.android.myrules.business.rule.RuleManager;

import dagger.Module;
import dagger.Provides;

/**
 * Provides business logic related modules
 * Created by becze on 12/15/2015.
 */
@Module
public class BusinessModule {


    @Provides
    @Singleton
    protected EventPluginManager providesEventPluginManager() {
        return new EventPluginManager();
    }

    @Inject
    @Provides
    @Singleton
    protected RuleEventManager providesRuleEventManager(EventPluginManager eventPluginManager) {
        return new RuleEventManager(eventPluginManager);
    }

    @Inject
    @Provides
    @Singleton
    protected ConditionManager providesConditionManager(DaoManager daoManager) {
        return new ConditionManager(daoManager);
    }


    @Inject
    @Provides
    @Singleton
    protected ActionManager providesActionManager(DaoManager daoManager) {
        return new ActionManager(daoManager);
    }

    @Inject
    @Provides
    @Singleton
    protected RuleManager providesRuleManager(DaoManager daoManager, ConditionManager conditionManager, ActionManager actionManager) {
        return new RuleManager(daoManager, conditionManager, actionManager);
    }





}
