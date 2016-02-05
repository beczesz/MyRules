package com.exarlabs.android.myrules.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.norbsoft.typefacehelper.TypefaceHelper;

import butterknife.ButterKnife;

/**
 * Base class for every Fragment, it provides some custom behavior for all of the fragments
 * sucn as dependency injection.
 * Created by becze on 9/21/2015.
 */
public class BaseFragment extends Fragment {

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

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        TypefaceHelper.typeface(view);
        //initActionBar(true, getString(R.string.app_name));
    }

    @Override
    public void onResume() {
        super.onResume();

        // On each fragment on resume we try to hide the heaborad
        hideKeyboard();
    }


    public void onDestroy() {
        super.onDestroy();
        Log.w("BSZ", "onDestroy: " + this);
    }


    /**
     * Initializes the ActionBar
     *
     * @param title
     */
    public void initActionBarWithHomeButton(String title) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ActionBar supportActionBar = ((BaseActivity) getActivity()).getSupportActionBar();
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setTitle(title);
        }
    }

    public void initActionBarWithBackButton(String title) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ActionBar supportActionBar = ((BaseActivity) getActivity()).getSupportActionBar();
            supportActionBar.setHomeButtonEnabled(true);

            supportActionBar.setTitle(title);
        }
    }

    /**
     * Enables the home indicator
     *
     * @param isEnabled
     */
    public void setHomeAsUpEnabled(boolean isEnabled) {

        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ActionBar supportActionBar = ((BaseActivity) getActivity()).getSupportActionBar();
            supportActionBar.setHomeButtonEnabled(isEnabled);

        }
    }

    /**
     * Shows and hides the actionbar
     *
     * @param isShown
     */
    protected void showActionbar(boolean isShown) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ActionBar supportActionBar = ((BaseActivity) getActivity()).getSupportActionBar();
            if (isShown) {
                supportActionBar.show();
            } else {
                supportActionBar.hide();
            }
        }
    }


    /**
     * Hides the keyboard
     */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View currentFocus = getActivity().getCurrentFocus();
        if (currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }
    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
