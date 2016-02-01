package com.exarlabs.android.myrules.business.rule.condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import com.exarlabs.android.myrules.business.database.DaoManager;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.model.dao.RuleConditionDao;
import com.exarlabs.android.myrules.model.dao.RuleConditionProperty;
import com.exarlabs.android.myrules.model.dao.RuleConditionPropertyDao;
import com.exarlabs.android.myrules.model.dao.RuleConditionTree;
import com.exarlabs.android.myrules.model.dao.RuleConditionTreeDao;


/**
 * Manager for conditions.
 * Created by becze on 12/15/2015.
 */
public class ConditionManager {

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

    private DaoManager mDaoManager;
    private final RuleConditionDao mRuleConditionDao;
    private final RuleConditionTreeDao mRuleConditionTreeDao;
    private final RuleConditionPropertyDao mRuleConditionPropertyDao;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    @Inject
    public ConditionManager(DaoManager daoManager) {
        mDaoManager = daoManager;
        mRuleConditionDao = mDaoManager.getRuleConditionDao();
        mRuleConditionTreeDao = mDaoManager.getRuleConditionTreeDao();
        mRuleConditionPropertyDao = mDaoManager.getRuleConditionPropertyDao();
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------


    public RuleCondition loadCondition(Long key) {
        return mRuleConditionDao.load(key);
    }

    public RuleConditionTree loadConditionTree(Long key) {
        return mRuleConditionTreeDao.load(key);
    }

    public List<RuleCondition> loadAllConditions() {
        return mRuleConditionDao.loadAll();
    }

    public long insert(RuleCondition entity) {
        return mRuleConditionDao.insert(entity);
    }

    public void update(RuleCondition entity) {
        mRuleConditionDao.update(entity);
    }

    public void deleteAll() {
        mRuleConditionDao.deleteAll();
    }

    /**
     * Inserts/Updates a condition taking care of it's property changes
     *
     * @param condition
     */
    public void saveCondition(RuleCondition condition) {
        // Save the condition and it's properties
        mRuleConditionDao.insertOrReplace(condition);
        mRuleConditionPropertyDao.deleteInTx(condition.getProperties());
        condition.getProperties().clear();
        List<RuleConditionProperty> properties = condition.getConditionPlugin().getProperties();
        condition.getProperties().addAll(properties);

        // set the parent condition
        for (RuleConditionProperty property : properties) {
            property.setConditionId(condition.getId());
        }

        mRuleConditionPropertyDao.insertOrReplaceInTx(properties);

        if (!condition.isBuilt()) {
            condition.build();
        }
    }

    /**
     * Save a list of conditions.
     * Note: this is not saved in a transaction.
     *
     * @param conditions
     */
    public void saveConditions(List<RuleCondition> conditions) {
        for (RuleCondition c : conditions) {
            saveCondition(c);
        }
    }


    /**
     * Removes the condition tree with the given root.
     * It is deleted on the current thread on which is invoked.
     * Note the root must be attached.
     *
     * @param root
     */
    public void removeConditionTree(RuleConditionTree root) {
        List<RuleConditionTree> childConditions = root.getChildConditions();
        if (childConditions != null) {
            for (RuleConditionTree child : childConditions) {
                removeConditionTree(child);
            }
        }

        // Delete last the root
        mRuleConditionTreeDao.delete(root);
    }

    /**
     * Builds the condition tree by inserting the RuleConditionTree into the database
     * Note:  the inertion is done on the caller thread.
     */
    public void insertOrReplaceConditionTree(RuleConditionTree root) {
        insertOrReplaceConditionTree(null, root);
    }

    private void insertOrReplaceConditionTree(RuleConditionTree parent, RuleConditionTree root) {

        // Insert the root
        if (parent != null) {
            root.setParentCondition(parent.getId());
        }
        mRuleConditionTreeDao.insertOrReplace(root);
        if (parent != null && parent.getChildConditions() != null && !parent.getChildConditions().contains(root)) {
            parent.getChildConditions().add(root);
        }


        // Recursively insert every child and attach to the root
        List<RuleConditionTree> children = root.getTempChildConditions();
        if (children != null) {
            for (RuleConditionTree child : children) {
                insertOrReplaceConditionTree(root, child);
            }
        }
    }


    /**
     * Deletes the old condition tree and rebuilds the new one.
     *
     * @param oldTree the root of the old tree
     * @param newCodnitionTree the Builder of the new tree
     * @return the root for the new tree.
     */
    public RuleConditionTree rebuildConditionTree(RuleConditionTree oldTree, ConditionTree.Builder newCodnitionTree) {
        RuleConditionTree newRoot = newCodnitionTree.build();
        removeConditionTree(oldTree);
        insertOrReplaceConditionTree(newRoot);
        return newRoot;
    }

    /**
     * Builds the condition tree
     *
     * @param newCodnitionTree
     * @return
     */
    public RuleConditionTree buildConditionTree(ConditionTree.Builder newCodnitionTree) {
        RuleConditionTree newRoot = newCodnitionTree.build();
        insertOrReplaceConditionTree(newRoot);
        return newRoot;
    }

    /**
     * It generates a default condition and saves it into the database.
     * It is required by condition tree roots, to be attached
     *
     * @return
     */
    public RuleCondition getDefaultCondition() {
        RuleCondition c = new RuleCondition();
        c.setType(Condition.Type.DEBUG_ALWAYS_TRUE);
        saveCondition(c);
        return c;
    }

    /**
     * Returns all the defined perimissions needed to run this rule.
     *
     * @param ruleRecord
     * @return
     */
    public Set<String> getPermissions(RuleConditionTree ruleConditionTree) {
        Set<String> requiredPermissions = new HashSet<>();

        // Make sure that the condition is built
        if (!ruleConditionTree.isBuilt()) {
            ruleConditionTree.build();
        }

        // Add the current condition's permission
        requiredPermissions.addAll(ruleConditionTree.getRuleCondition().getConditionPlugin().getRequiredPermissions());

        for (RuleConditionTree conditionTree : ruleConditionTree.getChildConditions()) {
            requiredPermissions.addAll(getPermissions(conditionTree));
        }

        return requiredPermissions;
    }
}
