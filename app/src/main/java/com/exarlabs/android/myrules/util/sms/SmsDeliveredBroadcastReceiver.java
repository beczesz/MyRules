package com.exarlabs.android.myrules.util.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Listens for the delivered SMSes and logs the corresponding info.
 *
 * Created by atiyka on 2016.02.08..
 */


public class SmsDeliveredBroadcastReceiver extends BroadcastReceiver{
    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------
    private static class Holder {
        static final SmsDeliveredBroadcastReceiver INSTANCE = new SmsDeliveredBroadcastReceiver();
    }

    private static final String TAG = SmsDeliveredBroadcastReceiver.class.getSimpleName();

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------
    public static SmsDeliveredBroadcastReceiver getInstance() {
        return Holder.INSTANCE;
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------
    private SmsDeliveredBroadcastReceiver() {

    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                Log.w(TAG, "SMS delivered");
                break;
            case Activity.RESULT_CANCELED:
                Log.w(TAG, "SMS not delivered");
                break;
        }
        context.unregisterReceiver(this);
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
