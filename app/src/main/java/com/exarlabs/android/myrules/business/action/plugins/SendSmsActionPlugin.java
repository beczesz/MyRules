package com.exarlabs.android.myrules.business.action.plugins;

import java.util.List;

import javax.inject.Inject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;

import com.exarlabs.android.myrules.business.action.ActionPlugin;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.event.Event;
import com.exarlabs.android.myrules.model.dao.RuleActionProperty;

/**
 * Action plugin which sends a given text to a given phone number as an SMS
 * Created by atiyka on 1/20/2016.
 */
public class SendSmsActionPlugin extends ActionPlugin {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = SendSmsActionPlugin.class.getSimpleName();
    private static final String KEY_PHONE_NUMBER = "PHONE_NUMBER";
    private static final String KEY_MESSAGE = "MESSAGE";

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    private String mPhoneNumber;
    private String mMessage;

    @Inject
    public Context mContext;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public SendSmsActionPlugin(){
        DaggerManager.component().inject(this);
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------
    @Override
    public void initialize(List<RuleActionProperty> properties) {
        super.initialize(properties);
        mPhoneNumber = getProperty(KEY_PHONE_NUMBER).getValue();
        mMessage = getProperty(KEY_MESSAGE).getValue();
    }

    @Override
    public boolean run(Event event) {
        if(mPhoneNumber == null){
            Log.w(TAG, "The phone number is null");
            return false;
        }
        sendSMS(mPhoneNumber, mMessage);
        return true;
    }

    // sends an SMS to the given phone number, with the specified value
    public void sendSMS(String phoneNumber, String message)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        Log.w(TAG, "Phone number: " + phoneNumber);
        Log.w(TAG, "Msg: " + message);

        PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0, new Intent(SENT), 0);

        PendingIntent deliveryPI = PendingIntent.getBroadcast(mContext, 0, new Intent(DELIVERED), 0);

        // when the SMS has been sent
        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
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
            }
        }, new IntentFilter(SENT));

        // when the SMS has been delivered
        mContext.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Log.w(TAG, "SMS delivered");
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.w(TAG, "SMS not delivered");
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveryPI);
    }


    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------

    public String getPhoneNumber() {
        return mPhoneNumber;
    }
    public String getMessage() {
        return mMessage;
    }

    public void setPhoneNumber(String phoneNumber) {
        saveProperty(KEY_PHONE_NUMBER, phoneNumber);
        this.mPhoneNumber = phoneNumber;
    }
    public void setMessage(String message) {
        saveProperty(KEY_MESSAGE, message);
        this.mMessage = message;
    }
}
