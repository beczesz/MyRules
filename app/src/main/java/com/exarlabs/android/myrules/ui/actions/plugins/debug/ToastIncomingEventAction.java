package com.exarlabs.android.myrules.ui.actions.plugins.debug;

import java.util.HashSet;
import java.util.Set;

import android.util.Log;
import android.widget.Toast;

import com.exarlabs.android.myrules.business.rule.action.ActionPlugin;
import com.exarlabs.android.myrules.business.rule.event.Event;

/**
 * Just toasts and logs the incomming event.
 * Created by becze on 2/4/2016.
 */
public class ToastIncomingEventAction extends ActionPlugin {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = ToastIncomingEventAction.class.getSimpleName();

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
    public Set<String> getRequiredPermissions() {
        return new HashSet<>();
    }

    @Override
    public boolean run(Event event) {
        String toString = event.toString();
        Toast.makeText(getContext(), toString, Toast.LENGTH_SHORT).show();
        Log.w(TAG, toString);
        return false;
    }
    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
