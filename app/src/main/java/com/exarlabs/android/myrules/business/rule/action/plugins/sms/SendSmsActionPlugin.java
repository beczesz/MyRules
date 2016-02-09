package com.exarlabs.android.myrules.business.rule.action.plugins.sms;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import android.content.Context;
import android.util.Log;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.RuleComponentProperty;
import com.exarlabs.android.myrules.business.rule.action.ActionPlugin;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.business.rule.event.plugins.ContactEvent;
import com.exarlabs.android.myrules.model.contact.Contact;
import com.exarlabs.android.myrules.model.dao.RuleActionProperty;
import com.exarlabs.android.myrules.util.sms.ExarSmsManager;
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
            if (groupJSON != null) {
                mContacts = mGson.fromJson(groupJSON.getValue(), mDatasetListType);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        RuleActionProperty message = getProperty(KEY_MESSAGE);
        mMessage = message != null ? message.getValue() : "";

        RuleActionProperty contact = getProperty(KEY_SEND_TO_CONTACT_FROM_EVENT);
        mSendToContactFromEvent = contact != null && Boolean.parseBoolean(contact.getValue());
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

        // Make sure that the message is not null
        if (message == null) {
            message = "";
        }

        Log.w(TAG, "Phone number: " + contact.getNumber());
        Log.w(TAG, "Msg: " + message);

        ExarSmsManager manager = new ExarSmsManager(getContext());

        manager.sendSms(contact, message);
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
