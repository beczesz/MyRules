package com.exarlabs.android.myrules.ui.navigation;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.actions.ActionDetailsFragment;
import com.exarlabs.android.myrules.ui.actions.ActionsOverviewFragment;
import com.exarlabs.android.myrules.ui.conditions.ConditionDetailsFragment;
import com.exarlabs.android.myrules.ui.conditions.ConditionsOverviewFragment;
import com.exarlabs.android.myrules.ui.debug.DebugOverviewFragment;
import com.exarlabs.android.myrules.ui.history.HistoryListFragment;
import com.exarlabs.android.myrules.ui.rules.RuleDetailsFragment;
import com.exarlabs.android.myrules.ui.rules.RulesAddRuleFragment;
import com.exarlabs.android.myrules.ui.rules.RulesOverviewFragment;
import com.exarlabs.android.myrules.ui.util.contact.ContactsSectorFragment;

/**
 * Helper class to ease the navigation between screens.
 * Created by becze on 9/30/2015.
 */
public class NavigationManager {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    /**
     * Listener interface for navigation events.
     */
    public interface NavigationListener {

        /**
         * Callback on backstack changed.
         *
         * @param fragment
         */
        void onBackstackChanged();
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

    private FragmentManager mFragmentManager;
    private NavigationListener mNavigationListener;

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
        mFragmentManager.addOnBackStackChangedListener(() -> {
            if (mNavigationListener != null) {
                mNavigationListener.onBackstackChanged();
            }
        });
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

    public void startAddRuleFragment() {
        Fragment fragment = RulesAddRuleFragment.newInstance();
        open(fragment);
    }

    public void startRuleDetailsFragment(long ruleId) {
        Fragment fragment = RuleDetailsFragment.newInstance(ruleId);
        open(fragment);
    }

    public void startContactsSelectorFragment(ContactsSectorFragment.ContactsSelectorListener listener) {
        ContactsSectorFragment fragment = ContactsSectorFragment.newInstance();
        fragment.setContactsSelectorListener(listener);
        open(fragment);
    }

    public void startConditionSelectorFragment(ConditionSectorFragment.ConditionSelectorListener listener) {
        ContactsSectorFragment fragment = ConditionSectorFragment.newInstance();
        fragment.setConditionSelectorListener(listener);
        open(fragment);
    }


    // MY CONDITIONS
    public void startConditionsOverview() {
        Fragment fragment = ConditionsOverviewFragment.newInstance();
        openAsRoot(fragment);
    }

    public void startConditionsDetails(Long id) {
        Fragment fragment = ConditionDetailsFragment.newInstance(id);
        open(fragment);
    }


    // MY ACTIONS
    public void startActionsOverview() {
        Fragment fragment = ActionsOverviewFragment.newInstance();
        openAsRoot(fragment);
    }

    public void startActionDetails(Long id) {
        Fragment fragment = ActionDetailsFragment.newInstance(id);
        open(fragment);
    }


    // HISTORY
    public void startHistoryOverview() {
        Fragment fragment = HistoryListFragment.newInstance();
        openAsRoot(fragment);
    }


    // DEBUG
    public void startDebugOverview() {
        Fragment fragment = DebugOverviewFragment.newInstance();
        openAsRoot(fragment);
    }



    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------

    /**
     * @return true if the current fragment displayed is a root fragment
     */
    public boolean isRootFragmentVisible() {
        return mFragmentManager.getBackStackEntryCount() <= 1;
    }


    public NavigationListener getNavigationListener() {
        return mNavigationListener;
    }

    public void setNavigationListener(NavigationListener navigationListener) {
        mNavigationListener = navigationListener;
    }
}
