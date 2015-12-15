package com.exarlabs.android.myrules.business.dagger.modules;

import javax.inject.Singleton;

import com.exarlabs.android.myrules.ui.navigation.NavigationManager;

import dagger.Module;
import dagger.Provides;

/**
 * Provides instances for navigation
 * Created by becze on 11/13/2015.
 */
@Module
public class NavigationModule {


    @Provides
    @Singleton
    protected NavigationManager provideNavigationManager() {
        return new NavigationManager();
    }
}
