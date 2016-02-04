package com.exarlabs.android.myrules.business.rule.event.plugins.call;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.exarlabs.android.myrules.model.contact.Contact;

/**
 * Listens for the phone state
 *
 * Created by atiyka on 2016.01.22..
 */
public class CallStateListener extends PhoneStateListener {
    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC INITIALIZERS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private OnIncomingCallListener onIncomingCallListener;

    // ------------------------------------------------------------------------
    // INITIALIZERS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------


    /**
     * If somebody calls, forwards the event to the incomingCallListener
     * @param state
     * @param incomingNumber
     */
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        switch(state) {
            case TelephonyManager.CALL_STATE_RINGING:
                Contact contact = new Contact(incomingNumber);
                Log.w("calling", contact.toString());
                onIncomingCallListener.onIncomingCall(contact);
                break;
        }
    }

    public void setOnIncomingCallListener(OnIncomingCallListener onIncomingCallListener) {
        this.onIncomingCallListener = onIncomingCallListener;
    }
}
