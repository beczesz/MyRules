package com.exarlabs.android.myrules.ui;

import java.util.Set;

import android.widget.Toast;

import com.exarlabs.android.myrules.business.rx.CallbackSubscriber;
import com.tbruyelle.rxpermissions.RxPermissions;

/**
 * Base abstract fragment for displaying a rule component
 * Created by becze on 2/4/2016.
 */
public abstract class RuleComponentDetailsFragment extends BaseFragment {

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
     * @return the set of needed permissions by this component
     */
    protected abstract Set<String> getRequiredPermissions();

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

    /**
     * Checks if all the necessary permission is granted for this component
     */
    private void checkPermissions() {

        Set<String> permissionsSet = getRequiredPermissions();
        String[] permissions = permissionsSet != null ? permissionsSet.toArray(new String[permissionsSet.size()]) : new String[0];

        /*
         * Check if the permission are granted or not.
         */
        if (permissions.length > 0) {
            //@formatter:off
            // Must be done during an initialization phase like onCreate
            RxPermissions.getInstance(getActivity())
                            .request( permissions)
                            .subscribe(new CallbackSubscriber<Boolean>() {
                                           @Override
                                           public void onResult(Boolean result, Throwable e) {
                                               if (result) {
                                                  onComponentReadyToSave();
                                               } else {
                                                   Toast.makeText(getActivity(), R.string.message_error_permission_denied, Toast.LENGTH_SHORT).show();
                                               }
                                           }
                                       });

            //@formatter:on
        } else {
            // we don't have any permissions
            onComponentReadyToSave();
        }
    }


    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
