package com.exarlabs.android.myrules.business.dagger.modules;

import javax.inject.Singleton;

import com.exarlabs.android.myrules.business.devel.DevelManager;

import dagger.Module;
import dagger.Provides;

/**
 * Provides developer helper modules
 * Created by becze on 12/15/2015.
 */
@Module
public class DevelModule {

    @Provides
    @Singleton
    protected DevelManager providesDevelManager() {
        return new DevelManager();
    }
}
