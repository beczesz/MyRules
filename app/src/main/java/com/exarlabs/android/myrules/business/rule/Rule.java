package com.exarlabs.android.myrules.business.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.support.annotation.NonNull;

import com.exarlabs.android.myrules.model.GreenDaoEntity;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.model.dao.RuleActionLink;
import com.exarlabs.android.myrules.model.dao.RuleConditionTree;
import com.exarlabs.android.myrules.model.dao.RuleRecord;

/**
 * Base class for all the rules
 * Created by becze on 1/11/2016.
 */
public abstract class Rule implements GreenDaoEntity {

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
                ruleConditionTree.build();
            }

            for (RuleAction action : ruleActions) {
                action.build();
            }

            setIsBuilt(true);
        }
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

    public void setIsBuilt(boolean isBuilt) {
        this.isBuilt = isBuilt;
    }



}
