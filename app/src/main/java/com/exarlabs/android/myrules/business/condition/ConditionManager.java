package com.exarlabs.android.myrules.business.condition;

import java.util.List;

import javax.inject.Inject;

import com.exarlabs.android.myrules.business.database.DaoManager;
import com.exarlabs.android.myrules.model.dao.Rule;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.model.dao.RuleConditionDao;

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

    public static Rule generateRandom() {
        Rule rule = new Rule();
        return rule;
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private DaoManager mDaoManager;
    private final RuleConditionDao mRuleConditionDao;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    @Inject
    public ConditionManager(DaoManager daoManager) {
        mDaoManager = daoManager;
        mRuleConditionDao = mDaoManager.getRuleConditionDao();
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------


    public RuleCondition load(Long key) {
        return mRuleConditionDao.load(key);
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

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
