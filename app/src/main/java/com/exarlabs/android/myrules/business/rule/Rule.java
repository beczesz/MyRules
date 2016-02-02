package com.exarlabs.android.myrules.business.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.support.annotation.NonNull;

import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.model.GreenDaoEntity;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.model.dao.RuleActionLink;
import com.exarlabs.android.myrules.model.dao.RuleConditionTree;
import com.exarlabs.android.myrules.model.dao.RuleRecord;

/**
 * A rule is a logical structure which is defined by three components.
 * <ul compact>
 * <li> 1. Events: Different event which can occur in one time like battery level change, network connection, incoming SMS etc.
 * <li> 2. Conditions: Evaluable components which can determine if the rule accomplishes some conditions.
 * <li> 3. ActionsL Runnable components, tasks which should be carried out when a rule evaluates to true for a given event.
 * </ul>
 *
 * @see Event Event
 * @see com.exarlabs.android.myrules.business.rule.condition.Condition Condition
 * @see com.exarlabs.android.myrules.business.rule.action.Action Action
 * <p>
 * Created by becze on 1/11/2016.
 */
public abstract class Rule implements GreenDaoEntity, Evaluable, Buildable, Runnable {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    /**
     * Possible states of rules
     * Created by becze on 1/11/2016.
     */
    public static class RuleState {

        // ------------------------------------------------------------------------
        // STATIC FIELDS
        // ------------------------------------------------------------------------

        public static final int STATE_ACTIVE = 1000;
        public static final int STATE_INACTIVE = 1100;
        public static final int STATE_DELETED = 1200;
    }

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    protected boolean isBuilt = false;

    // temporary storage for detached rules
    private List<RuleAction> mTemRuleActions;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    protected abstract String getRuleName();

    protected abstract RuleConditionTree getRuleConditionTree();

    protected abstract List<RuleActionLink> getRuleActionLinks();


    @Override
    public boolean evaluate(Event event) {
        RuleConditionTree ruleConditionTree = getRuleConditionTree();
        return ruleConditionTree != null ? ruleConditionTree.evaluate(event) : false;
    }

    /**
     * Builds the actions and conditions for this rule
     */
    public void build() {

        // Get the rule condition
        RuleConditionTree ruleConditionTree = getRuleConditionTree();

        // Get the actions
        List<RuleAction> ruleActions = getRuleActions();

        // Build the conditions and actions
        if (!isBuilt) {
            if (ruleConditionTree != null) {
                ruleConditionTree.rebuild();
            }

            for (RuleAction action : ruleActions) {
                action.rebuild();
            }

            isBuilt = true;
        }
    }

    @Override
    public void rebuild() {
        isBuilt = false;
        build();
    }


    @Override
    public boolean run(Event event) {
        /*
         * Run sequentially all the actions.
         */
        List<RuleAction> ruleActions = getRuleActions();
        if (ruleActions != null) {
            for (RuleAction ruleAction : ruleActions) {
                ruleAction.run(event);
            }

            return true;
        }

        return false;
    }

    /**
     * @return the RuleActions of this Rule. If there are nor RuleActions then an empty list.
     */
    @NonNull
    public List<RuleAction> getRuleActions() {
        List<RuleAction> ruleActions = new ArrayList<>();
        if (getRuleActionLinks() != null) {
            for (RuleActionLink ruleActionLink : getRuleActionLinks()) {
                if (ruleActionLink.getRuleAction() != null) {
                    ruleActions.add(ruleActionLink.getRuleAction());
                }
            }
        }
        return ruleActions;
    }

    /**
     * Appends the rules creatign new RuleActionLinks
     *
     * @param newActions
     */
    public void addRuleActions(List<RuleAction> newActions) {

        if (!isAttached()) {
            if (mTemRuleActions == null) {
                mTemRuleActions = new ArrayList<>();
            }
            mTemRuleActions.addAll(newActions);
        } else {
            List<RuleActionLink> ruleActionLinks = getRuleActionLinks();

            for (RuleAction action : newActions) {
                RuleActionLink ruleActionLink = new RuleActionLink();
                ruleActionLink.setRuleAction(action);
                ruleActionLink.setRuleRecord((RuleRecord) this);
                ruleActionLinks.add(ruleActionLink);
            }
        }
    }

    public void addRuleActions(RuleAction... actions) {
        addRuleActions(Arrays.asList(actions));
    }

    /**
     * If the entty is attached and there are any temporary rule actions left, then we add them as well.
     */
    public void addTempraryRuleActions() {
        if (mTemRuleActions != null && isAttached()) {
            addRuleActions(mTemRuleActions);
            mTemRuleActions = null;
        }

    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + getRuleName() + ")";
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------


    public boolean isBuilt() {
        return isBuilt;
    }

}
