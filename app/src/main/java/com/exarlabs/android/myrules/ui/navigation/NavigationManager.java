package com.exarlabs.android.myrules.ui.navigation;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.actions.ActionsAddActionFragment;
import com.exarlabs.android.myrules.ui.actions.ActionsOverviewFragment;
import com.exarlabs.android.myrules.ui.conditions.ConditionsAddConditionFragment;
import com.exarlabs.android.myrules.ui.conditions.ConditionsOverviewFragment;
import com.exarlabs.android.myrules.ui.events.EventsOverviewFragment;
import com.exarlabs.android.myrules.ui.history.HistoryListFragment;
import com.exarlabs.android.myrules.ui.rules.RulesAddRuleFragment;
import com.exarlabs.android.myrules.ui.rules.RulesOverviewFragment;

/**
 * Helper class to ease the navigation between screens.
 * Created by becze on 9/30/2015.
 */
public class NavigationManager {

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

    private FragmentManager mFragmentManager;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    /**
     * Initialize the NavigationManager with a FragmentManager, which will be used at the
     * fragment transactions.
     *
     * @param fragmentManager
     */
    public void init(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    /**
     * Displays the next fragment
     *
     * @param fragment
     */
    private void open(Fragment fragment) {
        if (mFragmentManager != null) {
            //@formatter:off
            mFragmentManager.beginTransaction()
                            .replace(R.id.main_container, fragment)
                            .setCustomAnimations(R.anim.slide_in_left,
                                                 R.anim.slide_out_right,
                                                 R.anim.slide_in_right,
                                                 R.anim.slide_out_left)
                            .addToBackStack(fragment.getClass().getSimpleName())
                            .commit();
            //@formatter:on
        }
    }

    /**
     * pops every fragment and starts the given fragment as a new one.
     *
     * @param fragment
     */
    private void openAsRoot(Fragment fragment) {
        popEveryFragment();
        open(fragment);
    }


    /**
     * Pops all the queued fragments
     */
    private void popEveryFragment() {
        // Clear all back stack.
        int backStackCount = mFragmentManager.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {

            // Get the back stack fragment id.
            int backStackId = mFragmentManager.getBackStackEntryAt(i).getId();

            mFragmentManager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
    }

    /**
     * Navigates back by popping teh back stack. If there is no more items left we finish the current activity.
     *
     * @param baseActivity
     */
    public void navigateBack(Activity baseActivity) {

        if (mFragmentManager.getBackStackEntryCount() == 0) {
            // we can finish the base activity since we have no other fragments
            baseActivity.finish();
        } else {
            mFragmentManager.popBackStackImmediate();
        }
    }

    // MY RULES
    public void startRulesOverview() {
        Fragment fragment = RulesOverviewFragment.newInstance();
        openAsRoot(fragment);
    }

    public void startAddRuleFragment(){
        Fragment fragment = RulesAddRuleFragment.newInstance();
        open(fragment);
    }

    // MY CONDITIONS
    public void startConditionsOverview() {
        Fragment fragment = ConditionsOverviewFragment.newInstance();
        openAsRoot(fragment);
    }

    public void startAddConditionFragment(){
        Fragment fragment = ConditionsAddConditionFragment.newInstance();
        open(fragment);
    }

    // MY EVENTS
    public void startEventOverview() {
        Fragment fragment = EventsOverviewFragment.newInstance();
        openAsRoot(fragment);
    }

    // MY ACTIONS
    public void startActionsOverview() {
        Fragment fragment = ActionsOverviewFragment.newInstance();
        openAsRoot(fragment);
    }

    public void startAddActionFragment(Long id){
        Fragment fragment = ActionsAddActionFragment.newInstance(id);
        open(fragment);
    }

    // HISTORY
    public void startHistoryOverview() {
        Fragment fragment = HistoryListFragment.newInstance();
        openAsRoot(fragment);
    }


    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
