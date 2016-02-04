package com.exarlabs.android.myrules.business.rule.action.plugins.sms;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;

import com.exarlabs.android.myrules.business.rule.RuleComponentProperty;
import com.exarlabs.android.myrules.business.rule.action.ActionPlugin;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.business.rule.event.plugins.sms.SmsEvent;
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
public class ForwardSmsActionPlugin extends ActionPlugin {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = ForwardSmsActionPlugin.class.getSimpleName();

    private static final String KEY_GROUP_SELECTION = "GROUP_SELECTION";
    private static final String KEY_MESSAGE = "MESSAGE";
    public static final String SMS_SENT = "SMS_SENT";
    public static final String SMS_DELIVERED = "SMS_DELIVERED";

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




    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public ForwardSmsActionPlugin() {
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
            if (groupJSON != null) {
                mContacts = mGson.fromJson(groupJSON.getValue(), mDatasetListType);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        if (getProperty(KEY_MESSAGE) != null) {
            mMessage = getProperty(KEY_MESSAGE).getValue();
        }
    }

    @Override
    public boolean run(Event event) {
        if (event instanceof SmsEvent) {
            for (Contact contact : mContacts) {
                sendSMS(contact, ((SmsEvent) event).getMessage());
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

        // Make sure that the message is not null
        if (message == null) {
            message = "";
        }

        Log.w(TAG, "Phone number: " + contact.getNumber());
        Log.w(TAG, "Msg: " + message);


        PendingIntent sentPI = PendingIntent.getBroadcast(getContext(), 0, new Intent(SMS_SENT), 0);

        PendingIntent deliveryPI = PendingIntent.getBroadcast(getContext(), 0, new Intent(SMS_DELIVERED), 0);

        // when the SMS has been sent
        getContext().registerReceiver(new BroadcastReceiver() {
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
        }, new IntentFilter(SMS_SENT));

        // when the SMS has been delivered
        getContext().registerReceiver(new BroadcastReceiver() {
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
        }, new IntentFilter(SMS_DELIVERED));

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

    @Override
    public String toString() {
        return TAG;
    }
}
