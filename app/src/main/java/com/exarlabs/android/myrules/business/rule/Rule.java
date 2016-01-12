package com.exarlabs.android.myrules.business.rule;

import java.util.ArrayList;
import java.util.List;

import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.model.dao.RuleActionLink;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.model.dao.RuleConditionLink;

/**
 * Base class for all the rules
 * Created by becze on 1/11/2016.
 */
public abstract class Rule {

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
    protected boolean isBuilt = false;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    protected abstract RuleConditionLink getRuleConditionLink();

    protected abstract List<RuleActionLink> getRuleActionLinks();

    /**
     * Builds the actions and conditions for this rule
     */
    public void build() {

        // Get the rule condition
        RuleCondition ruleCondition = getRuleConditionLink() != null ? getRuleConditionLink().getRuleCondition() : null;
        List<RuleAction> ruleActions = new ArrayList<>();
        if (getRuleActionLinks() != null) {
            for (RuleActionLink ruleActionLink : getRuleActionLinks()) {
                if (ruleActionLink.getRuleAction() != null) {
                    ruleActions.add(ruleActionLink.getRuleAction());
                }
            }
        }

        // Build the conditions and actions
        if (!isBuilt) {
            if (ruleCondition != null) {
                ruleCondition.build();
            }

            for (RuleAction action : ruleActions) {
                action.build();
            }

            setIsBuilt(true);
        }
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
