package com.exarlabs.android.myrules.ui;

import javax.inject.Inject;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.ui.drawer.DrawerManager;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Main activity which holds the container for the fragment navigation.
 */
public class MainActivity extends BaseActivity implements Drawer.OnDrawerItemClickListener {


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

    @Inject
    public DrawerManager mDrawerManager;

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

        initDrawer();
    }

    /**
     * Initializes the drawer
     */
    public void initDrawer() {
        mDrawerManager.buildDrawer(this, mToolbar);
        mDrawerManager.getDrawer().setOnDrawerItemClickListener(this);
    }

    @Override
    public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
        boolean isHandled = mDrawerManager.handleItemSelected(this, view, i, iDrawerItem);
        if (isHandled) {
            mDrawerManager.getDrawer().closeDrawer();
        }

        return isHandled;
    }

    @Override
    public void onBackPressed() {
        // Note: we intentionally not calling the super implementation since in the CIS app
        // we decided to use support fragment manager.

        //we pressed the back button, show the logout dialog
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            // we have only one fragment left so we would close the application with this back
            showExitDialog();
        } else {
            mNavigationManager.navigateBack(this);
        }
    }

    /**
     * Shows the logout dialog. Stops the service and finishes the application.
     */
    protected void showExitDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.exit_message)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, (dialog, id) -> {finish();})
                        .setNegativeButton(android.R.string.cancel, null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------

}
