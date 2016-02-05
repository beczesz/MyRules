package com.exarlabs.android.myrules.model.contact;

import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;

/**
 * Represents a contact from the phone
 * Created by becze on 1/28/2016.
 */
public class Contact {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    /**
     * The id of the contact given by the content provider
     * ContactsContract.CommonDataKinds.Phone._ID;
     */
    private long mId;

    /**
     * The phone number of the contact
     * ContactsContract.CommonDataKinds.Phone.NUMBER;
     */
    private String mNumber;


    /**
     * The name of the contact
     * ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY;
     */
    private String mName;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------


    public Contact(long id) {
        mId = id;
    }

    public Contact(String number) {
        mNumber = number;
    }

    public Contact(long id, String number) {
        mId = id;
        mNumber = number;
    }

    public Contact(long id, String name, String number) {
        this.mId = id;
        this.mName = name;
        this.mNumber = number;
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        String contactToString = mId != -1 ? Long.toString(mId) : "";
        contactToString += !TextUtils.isEmpty(mName) ? mName : "";
        contactToString += !TextUtils.isEmpty(mNumber) ? " (" + mNumber + ") " : "";
        return contactToString;
    }

    /**
     * @return a human readable name
     */
    public String toHumanReadableString() {

        if (!TextUtils.isEmpty(mName)) {
            return mName;
        } else if (!TextUtils.isEmpty(mNumber)) {
            return mNumber;
        } else {
            return Long.toString(mId);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Contact) {
            Contact contact = (Contact) o;

            // Compare first the id of the contact if we have two valid ids
            if (contact.getId() != -1 && getId() != -1) {
                return contact.getId() == getId();
            } else if (!TextUtils.isEmpty(contact.getNumber()) && !TextUtils.isEmpty(getNumber())) {
                // compare the numbers
                String number = contact.getNumber();
                String currentNumber = getNumber();
                return PhoneNumberUtils.compare(number, currentNumber);
            } else if (!TextUtils.isEmpty(contact.getName()) && !TextUtils.isEmpty(getName())) {
                return contact.getNumber().equals(getName());
            }
        }

        return super.equals(o);
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------


    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        this.mNumber = number;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

}
