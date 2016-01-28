package com.exarlabs.android.myrules.business.provider;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.exarlabs.android.myrules.model.contact.ContactRow;

/**
 * Manages the access to the phone's contacts.
 * Created by becze on 1/28/2016.
 */
public class PhoneContactManager {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String COL_ID = ContactsContract.CommonDataKinds.Phone._ID;
    private static final String COL_NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
    private static final String COL_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

    private static final Uri PHONES_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    private static final String[] SELECTED_COLUMNS = { COL_ID, COL_NAME, COL_NUMBER };

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

    public PhoneContactManager(Context context) {
        mContext = context;
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    /**
     * @return the list of contacts from the phone.
     * Note: call it from a background thread.
     */
    public List<ContactRow> getContacts() {

        List<ContactRow> result = new ArrayList<>();

        // TODO optimize based on search pattern
        // Prepare the content resolver and the query
        String searchPattern = "";
        String[] selectionArgs = { "%" + searchPattern + "%" };
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor contactsCursor = contentResolver.query(PHONES_URI, SELECTED_COLUMNS, COL_NAME + " LIKE ?", selectionArgs, COL_NAME + " " + "ASC");

        // Create the list of contacts
        while (contactsCursor.moveToNext()) {
            result.add(new ContactRow(contactsCursor.getLong(contactsCursor.getColumnIndex(COL_ID)),
                            contactsCursor.getString(contactsCursor.getColumnIndex(COL_NAME)),
                            contactsCursor.getString(contactsCursor.getColumnIndex(COL_NUMBER))));
        }

        contactsCursor.close();

        return result;
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
