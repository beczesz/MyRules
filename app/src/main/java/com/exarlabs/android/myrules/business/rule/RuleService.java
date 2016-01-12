package com.exarlabs.android.myrules.business.rule;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import android.util.Pair;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.event.Event;
import com.exarlabs.android.myrules.business.event.EventHandlerPlugin;
import com.exarlabs.android.myrules.business.event.RuleEventManager;
import com.exarlabs.android.myrules.business.event.plugins.debug.NumberEventHandlerPlugin;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.model.dao.RuleConditionTree;
import com.exarlabs.android.myrules.model.dao.RuleRecord;

import rx.Observable;
import rx.Observer;

/**
 * Service class which runs in background waiting for rule events and handling them.
 * Created by becze on 1/11/2016.
 */
public class RuleService {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    @Inject
    public RuleManager mRuleManager;

    private RuleEventManager mRuleEventManager;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public RuleService() {
        DaggerManager.component().inject(this);
    }


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    private void init() {

        //@formatter:off
        mRuleEventManager = new RuleEventManager(getEventPlugins());
        mRuleEventManager.getEventObservable()
                        .map(event -> new Pair<>(event, mRuleManager.getRules(event.getType(), RuleState.STATE_ACTIVE)))
                        .flatMap(pair -> Observable.from(pair.second).map(record -> new Pair<>(pair.first, (RuleRecord) record)))
                        .filter(eventRulePair -> {
                            RuleConditionTree ruleConditionTree = eventRulePair.second.getRuleConditionTree();
                            return ruleConditionTree != null ? ruleConditionTree.evaluate(eventRulePair.first) : false;
                        })
                        .subscribe(getRuleResolveSubscriber());
        //@formatter:on
    }

    private Observer<? super Pair<Event, RuleRecord>> getRuleResolveSubscriber() {
        return new Observer<Pair<Event, RuleRecord>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Pair<Event, RuleRecord> eventRulePair) {
                executeRule(eventRulePair.first, eventRulePair.second.getRuleActions());
            }
        };
    }

    /**
     * Exectutes the rules actions
     *
     * @param actions
     */
    private void executeRule(Event event, List<RuleAction> actions) {
        // TODO make it sequential
        for (RuleAction ruleAction : actions) {
            ruleAction.run(event);
        }
    }

    private List<EventHandlerPlugin> getEventPlugins() {
        List<EventHandlerPlugin> plugins = new ArrayList<>();
        plugins.add(new NumberEventHandlerPlugin());
        return plugins;
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
