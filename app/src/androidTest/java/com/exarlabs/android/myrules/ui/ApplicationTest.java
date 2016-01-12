package com.exarlabs.android.myrules.ui;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.exarlabs.android.myrules.business.action.Action;
import com.exarlabs.android.myrules.business.condition.Condition;
import com.exarlabs.android.myrules.business.database.DaoManager;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.model.dao.RuleActionDao;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.model.dao.RuleConditionDao;
import com.exarlabs.android.myrules.model.dao.RuleRecordDao;

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
    private RuleRecordDao mRuleRecordDao;
    private RuleConditionDao mRuleConditionDao;
    private RuleActionDao mRuleActionDao;

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
        initDao();
        // create a condition structure
        RuleCondition c1 = generateNewCondition(Condition.Type.DEBUG_ALWAYS_TRUE, Condition.Operator.OR);
        RuleCondition c2 = generateNewCondition(Condition.Type.DEBUG_ALWAYS_FALSE, Condition.Operator.AND);
        RuleCondition c3 = generateNewCondition(Condition.Type.DEBUG_ALWAYS_TRUE, Condition.Operator.OR);
        RuleCondition c4 = generateNewCondition(Condition.Type.DEBUG_ALWAYS_FALSE, Condition.Operator.AND);
        RuleCondition c5 = generateNewCondition(Condition.Type.DEBUG_ALWAYS_TRUE, Condition.Operator.AND);

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
        assertTrue(c1.evaluate(null));
    }

    public void testActions() {
        initDao();
        RuleAction ruleAction = generateNewAction(Action.Type.DEBUG_HELLO_WORLD);
        ruleAction.build();
        assertTrue(ruleAction.run(null));
    }

    private void initDao() {

        mDaoManager = new DaoManager();
        mRuleRecordDao = mDaoManager.getRuleRecordDao();
        mRuleConditionDao = mDaoManager.getRuleConditionDao();
        mRuleActionDao = mDaoManager.getRuleActionDao();

        // delete everything in the database
        mRuleRecordDao.deleteAll();
        mRuleActionDao.deleteAll();
        mRuleConditionDao.deleteAll();
    }


    private RuleCondition generateNewCondition(int type, int operator) {
        RuleCondition c = new RuleCondition();
        c.setOperator(operator);
        c.setType(type);
        return c;
    }

    private RuleAction generateNewAction(int type) {
        RuleAction ruleAction = new RuleAction();
        ruleAction.setType(type);
        return ruleAction;
    }
}
