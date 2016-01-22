package com.exarlabs.android.myrules.business.event.plugins.sms;

import javax.inject.Inject;

import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.event.Event;
import com.exarlabs.android.myrules.business.event.EventFactory;
import com.exarlabs.android.myrules.business.event.EventHandlerPlugin;

/**
 * Just a timer which displatches an event in every second.
 * Created by becze on 1/11/2016.
 */
public class SmsEventHandlerPlugin extends EventHandlerPlugin implements OnSmsReceivedListener{

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------
    private static final String TAG = SmsEventHandlerPlugin.class.getSimpleName();

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private IncomingSmsListener smsListener;

    @Inject
    public Context mContext;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public SmsEventHandlerPlugin(){

    }
    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------
    @Override
    protected boolean initPlugin() {
        DaggerManager.component().inject(this);

        smsListener = new IncomingSmsListener();
        smsListener.setOnSmsReceivedListener(this);

        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        // since SMS_RECEIVED is sent via ordered broadcast, we have to make sure that we
        // receive this with highest priority in case a receiver aborts the broadcast!
        intentFilter.setPriority(1000000);
        mContext.registerReceiver(smsListener, intentFilter);
        return true;
    }

    @Override
    public void getSms(String sender, String message) {
        SmsEvent event = (SmsEvent) EventFactory.create(Event.Type.RULE_EVENT_SMS);
        event.setSender(sender);
        event.setMessage(message);
        dispatchEvent(event);

        Log.w(TAG, "SMS from: " + sender);
        Log.w(TAG, "SMS text: " + message);
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
