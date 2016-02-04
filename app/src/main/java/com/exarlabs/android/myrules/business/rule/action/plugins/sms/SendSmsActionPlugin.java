package com.exarlabs.android.myrules.business.rule.action.plugins.sms;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.exarlabs.android.myrules.business.rule.event.plugins.ContactEvent;
import com.exarlabs.android.myrules.model.contact.Contact;
import com.exarlabs.android.myrules.model.dao.RuleActionProperty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

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

    private static final String KEY_GROUP_SELECTION = "GROUP_SELECTION";
    private static final String KEY_MESSAGE = "MESSAGE";
    private static final String KEY_SEND_TO_CONTACT_FROM_EVENT = "SEND_TO_CONTACT_FROM_EVENT";

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    /**
     * This is the list of contacts in this group
     */
    private List<Contact> mContacts;
    private final Gson mGson;
    private final Type mDatasetListType;
    private String mMessage;

    private boolean mSendToContactFromEvent;

    @Inject
    public Context mContext;


    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public SendSmsActionPlugin() {
        DaggerManager.component().inject(this);

        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        mGson = builder.create();

        mDatasetListType = new TypeToken<List<Contact>>() {}.getType();

        mContacts = new ArrayList<>();
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public void initialize(List<? extends RuleComponentProperty> properties) {
        super.initialize(properties);
        try {
            RuleActionProperty groupJSON = getProperty(KEY_GROUP_SELECTION);
            mContacts = mGson.fromJson(groupJSON.getValue(), mDatasetListType);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        if (getProperty(KEY_MESSAGE) != null) {
            mMessage = getProperty(KEY_MESSAGE).getValue();
        }

        if (getProperty(KEY_SEND_TO_CONTACT_FROM_EVENT) != null) {
            mSendToContactFromEvent = Boolean.parseBoolean(getProperty(KEY_SEND_TO_CONTACT_FROM_EVENT).getValue());
        }
    }

    @Override
    public boolean run(Event event) {
        if (mSendToContactFromEvent) {
            if (event instanceof ContactEvent) {
                sendSMS(((ContactEvent) event).getContact(), mMessage);
            } else {
                Log.e(TAG, "The event must be a ContactEvent");
            }
        } else {
            for (Contact contact : mContacts) {
                sendSMS(contact, mMessage);
            }
        }

        return true;
    }

    /**
     * Sends an SMS to the given phone number, with the specified value
     *
     * @param message
     */
    public void sendSMS(Contact contact, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";


        Log.w(TAG, "Phone number: " + contact.getNumber());
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
        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
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
        sms.sendTextMessage(contact.getNumber(), null, message, sentPI, deliveryPI);
    }

    @Override
    public Set<String> getRequiredPermissions() {
        HashSet<String> permissions = new HashSet<>();
        permissions.add(android.Manifest.permission.SEND_SMS);
        return permissions;
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------


    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        saveProperty(KEY_MESSAGE, message);
        this.mMessage = message;
    }

    public List<Contact> getContacts() {
        return mContacts;
    }

    public void setContacts(List<Contact> contacts) {
        mContacts = contacts;
        saveProperty(KEY_GROUP_SELECTION, mGson.toJson(mContacts, mDatasetListType));
    }

    public boolean isSendToContactFromEvent() {
        return mSendToContactFromEvent;
    }

    public void setSendToContactFromEvent(boolean sendToContactFromEvent) {
        mSendToContactFromEvent = sendToContactFromEvent;
        saveProperty(KEY_SEND_TO_CONTACT_FROM_EVENT, Boolean.toString(sendToContactFromEvent));
    }

    @Override
    public String toString() {
        return TAG;
    }
}
