package com.exarlabs.android.myrules.ui;

import java.util.Set;

import android.os.Handler;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.RxPermissions;

/**
 * Fragment which deals with data which need runtime permission check.
 * Created by becze on 2/5/2016.
 */
public abstract class PermissionCheckerFragment extends BaseFragment {


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
     * Callback implmentation when the permission is granted
     */
    protected abstract void onPermissionGranted();

    /**
     * Callback when a permission is declined
     */
    protected abstract void onPermissionDeclined();

    /**
     * Checks if all the necessary permission is granted for this component
     */
    protected void checkPermissions() {
        Set<String> permissionsSet = getRequiredPermissions();
        String[] permissions = permissionsSet != null ? permissionsSet.toArray(new String[permissionsSet.size()]) : new String[0];

        /*
         * Check if the permission are granted or not.
         */
        //@formatter:off
        if (permissions.length > 0) {
            // Must be done during an initialization phase like onCreate
            RxPermissions.getInstance(getActivity())
                            .request( permissions)
                            .doOnError(throwable ->  {
                                throwable.printStackTrace();
                                Toast.makeText(getActivity(), R.string.message_error_permission_denied, Toast.LENGTH_SHORT).show();
                                onPermissionDeclined();
                            })
                            .doOnCompleted(() ->  new Handler().postDelayed(() -> onPermissionGranted(), 100))
                            .subscribe(isGranted -> {if (!isGranted) onPermissionDeclined();});

            //@formatter:on
        } else {
            // we don't have any permissions
            onPermissionGranted();
        }
    }


    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
