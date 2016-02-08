package com.exarlabs.android.myrules.util.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

import com.exarlabs.android.myrules.model.contact.Contact;

/**
 * Created by atiyka on 2016.02.08..
 */
public class MyRulesSmsManager {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------
    public static final String SMS_SENT = "SMS_SENT";
    public static final String SMS_DELIVERED = "SMS_DELIVERED";

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    private Context mContext;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------
    public MyRulesSmsManager(Context context){
        mContext = context;
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    public void sendSms(Contact contact, String message){
        // when the SMS has been sent
        PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0, new Intent(SMS_SENT), 0);
        mContext.registerReceiver(SmsSentBroadcastReceiver.getInstance(), new IntentFilter(SMS_SENT));

        // when the SMS has been delivered
        PendingIntent deliveryPI = PendingIntent.getBroadcast(mContext, 0, new Intent(SMS_DELIVERED), 0);
        mContext.registerReceiver(SmsDeliveredBroadcastReceiver.getInstance(), new IntentFilter(SMS_DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(contact.getNumber(), null, message, sentPI, deliveryPI);
    }
    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
