package com.exarlabs.android.myrules.ui.util.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exarlabs.android.myrules.model.contact.Contact;
import com.exarlabs.android.myrules.ui.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Displays a list of contacts with the option to remove them, and add more to them.
 * Created by becze on 2/4/2016.
 */
public class ContactGroupFlowLayout extends FlowLayout {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    public class ContactViewHolder {

        @Bind (R.id.contact_name)
        public TextView mItemText;

        private Contact mContact;
        public final LinearLayout contactView;

        public ContactViewHolder(Contact contact) {

            // Inflate a view
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            contactView = (LinearLayout) inflater.inflate(R.layout.contact_view, null);
            contactView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            ButterKnife.bind(this, contactView);

            mContact = contact;
            mItemText.setText(contact.toHumanReadableString());
        }

        @OnClick (R.id.remove_icon)
        public void removeContact() {
            remove(mContact);
            refreshLayout();
        }
    }


    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private List<Contact> mContacts;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public ContactGroupFlowLayout(Context context) {
        super(context);
        init();
    }


    public ContactGroupFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContactGroupFlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    /**
     * Regenerates all the views in the latyout
     */
    public void refreshLayout() {
        // remove all children
        removeAllViews();

        for (Contact contact : mContacts) {
            ContactViewHolder contactView = new ContactViewHolder(contact);
            addView(contactView.contactView);
        }
    }


    private void init() {
        mContacts = new ArrayList<>();
    }

    public boolean add(Contact object) {
        return mContacts.add(object);
    }

    public boolean addAll(List<Contact> contacts) {
        return mContacts.addAll(contacts);
    }

    public boolean remove(Contact contact) {
        return mContacts.remove(contact);
    }

    public void clear() {
        mContacts.clear();
    }

    public boolean contains(Contact contact) {
        return mContacts.contains(contact);
    }

    public Contact remove(int location) {
        return mContacts.remove(location);
    }

    public int size() {
        return mContacts.size();
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------


    public List<Contact> getContacts() {
        return mContacts;
    }
}
