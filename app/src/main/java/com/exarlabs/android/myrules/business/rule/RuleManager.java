package com.exarlabs.android.myrules.business.rule;

import java.util.List;

import javax.inject.Inject;

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

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    @Inject
    public RuleManager(DaoManager daoManager) {
        mDaoManager = daoManager;
        mRuleRecordDao = mDaoManager.getRuleRecordDao();
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    public List<RuleRecord> loadAllRules() {
        return mRuleRecordDao.loadAll();
    }

    /**
     * Loads the list of rules which are responding to a specified event and it has the given status.
     * @param eventCode the code of the event
     * @param status the status of the event
     * @return
     */
    public List<RuleRecord> getRules(int eventCode, int status) {
        return mRuleRecordDao.loadAll();
    }

    public long insert(RuleRecord entity) {
        return mRuleRecordDao.insert(entity);
    }



    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
