package com.exarlabs.android.myrules.business.rule.event.plugins.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Listens for the incoming sms messages
 *
 * Created by atiyka on 2016.01.21..
 */
public class IncomingSmsListener extends BroadcastReceiver{
    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = IncomingSmsListener.class.getSimpleName();

    // ------------------------------------------------------------------------
    // STATIC INITIALIZERS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private OnSmsReceivedListener mOnSmsReceivedListener;

    // ------------------------------------------------------------------------
    // INITIALIZERS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------


    public void setOnSmsReceivedListener(OnSmsReceivedListener onSmsReceivedListener) {
        this.mOnSmsReceivedListener = onSmsReceivedListener;
    }

    /**
     * It will be executed when you get an SMS
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            // TODO: to discuss: getMessagesFromIntent() -> requires API level 19
            // another example: http://androidexample.com/Incomming_SMS_Broadcast_Receiver_-_Android_Example/index.php?view=article_discription&aid=62
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                String from = smsMessage.getOriginatingAddress();
                String msgBody = smsMessage.getMessageBody();

                if(mOnSmsReceivedListener != null)
                    mOnSmsReceivedListener.getSms(from, msgBody);
                else Log.w(TAG, "error");
            }
        }
    }
}
