package com.exarlabs.android.myrules.business.rule;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.exarlabs.android.myrules.business.database.DaoManager;
import com.exarlabs.android.myrules.model.dao.Rule;
import com.exarlabs.android.myrules.model.dao.RuleDao;
import com.exarlabs.android.myrules.util.RandomUtil;

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

    public static Rule generateRandom() {
        Rule rule = new Rule();
        rule.setName(RandomUtil.getRandomStringNumber(10));
        rule.setDate(new Date());
        return rule;
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private DaoManager mDaoManager;
    private final RuleDao mRuleDao;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    @Inject
    public RuleManager(DaoManager daoManager) {
        mDaoManager = daoManager;
        mRuleDao = mDaoManager.getRuleDao();
    }


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    public List<Rule> loadAllRules() {
        return mRuleDao.loadAll();
    }

    public long insert(Rule entity) {
        return mRuleDao.insert(entity);
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
