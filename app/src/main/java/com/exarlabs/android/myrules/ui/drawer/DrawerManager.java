package com.exarlabs.android.myrules.ui.drawer;

import java.util.ArrayList;

import javax.inject.Inject;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.ui.BuildConfig;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.octicons_typeface_library.Octicons;
import com.mikepenz.typeicons_typeface_library.Typeicons;

/**
 * Created by becze on 10/21/2015.
 */
public class DrawerManager {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------


    public enum MenuItem {
        MY_RULES("My Rules"),
        MY_CONDITIONS("My Conditions"),
        MY_ACTIONS("My Actions"),
        DEBUG("Debug");

        private String mLabel;

        MenuItem(String label) {
            mLabel = label;
        }
    }


    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private MenuItem mLastSelectedItem;

    @Inject
    public NavigationManager mNavigationManager;

    private Drawer mDrawer;
    private Context mContext;
    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public DrawerManager(Context context) {
        // we always select the first item on creation
        mLastSelectedItem = MenuItem.MY_RULES;
        DaggerManager.component().inject(this);

        mContext = context;
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------


    /**
     * Creates a new drawer for this application with predefined items
     *
     * @param activity
     * @param toolbar
     * @return
     */
    public void buildDrawer(Activity activity, Toolbar toolbar) {

        //@formatter:off
        mDrawer = new DrawerBuilder()
                        .withActivity(activity)
                        .withToolbar(toolbar)
                        .withDrawerItems(getDrawerItems())
                        .withSelectedItemByPosition(getLastSelectedItemPosition())
                        .withAccountHeader(getAccountHeader(activity))
                        .addDrawerItems()
                        .build();
        //@formatter:on
    }

    private AccountHeader getAccountHeader(Activity activity) {
        //@formatter:off

//        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.drawer_header, null);
        return new AccountHeaderBuilder()
                        .withActivity(activity)
                        .withHeaderBackground(R.color.primary_dark)
                        .addProfiles(
                            new ProfileDrawerItem()
                                            .withName(activity.getString(R.string.app_name))
                        )
                        .build();
        //@formatter:on
    }


    /**
     * @return the list of items
     */
    public ArrayList<IDrawerItem> getDrawerItems() {

        // The menu items in the drawer
        ArrayList<IDrawerItem> items = new ArrayList<>();

        // Dashboard
        //if you want to update the items at a later time it is recommended to keep it in a variable
        //@formatter:off
        items.add(new PrimaryDrawerItem()
                        .withName(MenuItem.MY_RULES.mLabel).withIcon(Octicons.Icon.oct_checklist)
                        .withIconColorRes(R.color.text_primary).withSelectedIconColorRes(R.color.text_primary));

        items.add(new PrimaryDrawerItem()
                        .withName(MenuItem.MY_CONDITIONS.mLabel).withIcon(FontAwesome.Icon.faw_wrench)
                        .withIconColorRes(R.color.text_primary).withSelectedIconColorRes(R.color.text_primary));

        items.add(new PrimaryDrawerItem()
                        .withName(MenuItem.MY_ACTIONS.mLabel).withIcon(Typeicons.Icon.typ_arrow_forward_outline)
                        .withIconColorRes(R.color.text_primary).withSelectedIconColorRes(R.color.text_primary));

        if(BuildConfig.DEBUG){
            items.add(new PrimaryDrawerItem()
                        .withName(MenuItem.DEBUG.mLabel).withIcon(FontAwesome.Icon.faw_bug)
                        .withIconColorRes(R.color.menu_item_6).withSelectedIconColorRes(R.color.menu_item_6));
        }

        //@formatter:on
        decorate(items);

        return items;
    }

    private void decorate(ArrayList<IDrawerItem> items) {
        for (IDrawerItem item : items) {
            if (item instanceof PrimaryDrawerItem) {
                ((PrimaryDrawerItem) item).withSelectedColorRes(R.color.menu_item_selected);
            }
        }
    }


    /**
     * Handles the menu item selected event
     *
     * @param context
     * @param view
     * @param i
     * @param iDrawerItem
     * @return
     */
    public boolean handleItemSelected(Context context, View view, int i, IDrawerItem iDrawerItem) {

        i--;
        if (i >= 0 && i < MenuItem.values().length) {

            MenuItem item = MenuItem.values()[i];

            if (item != mLastSelectedItem) {

                switch (item) {
                    case MY_RULES:
                        mNavigationManager.startRulesOverview();
                        break;

                    case MY_CONDITIONS:
                        mNavigationManager.startConditionsOverview();
                        break;

                    case MY_ACTIONS:
                        mNavigationManager.startActionsOverview();
                        break;

                    case DEBUG:
                        mNavigationManager.startDebugOverview();
                        break;

                    default:
                        mNavigationManager.startRulesOverview();
                        break;

                }

                mLastSelectedItem = item;
                closeDrawer();
                return true;
            }

        }

        return false;
    }

    private void closeDrawer() {
        if (mDrawer != null) {
            mDrawer.closeDrawer();
        }
    }


    /**
     * Enables/disbales the drawer the drawer
     *
     * @param isEnabled
     */
    public void enableDrawer(boolean isEnabled) {
        if (mDrawer != null) {
            mDrawer.getDrawerLayout().setDrawerLockMode(isEnabled ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    /**
     * Enable / disable the drawer indicator
     * @param isEnabled
     */
    public void enableActionBarDrawerToggle(boolean isEnabled) {
        if (mDrawer != null) {
            mDrawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(isEnabled);
        }
    }

    /**
     * Initializes the drawer state.
     */
    public void reset() {
        mLastSelectedItem = MenuItem.MY_RULES;
    }


    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------


    /**
     * Get the lst selected drawer item.
     *
     * @return
     */

    public int getLastSelectedItemPosition() {
        return mLastSelectedItem.ordinal();
    }

    public Drawer getDrawer() {
        return mDrawer;
    }
}
