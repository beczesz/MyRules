package com.exarlabs.android.myrules.business.action;

import java.util.List;

import javax.inject.Inject;

import com.exarlabs.android.myrules.business.database.DaoManager;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.model.dao.RuleActionDao;
import com.exarlabs.android.myrules.model.dao.RuleRecord;

/**
 * Manager for actions.
 * Created by becze on 12/15/2015.
 */
public class ActionManager {

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
    private final RuleActionDao mRuleActionDao;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    @Inject
    public ActionManager(DaoManager daoManager) {
        mDaoManager = daoManager;
        mRuleActionDao = mDaoManager.getRuleActionDao();
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------


    public RuleAction load(Long key) {
        return mRuleActionDao.load(key);
    }

    public List<RuleAction> loadAllConditions() {
        return mRuleActionDao.loadAll();
    }

    public long insert(RuleAction entity) {
        return mRuleActionDao.insert(entity);
    }

    public void update(RuleAction entity) {
        mRuleActionDao.update(entity);
    }

    public void deleteAll() {
        mRuleActionDao.deleteAll();
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
