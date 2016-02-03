package com.exarlabs.android.myrules.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.exarlabs.android.myrules.business.rule.action.Action;
import com.exarlabs.android.myrules.business.rule.action.ActionManager;
import com.exarlabs.android.myrules.business.rule.action.ActionPlugin;
import com.exarlabs.android.myrules.business.rule.action.plugins.FibonacciActionPlugin;
import com.exarlabs.android.myrules.business.rule.action.plugins.MultiplyActionPlugin;
import com.exarlabs.android.myrules.business.rule.condition.Condition;
import com.exarlabs.android.myrules.business.rule.condition.ConditionManager;
import com.exarlabs.android.myrules.business.rule.condition.ConditionPlugin;
import com.exarlabs.android.myrules.business.rule.condition.ConditionTree;
import com.exarlabs.android.myrules.business.rule.condition.plugins.AlwaysTrueConditionPlugin;
import com.exarlabs.android.myrules.business.rule.condition.plugins.math.IsNumberEqualConditionPlugin;
import com.exarlabs.android.myrules.business.rule.condition.plugins.math.IsNumberInIntervalConditionPlugin;
import com.exarlabs.android.myrules.business.database.DaoManager;
import com.exarlabs.android.myrules.business.rule.event.plugins.math.NumberEvent;
import com.exarlabs.android.myrules.business.rule.RuleManager;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.model.dao.RuleActionDao;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.model.dao.RuleConditionDao;
import com.exarlabs.android.myrules.model.dao.RuleConditionTree;
import com.exarlabs.android.myrules.model.dao.RuleRecord;
import com.exarlabs.android.myrules.model.dao.RuleRecordDao;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public static final String KEY_TEST1 = "test";
    public static final String VALUE_TEST_1 = "Heureka";


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
    private ConditionManager mConditionManager;
    private ActionManager mActionManager;
    private RuleManager mRuleManager;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createDaos();
    }


    // ------------------------------------------------------------------------
    // TESTS
    // ------------------------------------------------------------------------

    /**
     * Tests if the condition are recursively correctly evaluated
     */
    public void testConditionsEvaluation() {
        // create a condition structure
        RuleCondition c1 = generateNewCondition(Condition.Type.DEBUG_ALWAYS_TRUE.getType());
        RuleCondition c2 = generateNewCondition(Condition.Type.DEBUG_ALWAYS_FALSE.getType());
        RuleCondition c3 = generateNewCondition(Condition.Type.DEBUG_ALWAYS_TRUE.getType());
        RuleCondition c4 = generateNewCondition(Condition.Type.DEBUG_ALWAYS_FALSE.getType());
        RuleCondition c5 = generateNewCondition(Condition.Type.DEBUG_ALWAYS_FALSE.getType());

        List<RuleCondition> condition = new ArrayList<>();
        Collections.addAll(condition, c1, c2, c3, c4, c5);
        mConditionManager.saveConditions(condition);


        // Build the condition tree
        RuleConditionTree.Builder builder = new ConditionTree.Builder();
        builder.add(c1, new RuleCondition[] { c2, c3 }, ConditionTree.Operator.OR);
        builder.add(c3, new RuleCondition[] { c4, c5 }, ConditionTree.Operator.OR);
        RuleConditionTree root = mConditionManager.buildConditionTree(builder);

        Long rootId = root.getId();

        refreshDaos();
        RuleConditionTree tree = mConditionManager.loadConditionTree(rootId);
        tree.build();

        // Check if we have the children
        assertTrue(tree.getChildConditions().size() == 2);
        assertTrue(tree.getChildConditions().get(1).getChildConditions().size() == 2);

        // condition is evaluated to true
        assertTrue(!tree.evaluate(null));
    }

    public void testConditionProperties() {

        // create a condition structure
        RuleCondition c = generateNewCondition(Condition.Type.DEBUG_ALWAYS_TRUE.getType());
        ConditionPlugin conditionPlugin = c.getConditionPlugin();

        assertTrue(conditionPlugin instanceof AlwaysTrueConditionPlugin);

        for (int i = 0; i < 5; i++) {
            conditionPlugin.saveProperty(KEY_TEST1 + i, VALUE_TEST_1 + i);
        }

        mConditionManager.saveCondition(c);

        Long conditionId = c.getId();
        assertTrue(conditionId > 0);

        refreshDaos();
        RuleCondition ruleCondition = mConditionManager.loadCondition(conditionId);
        ruleCondition.build();

        for (int i = 0; i < 5; i++) {
            assertTrue(ruleCondition.getConditionPlugin().hasProperty(KEY_TEST1 + i));
            assertTrue(ruleCondition.getConditionPlugin().getProperty(KEY_TEST1 + i).getValue().equals(VALUE_TEST_1 + i));
        }
    }

    /**
     * Tests actions with some random properties
     */
    public void testActionProperties() {
        RuleAction ruleAction = generateNewAction(Action.Type.ARITHMETRIC_ACTION_FIBONACCI.getType());
        ActionPlugin plugin = ruleAction.getActionPlugin();

        assertTrue(plugin instanceof FibonacciActionPlugin);

        for (int i = 0; i < 5; i++) {
            plugin.saveProperty(KEY_TEST1 + i, VALUE_TEST_1 + i);
        }
        mActionManager.saveAction(ruleAction);

        Long conditionId = ruleAction.getId();
        assertTrue(conditionId > 0);

        refreshDaos();
        ruleAction = mActionManager.loadAction(conditionId);
        ruleAction.build();
        for (int i = 0; i < 5; i++) {
            assertTrue(ruleAction.getActionPlugin().hasProperty(KEY_TEST1 + i));
            assertTrue(ruleAction.getActionPlugin().getProperty(KEY_TEST1 + i).getValue().equals(VALUE_TEST_1 + i));
        }

    }

    /**
     * We create a rule which responds to Number events and with some conditions it calculates fibonacci and multiplications.
     */
    public void testSimpleArithmetricRule() {
        // Create the event
        NumberEvent event = new NumberEvent();


        /*
         * Create the conditions
         */
        RuleCondition cTrue = generateNewCondition(Condition.Type.DEBUG_ALWAYS_TRUE.getType());
        RuleCondition cTrue1 = generateNewCondition(Condition.Type.DEBUG_ALWAYS_TRUE.getType());

        RuleCondition cInterval = generateNewCondition(Condition.Type.ARITHMETRIC_IS_NUMBER_IN_INTERVAL.getType());
        ((IsNumberInIntervalConditionPlugin) cInterval.getConditionPlugin()).setMin(5);
        ((IsNumberInIntervalConditionPlugin) cInterval.getConditionPlugin()).setMax(500);

        RuleCondition cPrime = generateNewCondition(Condition.Type.ARITHMETRIC_IS_NUMBER_PRIME.getType());

        RuleCondition cEqual = generateNewCondition(Condition.Type.ARITHMETRIC_IS_NUMBER_EQUAL.getType());
        ((IsNumberEqualConditionPlugin) cEqual.getConditionPlugin()).setValue(1);

        List<RuleCondition> ruleConditions = new ArrayList<>();
        Collections.addAll(ruleConditions, cTrue, cTrue1, cInterval, cPrime, cEqual);
        mConditionManager.saveConditions(ruleConditions);

        // create dependencies between conditions
        // Build the condition tree
        RuleConditionTree.Builder builder = new ConditionTree.Builder();
        builder.add(cTrue, new RuleCondition[] { cEqual, cTrue1 }, ConditionTree.Operator.OR);
        builder.add(cTrue1, new RuleCondition[] { cInterval, cPrime }, ConditionTree.Operator.AND);
        RuleConditionTree root = mConditionManager.buildConditionTree(builder);

        /*
         * Create actions
         */

        RuleAction aMultiply = generateNewAction(Action.Type.ARITHMETRIC_ACTION_MULTIPLY.getType());
        ((MultiplyActionPlugin) aMultiply.getActionPlugin()).setValue(5);

        RuleAction aFib = generateNewAction(Action.Type.ARITHMETRIC_ACTION_FIBONACCI.getType());
        mActionManager.saveActions(aFib, aMultiply);

        // Create a rule with these actions and conditions
        RuleRecord ruleRecord = new RuleRecord();

        // set the event
        ruleRecord.setRuleName("Rule" + Math.random()*1000);
        ruleRecord.setEventCode(event.getType());
        ruleRecord.setRuleConditionTree(root);
        ruleRecord.addRuleActions(aFib, aMultiply);
        long ruleRecordId = mRuleManager.saveRuleRecord(ruleRecord);

        refreshDaos();

        ruleRecord = mRuleManager.load(ruleRecordId);
        ruleRecord.build();
        RuleConditionTree ruleConditionTree = ruleRecord.getRuleConditionTree();


        // check if the condition i true for some number events
        event.setValue(1);
        assertTrue(ruleConditionTree.evaluate(event));
        event.setValue(5);
        assertTrue(ruleConditionTree.evaluate(event));
        event.setValue(563);
        assertTrue(!ruleConditionTree.evaluate(event));

        event.setValue(20);
        // Run the actions
        List<RuleAction> ruleActions = ruleRecord.getRuleActions();

        assertTrue(ruleActions.size() == 2);

        for (RuleAction actions : ruleActions) {
            actions.run(event);

            if (actions.getActionPlugin() instanceof MultiplyActionPlugin) {
                assertTrue(((MultiplyActionPlugin) actions.getActionPlugin()).getResult() == 100);
            } else if (actions.getActionPlugin() instanceof FibonacciActionPlugin) {
                assertTrue(((FibonacciActionPlugin) actions.getActionPlugin()).getResult() == 6765);
            }
        }
    }

    // ------------------------------------------------------------------------
    // HELPER METHODS
    // ------------------------------------------------------------------------

    private void createDaos() {
        mDaoManager = new DaoManager();
        refreshDaos();
        deleteDao();
    }

    private void refreshDaos() {
        mDaoManager.createNewSession();
        mRuleRecordDao = mDaoManager.getRuleRecordDao();
        mRuleConditionDao = mDaoManager.getRuleConditionDao();
        mRuleActionDao = mDaoManager.getRuleActionDao();
        mConditionManager = new ConditionManager(mDaoManager);
        mActionManager = new ActionManager(mDaoManager);
        mRuleManager = new RuleManager(mDaoManager, mConditionManager, mActionManager);
    }

    private void deleteDao() {
        // delete everything in the database
        mDaoManager.deleteAll();
    }


    private RuleCondition generateNewCondition(int type) {
        RuleCondition c = new RuleCondition();
        c.setConditionName("Condition" + Math.random()*1000);
        c.setType(type);
        return c;
    }

    private RuleAction generateNewAction(int type) {
        RuleAction ruleAction = new RuleAction();
        ruleAction.setType(type);
        ruleAction.setActionName("Action" + Math.random()*1000);
        return ruleAction;
    }
}
