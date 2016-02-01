package com.exarlabs.android.myrules.business.rule.action.plugins;

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

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.RuleComponentProperty;
import com.exarlabs.android.myrules.business.rule.action.ActionPlugin;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.business.rule.event.plugins.call.CallEvent;
import com.exarlabs.android.myrules.business.rule.event.plugins.sms.SmsEvent;

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

    public SendSmsActionPlugin(int type){
        super(type);
        DaggerManager.component().inject(this);
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public void initialize(List<? extends RuleComponentProperty> properties) {
        super.initialize(properties);
        if(getProperty(KEY_PHONE_NUMBER) != null)
            mPhoneNumber = getProperty(KEY_PHONE_NUMBER).getValue();

        if(getProperty(KEY_MESSAGE) != null)
            mMessage = getProperty(KEY_MESSAGE).getValue();
    }

    @Override
    public boolean run(Event event) {
        // reply to sender
        if(event.getType() == Event.Type.RULE_EVENT_SMS) {
            SmsEvent smsEvent = (SmsEvent) event;
            sendSMS(smsEvent.getSender(), "I have got! :)");

        }else if(event.getType() == Event.Type.RULE_EVENT_CALL){
            CallEvent callEvent = (CallEvent) event;
            sendSMS(callEvent.getCaller(), mMessage);

        }else if(mPhoneNumber == null){
            Log.w(TAG, "The phone number is null");
            return false;
        }

        //sendSMS(mPhoneNumber, mMessage);
        return true;
    }

    /**
     * Sends an SMS to the given phone number, with the specified value
     * @param phoneNumber
     * @param message
     */
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

    @Override
    public String[] getRequiredPermissions() {
        return new String[] { android.Manifest.permission.SEND_SMS};
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

    @Override
    public String toString() {
        return TAG;
    }
}
