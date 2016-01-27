package com.exarlabs.android.myrules.business.rule;

import java.util.List;

import javax.inject.Inject;

import com.exarlabs.android.myrules.business.action.ActionManager;
import com.exarlabs.android.myrules.business.condition.ConditionManager;
import com.exarlabs.android.myrules.business.database.DaoManager;
import com.exarlabs.android.myrules.model.dao.RuleRecord;
import com.exarlabs.android.myrules.model.dao.RuleRecordDao;

/**
 * Manager for rules.
 * Created by becze on 12/15/2015.
 */
public class RuleManager {

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

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    @Inject
    public RuleManager(DaoManager daoManager, ConditionManager conditionManager, ActionManager actionManager) {
        mDaoManager = daoManager;
        mRuleRecordDao = mDaoManager.getRuleRecordDao();
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
     * @param status the status of the event
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
        mConditionManager.insertConditionTree(ruleRecord.getRuleConditionTree());

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

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
