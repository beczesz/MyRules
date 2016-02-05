package com.exarlabs.android.myrules.ui;

/**
 * Base abstract fragment for displaying a rule component
 * Created by becze on 2/4/2016.
 */
public abstract class RuleComponentDetailsFragment extends PermissionCheckerFragment {

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

    /**
     * Validates the component settings out of which the component can be built up
     *
     * @return true if the settings are valid.
     */
    protected abstract boolean validateComponent();

    /**
     * Callback function which called only if the component is validated and all the permissions are granted.
     */
    protected abstract void onComponentReadyToSave();


    /**
     * Saves the component
     */
    protected void saveComponent() {
        if (validateComponent()) {
            checkPermissions();
        }
    }

    @Override
    protected void onPermissionGranted() {
        onComponentReadyToSave();
    }

    @Override
    protected void onPermissionDeclined() {
        // Leave it empty
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
