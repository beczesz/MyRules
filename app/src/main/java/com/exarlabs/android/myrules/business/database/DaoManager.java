package com.exarlabs.android.myrules.business.database;

import android.content.Context;

import com.exarlabs.android.myrules.model.dao.DaoMaster;
import com.exarlabs.android.myrules.model.dao.DaoSession;
import com.exarlabs.android.myrules.model.dao.RuleActionDao;
import com.exarlabs.android.myrules.model.dao.RuleActionLinkDao;
import com.exarlabs.android.myrules.model.dao.RuleActionPropertyDao;
import com.exarlabs.android.myrules.model.dao.RuleConditionDao;
import com.exarlabs.android.myrules.model.dao.RuleConditionPropertyDao;
import com.exarlabs.android.myrules.model.dao.RuleConditionTreeDao;
import com.exarlabs.android.myrules.model.dao.RuleRecordDao;

/**
 * Dao Manager object which initializes and provides the diferent Daos
 * Created by becze on 12/15/2015.
 */
public class DaoManager {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------
    public static final String DATABASE_NAME = "rules-db";

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    public Context mContext;

    private final DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public DaoManager(Context context) {
        mContext = context;

        // Initialize the green dao master
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(mContext, DATABASE_NAME, null);
        mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        createNewSession();

    }



    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------


    public RuleConditionDao getRuleConditionDao() {
        return mDaoSession.getRuleConditionDao();
    }

    public RuleConditionPropertyDao getRuleConditionPropertyDao() {
        return mDaoSession.getRuleConditionPropertyDao();
    }

    public RuleConditionTreeDao getRuleConditionTreeDao() {
        return mDaoSession.getRuleConditionTreeDao();
    }

    public RuleActionDao getRuleActionDao() {
        return mDaoSession.getRuleActionDao();
    }

    public RuleActionPropertyDao getRuleActionPropertyDao() {
        return mDaoSession.getRuleActionPropertyDao();
    }

    public RuleRecordDao getRuleRecordDao() {
        return mDaoSession.getRuleRecordDao();
    }

    public RuleActionLinkDao getRuleActionLinkDao() {
        return mDaoSession.getRuleActionLinkDao();
    }

    /**
     * Creates a new session
     */
    public void createNewSession() {
        mDaoSession = mDaoMaster.newSession();
    }

    /**
     * Clears all the database content.
     * You should never do this only for debug purposes
     */
    public void deleteAll() {
        getRuleConditionDao().deleteAll();
        getRuleConditionPropertyDao().deleteAll();
        getRuleConditionTreeDao().deleteAll();
        getRuleActionDao().deleteAll();
        getRuleActionLinkDao().deleteAll();
        getRuleActionPropertyDao().deleteAll();
        getRuleRecordDao().deleteAll();
    }

}
