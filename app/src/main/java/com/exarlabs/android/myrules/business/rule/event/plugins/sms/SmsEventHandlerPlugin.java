package com.exarlabs.android.myrules.business.rule.event.plugins.sms;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.event.EventHandlerPlugin;

/**
 * The plugin converts the received SMS to an SmsEvent
 * <p>
 * Created by atiyka on 1/21/2016.
 */
public class SmsEventHandlerPlugin extends EventHandlerPlugin implements OnSmsReceivedListener {

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
        SmsEvent event = createNewEvent();
        event.setSender(sender);
        event.setMessage(message);
        dispatchEvent(event);

        Log.w(TAG, "SMS from: " + sender);
        Log.w(TAG, "SMS text: " + message);
    }

    @Override
    public Set<String> getRequiredPermissions() {
        HashSet<String> permissions = new HashSet<>();
        permissions.add(Manifest.permission.RECEIVE_SMS);
        return permissions;
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
