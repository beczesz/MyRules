package com.exarlabs.android.myrules.ui;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.exarlabs.android.myrules.business.condition.Condition;
import com.exarlabs.android.myrules.business.database.DaoManager;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.model.dao.RuleConditionDao;
import com.exarlabs.android.myrules.model.dao.RuleDao;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {


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
    private RuleDao mRuleDao;
    private RuleConditionDao mRuleConditionDao;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public ApplicationTest() {
        super(Application.class);
    }
    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------

    public void testConditions() {
        mDaoManager = new DaoManager();
        mRuleDao = mDaoManager.getRuleDao();
        mRuleConditionDao = mDaoManager.getRuleConditionDao();

        // delete everything in the database
        mRuleDao.deleteAll();
        mRuleConditionDao.deleteAll();


        // create a condition structure
        RuleCondition c1 = generateNew(Condition.Type.DEBUG_ALWAYS_TRUE, Condition.Operator.AND);
        RuleCondition c2 = generateNew(Condition.Type.DEBUG_ALWAYS_FALSE, Condition.Operator.AND);
        RuleCondition c3 = generateNew(Condition.Type.DEBUG_ALWAYS_TRUE, Condition.Operator.AND);
        RuleCondition c4 = generateNew(Condition.Type.DEBUG_ALWAYS_TRUE, Condition.Operator.AND);
        RuleCondition c5 = generateNew(Condition.Type.DEBUG_ALWAYS_TRUE, Condition.Operator.AND);

        mRuleConditionDao.insert(c1);
        mRuleConditionDao.insert(c2);
        mRuleConditionDao.insert(c3);
        mRuleConditionDao.insert(c4);
        mRuleConditionDao.insert(c5);

        // build up the relations
        c2.setParentCondition(c1.getId());
        c3.setParentCondition(c1.getId());
        c4.setParentCondition(c3.getId());
        c5.setParentCondition(c3.getId());

        c1.getChildConditions().add(c2);
        c1.getChildConditions().add(c3);
        c3.getChildConditions().add(c4);
        c3.getChildConditions().add(c5);

        mRuleConditionDao.update(c1);
        mRuleConditionDao.update(c2);
        mRuleConditionDao.update(c3);
        mRuleConditionDao.update(c4);
        mRuleConditionDao.update(c5);

        c1.build();

        // condition is evaluated to true
        assertTrue(!c1.evaluate(null));
    }

    private RuleCondition generateNew(int type, int operator) {
        RuleCondition c = new RuleCondition();
        c.setOperator(operator);
        c.setType(type);
        return c;
    }
}
