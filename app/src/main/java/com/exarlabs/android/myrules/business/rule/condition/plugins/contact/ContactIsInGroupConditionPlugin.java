package com.exarlabs.android.myrules.business.rule.condition.plugins.contact;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.exarlabs.android.myrules.business.rule.RuleComponentProperty;
import com.exarlabs.android.myrules.business.rule.condition.ConditionPlugin;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.business.rule.event.plugins.ContactEvent;
import com.exarlabs.android.myrules.model.contact.Contact;
import com.exarlabs.android.myrules.model.dao.RuleConditionProperty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * Checks whether the contact from the event is member of a predefined group.
 * Created by becze on 1/28/2016.
 */
public class ContactIsInGroupConditionPlugin extends ConditionPlugin {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = ContactIsInGroupConditionPlugin.class.getSimpleName();

    private static final String KEY_GROUP_SELECTION = "GROUP_SELECTION";

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    /**
     * This is the list of contacts in this group
     */
    private List<Contact> mContactRows;
    private final Gson mGson;
    private final Type mDatasetListType;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public ContactIsInGroupConditionPlugin() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        mGson = builder.create();

        mDatasetListType = new TypeToken<List<Contact>>() {}.getType();

        mContactRows = new ArrayList<>();
    }


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------


    @Override
    public void initialize(List<? extends RuleComponentProperty> properties) {
        super.initialize(properties);


        try {
            RuleConditionProperty groupJSON = getProperty(KEY_GROUP_SELECTION);
            if (groupJSON != null) {
                mContactRows = mGson.fromJson(groupJSON.getValue(), mDatasetListType);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean evaluate(Event event) {
        if (event instanceof ContactEvent) {
            return mContactRows.contains(((ContactEvent) event).getContact());
        }
        return false;
    }

    @Override
    public Set<String> getRequiredPermissions() {
        HashSet<String> permissions = new HashSet<>();
        permissions.add(android.Manifest.permission.READ_CONTACTS);
        return permissions;
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------


    public List<Contact> getContactRows() {
        return mContactRows;
    }

    public void setContactRows(List<Contact> contactRows) {
        mContactRows = contactRows;
        saveProperty(KEY_GROUP_SELECTION, mGson.toJson(mContactRows, mDatasetListType));
    }
}
