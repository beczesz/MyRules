package com.exarlabs.android.myrules.business.rule.event.plugins.call;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import android.Manifest;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.event.EventHandlerPlugin;
import com.exarlabs.android.myrules.business.rule.event.EventPluginManager;
import com.exarlabs.android.myrules.model.contact.Contact;

/**
 * The plugin converts the incoming call event to a CallEvent
 *
 * Created by atiyka on 1/22/2016.
 */
public class CallEventHandlerPlugin extends EventHandlerPlugin implements OnIncomingCallListener {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------
    private static final String TAG = CallEventHandlerPlugin.class.getSimpleName();

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

//    private IncomingCallListener callListener;

    @Inject
    public Context mContext;

    @Inject
    public EventPluginManager mEventPluginManager;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------
    @Override
    protected boolean initPlugin() {
        DaggerManager.component().inject(this);

        CallStateListener callStateListener = new CallStateListener();
        callStateListener.setOnIncomingCallListener(this);

        // setting to listen for the incoming calls
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        return true;
    }

    @Override
    public void onIncomingCall(Contact caller) {
        CallEvent event = createNewEvent();
        event.setContact(caller);

        dispatchEvent(event);
        Log.w(TAG, "Call from: " + caller);
    }

    @Override
    public Set<String> getRequiredPermissions() {
        HashSet<String> permissions = new HashSet<>();
        permissions.add(Manifest.permission.READ_PHONE_STATE);
        return permissions;

    }


    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
