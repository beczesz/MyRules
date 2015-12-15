package com.exarlabs.android.myrules.business.database;

import javax.inject.Inject;

import android.content.Context;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.model.dao.DaoMaster;
import com.exarlabs.android.myrules.model.dao.DaoSession;
import com.exarlabs.android.myrules.model.dao.RuleDao;

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

    @Inject
    public Context mContext;

    private final DaoMaster mDaoMaster;
    private final DaoSession mDaoSession;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public DaoManager() {
        DaggerManager.component().inject(this);

        // Initialize the green dao master
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(mContext, DATABASE_NAME, null);
        mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------

    public RuleDao getRuleDao() {
        return mDaoSession.getRuleDao();
    }
}
