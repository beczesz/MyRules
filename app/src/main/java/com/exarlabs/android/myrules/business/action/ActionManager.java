package com.exarlabs.android.myrules.business.action;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import com.exarlabs.android.myrules.business.database.DaoManager;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.model.dao.RuleActionDao;
import com.exarlabs.android.myrules.model.dao.RuleActionLink;
import com.exarlabs.android.myrules.model.dao.RuleActionLinkDao;
import com.exarlabs.android.myrules.model.dao.RuleActionProperty;
import com.exarlabs.android.myrules.model.dao.RuleActionPropertyDao;
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
    private final RuleActionLinkDao mRuleActionLinkDao;
    private final RuleActionPropertyDao mRuleActionPropertiesDao;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    @Inject
    public ActionManager(DaoManager daoManager) {
        mDaoManager = daoManager;
        mRuleActionDao = mDaoManager.getRuleActionDao();
        mRuleActionLinkDao = mDaoManager.getRuleActionLinkDao();
        mRuleActionPropertiesDao = mDaoManager.getRuleActionPropertyDao();
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------


    public RuleAction loadAction(Long key) {
        return mRuleActionDao.load(key);
    }

    public List<RuleAction> loadAllActions() {
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

    /**
     * Inserts/Updates an action taking care of it's property changes
     *
     * @param ruleAction
     */
    public void saveAction(RuleAction ruleAction) {
        // Save the ruleAction and it's properties
        mRuleActionDao.insertOrReplace(ruleAction);
        mRuleActionPropertiesDao.deleteInTx(ruleAction.getProperties());
        ruleAction.getProperties().clear();
        List<RuleActionProperty> properties = ruleAction.getActionPlugin().getProperties();
        ruleAction.getProperties().addAll(properties);

        // set the parent for the property actions
        for (RuleActionProperty property : properties) {
            property.setActionId(ruleAction.getId());
        }
        mRuleActionPropertiesDao.insertOrReplaceInTx(properties);

        ruleAction.rebuild();
    }

    /**
     * Save a list of ruleActions.
     * Note: this is not saved in a transaction.
     *
     * @param ruleActions
     */
    public void saveActions(List<RuleAction> ruleActions) {
        for (RuleAction ruleAction : ruleActions) {
            saveAction(ruleAction);
        }
    }

    public void saveActions(RuleAction... actions) {
        saveActions(Arrays.asList(actions));
    }

    /**
     * Inserts/Updates an action taking care of it's property changes
     *
     * @param ruleRecord
     * @param ruleActionLink
     */
    public void saveActionLink(RuleRecord ruleRecord, RuleActionLink ruleActionLink) {
        ruleActionLink.setRuleId(ruleRecord.getId());
        // set the 1:N link for rule actions
        ruleActionLink.getRuleAction().getRuleActionLinks().add(ruleActionLink);
        ruleActionLink.setActionId(ruleActionLink.getRuleAction().getId());
        mRuleActionLinkDao.insertOrReplace(ruleActionLink);

    }


    /**
     * Save a list of ruleActionsLinks.
     * Note: this is not saved in a transaction.
     *
     * @param ruleRecord
     * @param ruleActionLinks
     */
    public void saveActionLinks(RuleRecord ruleRecord, List<RuleActionLink> ruleActionLinks) {
        for (RuleActionLink ruleAction : ruleActionLinks) {
            saveActionLink(ruleRecord, ruleAction);
        }
    }

    public void saveActionsLinks(RuleRecord ruleRecord, RuleActionLink... actions) {
        saveActionLinks(ruleRecord, Arrays.asList(actions));
    }

    /**
     * Deletes an action and all of it's properties
     * 
     * @param ruleAction
     */
    public void deleteAction(RuleAction ruleAction){
        List<RuleActionProperty> properties = ruleAction.getProperties();

        // delete the properties
        mRuleActionPropertiesDao.deleteInTx(properties);
        // and the action
        mRuleActionDao.delete(ruleAction);
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
