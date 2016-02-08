package com.exarlabs.android.myrules.util.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Listens for the sent SMSes and logs the corresponding info.
 *
 * Created by atiyka on 2016.02.08..
 */


public class SmsSentBroadcastReceiver extends BroadcastReceiver{
    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------
    private static class Holder {
        static final SmsSentBroadcastReceiver INSTANCE = new SmsSentBroadcastReceiver();
    }

    private static final String TAG = SmsSentBroadcastReceiver.class.getSimpleName();

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------
    public static SmsSentBroadcastReceiver getInstance() {
        return Holder.INSTANCE;
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------
    private SmsSentBroadcastReceiver() {

    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                Log.w(TAG, "SMS sent");
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                Log.w(TAG, "Generic failure");
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                Log.w(TAG, "No service");
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                Log.w(TAG, "Null PDU");
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                Log.w(TAG, "Radio off");
                break;
        }
        context.unregisterReceiver(this);
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
