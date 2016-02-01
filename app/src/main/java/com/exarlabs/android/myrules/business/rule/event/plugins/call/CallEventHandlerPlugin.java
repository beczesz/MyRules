package com.exarlabs.android.myrules.business.rule.event.plugins.call;

import javax.inject.Inject;

import android.Manifest;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.business.rule.event.EventFactory;
import com.exarlabs.android.myrules.business.rule.event.EventHandlerPlugin;

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

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------


    public CallEventHandlerPlugin() {
        super(Event.Type.RULE_EVENT_CALL);
    }

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
    public void getCall(String caller) {
        CallEvent event = (CallEvent) EventFactory.create(Event.Type.RULE_EVENT_CALL);
        event.setCaller(caller);

        dispatchEvent(event);
        Log.w(TAG, "Call from: " + caller);
    }

    @Override
    public String[] getRequiredPermissions() {
        return new String[] { Manifest.permission.READ_PHONE_STATE};
    }


    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
