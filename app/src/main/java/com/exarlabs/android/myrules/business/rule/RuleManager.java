package com.exarlabs.android.myrules.business.rule;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import com.exarlabs.android.myrules.business.database.DaoManager;
import com.exarlabs.android.myrules.business.rule.action.ActionManager;
import com.exarlabs.android.myrules.business.rule.condition.ConditionManager;
import com.exarlabs.android.myrules.model.dao.RuleActionLinkDao;
import com.exarlabs.android.myrules.model.dao.RuleConditionTree;
import com.exarlabs.android.myrules.model.dao.RuleConditionTreeDao;
import com.exarlabs.android.myrules.model.dao.RuleRecord;
import com.exarlabs.android.myrules.model.dao.RuleRecordDao;

/**
 * Manager for rules.
 * Created by becze on 12/15/2015.
 */
public class RuleManager {
    private final RuleConditionTreeDao mRuleConditionTreeDao;

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    public static RuleRecord generateRandom() {
        RuleRecord rule = new RuleRecord();
        return rule;
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private DaoManager mDaoManager;
    private final RuleRecordDao mRuleRecordDao;
    private final ConditionManager mConditionManager;
    private final ActionManager mActionManager;
    private final RuleActionLinkDao mRuleActionLinkDao;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    @Inject
    public RuleManager(DaoManager daoManager, ConditionManager conditionManager, ActionManager actionManager) {
        mDaoManager = daoManager;
        mRuleRecordDao = mDaoManager.getRuleRecordDao();
        mRuleActionLinkDao = mDaoManager.getRuleActionLinkDao();
        mRuleConditionTreeDao = mDaoManager.getRuleConditionTreeDao();
        mConditionManager = conditionManager;
        mActionManager = actionManager;
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    public List<RuleRecord> loadAllRules() {
        return mRuleRecordDao.loadAll();
    }

    /**
     * Loads the list of rules which are responding to a specified event and it has the given status.
     *
     * @param eventCode the code of the event
     * @param status    the status of the event
     *
     * @return
     */
    public List<RuleRecord> getRules(int eventCode, int status) {
        //@formatter:off
        return mRuleRecordDao.queryBuilder()
                        .where(RuleRecordDao.Properties.EventCode.eq(eventCode), RuleRecordDao.Properties.State.eq(status))
                        .list();
        //@formatter:on
    }

    public long insert(RuleRecord entity) {
        return mRuleRecordDao.insert(entity);
    }

    /**
     * Saves / updates a rule record on the current thread.
     *
     * @return
     */
    public long saveRuleRecord(RuleRecord ruleRecord) {

        long id = mRuleRecordDao.insertOrReplace(ruleRecord);

        // save update the conditions
        mConditionManager.insertOrReplaceConditionTree(ruleRecord.getRuleConditionTree());

        // if there were any temporary actions save them
        ruleRecord.addTempraryRuleActions();
        // save the actions

        mActionManager.saveActionLinks(ruleRecord, ruleRecord.getRuleActionLinks());

        // save the rule
        return id;
    }

    public RuleRecord load(Long key) {
        return mRuleRecordDao.load(key);
    }

    /**
     * Deletes the rule record.
     *
     * @param ruleRecord
     */
    public void deleteRule(RuleRecord ruleRecord) {
        if (ruleRecord.isAttached()) {
            // we have to delete the rule and all the realted links
            mRuleActionLinkDao.deleteInTx(ruleRecord.getRuleActionLinks());

            deleteRuleConditionLinks(ruleRecord.getRuleConditionTree());

            // delete the rule itself
            mRuleRecordDao.delete(ruleRecord);
        }
    }

    /**
     * Delete recursively all the links
     * @param ruleConditionTree
     */
    public void deleteRuleConditionLinks(RuleConditionTree ruleConditionTree) {
        List<RuleConditionTree> childConditions = ruleConditionTree.getChildConditions();
        for (RuleConditionTree child : childConditions) {
            deleteRuleConditionLinks(child);
        }
        mRuleConditionTreeDao.delete(ruleConditionTree);
    }

    /**
     * Returns all the defined perimissions needed to run this rule.
     *
     * @param ruleRecord
     *
     * @return
     */
    public Set<String> getPermissions(RuleRecord ruleRecord) {
        Set<String> requiredPermissions = new HashSet<>();

        if (!ruleRecord.isAttached()) {
            return requiredPermissions;
        }

        // Add the required permissions by the conditions tree
        requiredPermissions.addAll(mConditionManager.getPermissions(ruleRecord.getRuleConditionTree()));

        // add the required permission by the actions
        requiredPermissions.addAll(mActionManager.getPermissions(ruleRecord.getRuleActions()));

        return requiredPermissions;
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
