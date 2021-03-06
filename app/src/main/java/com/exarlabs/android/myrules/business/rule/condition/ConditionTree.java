package com.exarlabs.android.myrules.business.rule.condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

import com.exarlabs.android.myrules.business.rule.Buildable;
import com.exarlabs.android.myrules.business.rule.Evaluable;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.model.GreenDaoEntity;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.model.dao.RuleConditionTree;

/**
 * It is a rule condition abstraction which with a bridge pattern it decouples the
 * condition implementation from the type.
 * <p>
 * The concrete condition implementation will specify the type of the condition and it's properties.
 * </p>
 * Created by becze on 12/18/2015.
 */
public abstract class ConditionTree implements GreenDaoEntity, Evaluable, Buildable{

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    /**
     * Condition tree builder.
     * Note: all the RuleConditions must be attached when you add a new one.
     * TODO: change the builder such that no default condition generation should be necessary
     */
    public static class Builder {

        private RuleConditionTree mRoot;

        public Builder() {
        }


        /**
         * Adds a condition tree with a root and it's children applying the operator between the children.
         *
         * @param subTreeRoot
         * @param children
         * @param operator
         * @return
         */
        public Builder add(RuleCondition subTreeRoot, List<RuleCondition> children, int operator) {

            // Either subTreeRoot or children should be specified.
            if (subTreeRoot == null && children == null) {
                return this;
            }
            if (mRoot == null) {
                mRoot = new RuleConditionTree();
                mRoot.setRuleCondition(subTreeRoot);
            }
            // find the subTreeRoot in our tree
            RuleConditionTree subTree = getRuleConditionTree(mRoot, subTreeRoot);
            if (subTree != null) {
                subTree.setOperator(operator);
                buildTempChildren(subTree, children);
            } else {
                Log.w(TAG, "Subtree not found for: " + subTreeRoot);
            }

            return this;
        }

        public Builder add(RuleCondition subTreeRoot, RuleCondition[] children, int operator) {
            return add(subTreeRoot, Arrays.asList(children), operator);
        }

        /**
         * It creates a new list of RuleConditionTree out of the list of children RuleCondition
         * and sets it as a tempChildren
         *
         * @param subTree
         * @param children
         */
        private void buildTempChildren(RuleConditionTree subTree, List<RuleCondition> children) {
            List<RuleConditionTree> childrenCondtionTrees = new ArrayList<>();
            for (RuleCondition child : children) {
                RuleConditionTree childTree = new RuleConditionTree();
                childTree.setRuleCondition(child);
                childrenCondtionTrees.add(childTree);
            }

            subTree.setTempChildConditions(childrenCondtionTrees);
        }

        /**
         * Searches in the tree with the given root for a condition.
         *
         * @param currentRoot
         * @param subTreeRoot
         * @return
         */
        private RuleConditionTree getRuleConditionTree(RuleConditionTree currentRoot, RuleCondition subTreeRoot) {

            if (currentRoot.getRuleCondition() == subTreeRoot) {
                return currentRoot;
            } else {
                List<RuleConditionTree> childConditions = currentRoot.getTempChildConditions();
                if (childConditions != null) {
                    for (RuleConditionTree childConditionTree : childConditions) {
                        RuleConditionTree ruleContionTree = getRuleConditionTree(childConditionTree, subTreeRoot);
                        if (ruleContionTree != null) {
                            return ruleContionTree;
                        }
                    }
                }
            }

            return null;
        }

        /**
         * @return the root of this tree.
         */
        public RuleConditionTree build() {
            return mRoot;
        }
    }

    /**
     * The tyepe of the logical operator
     */
    public static class Operator {
        public static final int AND = 0;
        public static final int OR = 1;

    }


    /**
     * Encapsulates the Condition Types.
     */
    public static class State {
        // Debug condition types
        public static final int ACTIVE = 100;
        public static final int DELETED = 101;
    }

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = ConditionTree.class.getSimpleName();


    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private boolean isBuilt;

    /**
     * This is a temporary field used for building the condition trees, when they are not yet attached.
     * Green Dao can only retrieve child conditions if the parent is attached so, when we are building a new
     * condition tree, we have to use some temporary storage.
     */
    private List<RuleConditionTree> mTempChildConditions;


    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    /**
     * @return the list of child conditions.
     */
    public abstract List<? extends ConditionTree> getChildConditions();


    /**
     * @return the logical operator type which holds for the children
     */
    public abstract int getOperator();

    public abstract Condition getRuleCondition();

    /**
     * Builds the condition if it is not yet built.
     * Note: you can only build an attched condition
     */
    public void build() {
        if (!isBuilt) {
            // Build the condition where this tree points to
            getRuleCondition().rebuild();

            List<? extends ConditionTree> conditionTrees = getChildConditions();
            if (conditionTrees != null) {
                for (ConditionTree condition : conditionTrees) {
                    condition.rebuild();
                }
            }

            isBuilt = true;
        }
    }

    @Override
    public void rebuild() {
        isBuilt = false;
        build();
    }

    /**
     * Evaluates the condition and recursively all of the child-conditions based on the event.
     *
     * @param event
     * @return true if the condition is true otherwise false.
     */
    public boolean evaluate(Event event) {
        // If for some reason it is not yet built then build it
        if (!isBuilt) {
            build();
        }

        // evaluate this condition
        // The initial value is based on the type of the operator
        boolean isConditionHold = false;
        switch (getOperator()) {
            case Operator.AND:
                isConditionHold = true;
                break;

            case Operator.OR:
                isConditionHold = false;
                break;
        }

        int operator = getOperator();

        // Evaluate all of the child conditions recursively if we have children
        List<? extends ConditionTree> childConditions = getChildConditions();
        if (childConditions != null && childConditions.size() > 0) {
            for (ConditionTree condition : childConditions) {
                switch (operator) {
                    case Operator.AND:
                        isConditionHold = isConditionHold && condition.evaluate(event);
                        break;

                    case Operator.OR:
                        isConditionHold = isConditionHold || condition.evaluate(event);
                        break;
                }
            }
        } else {
            // If we are on the leaf then we evaluate the condition.
            isConditionHold = getRuleCondition().evaluate(event);
        }

        return isConditionHold;
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------

    public boolean isBuilt() {
        return isBuilt;
    }

    public List<RuleConditionTree> getTempChildConditions() {
        return mTempChildConditions;
    }

    public void setTempChildConditions(List<RuleConditionTree> tempChildConditions) {
        mTempChildConditions = tempChildConditions;
    }
}
