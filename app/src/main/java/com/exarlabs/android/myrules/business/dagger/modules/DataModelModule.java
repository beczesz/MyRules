package com.exarlabs.android.myrules.business.dagger.modules;

import javax.inject.Singleton;

import com.exarlabs.android.myrules.business.database.DaoManager;

import dagger.Module;
import dagger.Provides;

/**
 * Provides the singleton DAOs and other helper classes which hive access to the data of this application.
 * Created by becze on 11/17/2015.
 */
@Module
public class DataModelModule {

    // ------------------------------------------------------------------------
    // DAO
    // ------------------------------------------------------------------------

    @Provides
    @Singleton
    protected DaoManager provideDaoManager() {
        return new DaoManager();
    }



}
