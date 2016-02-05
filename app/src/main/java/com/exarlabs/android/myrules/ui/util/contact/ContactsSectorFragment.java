package com.exarlabs.android.myrules.ui.util.contact;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.exarlabs.android.myrules.business.MyRulesConstants;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.provider.PhoneContactManager;
import com.exarlabs.android.myrules.model.contact.Contact;
import com.exarlabs.android.myrules.ui.PermissionCheckerFragment;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;
import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Lets the user to select some contacts. When the user is finished it notifies back the
 * originator fragment.
 *
 */
public class ContactsSectorFragment extends PermissionCheckerFragment {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    /**
     * Listener interface for selecting multiple contacts.
     */
    public interface ContactsSelectorListener {

        /**
         * Callback method for the selected contacts.
         *
         * @param contacts
         */
        void onContactsSelected(List<Contact> contacts);
    }

    public static class ContactsRowViewHolder {
        public ContactsRowViewHolder(View v) {
            ButterKnife.bind(this, v);
        }

        @Bind(R.id.contact_name)
        TextView mContactName;

        @Bind(R.id.contact_number)
        TextView mContactNumber;

        @Bind(R.id.contact_selected_cb)
        CheckBox mContactSelectedCB;
    }

    public class ContactAdapter extends ArrayAdapter<Contact> {

        private final Context mContext;

        public ContactAdapter(Context context) {
            super(context, R.layout.contact_row_layout);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ContactsRowViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.contact_row_layout, null);
                viewHolder = new ContactsRowViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            viewHolder = (ContactsRowViewHolder) convertView.getTag();

            Contact contactRow = getItem(position);
            viewHolder.mContactName.setText(contactRow.getName());
            viewHolder.mContactNumber.setText(contactRow.getNumber());
            viewHolder.mContactSelectedCB.setChecked(isSelected(contactRow));
            return convertView;
        }

    }

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    /**
     * @return newInstance of ContactsSectorFragment
     */
    public static ContactsSectorFragment newInstance() {
        return new ContactsSectorFragment();
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private ContactsSelectorListener mContactsSelectorListener;
    private ContactAdapter mContactAdapter;

    @Inject
    public PhoneContactManager mPhoneContactManager;

    @Inject
    public NavigationManager mNavigationManager;

    @Bind(R.id.contact_list)
    public ListView mContactsList;

    @Bind(R.id.contacts_filter)
    public EditText mContactsFilterEditText;

    private List<Contact> mSelectedContacts = new ArrayList<>();

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public ContactsSectorFragment() {
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerManager.component().inject(this);
        setHasOptionsMenu(true);

        // create a new adapter and add it to the list view
        mContactAdapter = new ContactAdapter(getActivity());

        // check the permissions before we do anything else
        checkPermissions();
    }

    @Override
    protected Set<String> getRequiredPermissions() {
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add(Manifest.permission.READ_CONTACTS);
        return hashSet;
    }

    @Override
    protected void onPermissionGranted() {
        queryContacts();
    }

    @Override
    protected void onPermissionDeclined() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact_selector_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
            case R.id.selection_completed:
                notifyContactSelectorListener(mSelectedContacts);
                goBack();
                break;
        }

        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contacts_selector_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initActionBarWithBackButton(getString(R.string.select_contacts));

        mContactsList.setAdapter(mContactAdapter);

        //@formatter:off
        RxTextView.textChangeEvents(mContactsFilterEditText)
                        .debounce(MyRulesConstants.DELAY_BEFORE_PROCESSING, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(textChangedEvent -> mContactAdapter.getFilter().filter(textChangedEvent.text().toString()));
        //@formatter:on

    }

    private void queryContacts() {
        List<Contact> contacts = mPhoneContactManager.getContacts();
        mContactAdapter.clear();
        mContactAdapter.addAll(contacts);
        mContactAdapter.notifyDataSetChanged();
        mContactAdapter.getFilter().filter("");
    }


    private void notifyContactSelectorListener(List<Contact> contacts) {
        if (mContactsSelectorListener != null) {
            mContactsSelectorListener.onContactsSelected(new ArrayList<>(contacts));
        }
    }

    @OnItemClick(R.id.contact_list)
    void onItemClick(int position) {

        Contact contactRow = mContactAdapter.getItem(position);
        if (mSelectedContacts.contains(contactRow)) {
            mSelectedContacts.remove(contactRow);
        } else {
            mSelectedContacts.add(contactRow);
        }

        // update the fragment's title
        String title = getString(R.string.contact_selected) + " " + mSelectedContacts.size();
        initActionBarWithBackButton(title);

        // notify the adapter to update the items
        mContactAdapter.notifyDataSetChanged();
    }


    /**
     * Returns true if the contact row is selected
     *
     * @param contactRow
     * @return
     */
    private boolean isSelected(Contact contactRow) {
        return mSelectedContacts.contains(contactRow);
    }

    /**
     * Hides the keyboard, and navigates back
     */
    private void goBack() {
        // hide the keyboard
        mNavigationManager.navigateBack(getActivity());
    }


    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------

    public ContactsSelectorListener getContactsSelectorListener() {
        return mContactsSelectorListener;
    }

    public void setContactsSelectorListener(ContactsSelectorListener contactsSelectorListener) {
        mContactsSelectorListener = contactsSelectorListener;
    }

    /**
     * Sets the already selected contacts.
     * @param selectedContacts
     */
    public void setSelectedContacts(List<Contact> selectedContacts) {
        mSelectedContacts = selectedContacts;
    }
}
