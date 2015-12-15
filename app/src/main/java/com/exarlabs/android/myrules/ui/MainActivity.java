package com.exarlabs.android.myrules.ui;

import javax.inject.Inject;

import android.os.Bundle;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;

/**
 * Main activity which holds the container for the fragment navigation.
 */
public class MainActivity extends BaseActivity {


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

    @Inject
    public NavigationManager mNavigationManager;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inject members
        DaggerManager.component().inject(this);

        // Initialize the NavigationManager with this activity's FragmentManager
        mNavigationManager.init(getSupportFragmentManager());

        // start as the first screen the rules overview
        mNavigationManager.startRulesOverview();
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------

}
