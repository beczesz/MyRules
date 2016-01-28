package com.exarlabs.android.myrules.business.dagger.modules;

import javax.inject.Inject;
import javax.inject.Singleton;

import android.content.Context;

import com.exarlabs.android.myrules.business.database.DaoManager;
import com.exarlabs.android.myrules.business.provider.PhoneContactManager;

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

    @Inject
    @Provides
    @Singleton
    protected PhoneContactManager providePhoneContactManager(Context context) {
        return new PhoneContactManager(context);
    }


}
