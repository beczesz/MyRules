package com.exarlabs.android.myrules.model.contact;

/**
 * Represents a contact from the phone
 * Created by becze on 1/28/2016.
 */
public class ContactRow {

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


    public ContactRow(long id, String name, String number) {
        this.mId = id;
        this.mName = name;
        this.mNumber = number;
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return Long.toString(mId) + ", " + mName + ", " + mNumber + "\n";
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
