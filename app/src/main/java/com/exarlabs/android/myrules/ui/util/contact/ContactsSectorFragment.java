package com.exarlabs.android.myrules.ui.util.contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.exarlabs.android.myrules.model.contact.ContactRow;
import com.exarlabs.android.myrules.ui.BaseFragment;
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
public class ContactsSectorFragment extends BaseFragment {
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
        void onContactsSelected(List<ContactRow> contacts);
    }

    public static class ContactsRowViewHolder {
        public ContactsRowViewHolder(View v) {
            ButterKnife.bind(this, v);
        }

        @Bind(R.id.contact_id)
        TextView mContactId;

        @Bind(R.id.contact_name)
        TextView mContactName;

        @Bind(R.id.contact_number)
        TextView mContactNumber;

        @Bind(R.id.contact_selected_cb)
        CheckBox mContactSelectedCB;
    }

    public class ContactAdapter extends ArrayAdapter<ContactRow> {

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

            ContactRow contactRow = getItem(position);
            viewHolder.mContactId.setText(Long.toString(contactRow.getId()));
            viewHolder.mContactName.setText(contactRow.getName());
            viewHolder.mContactNumber.setText(contactRow.getNumber());
            viewHolder.mContactSelectedCB.setChecked(isSelected(contactRow));
            return convertView;
        }

    }

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final int PERMISSIONS_REQUEST_CONTACTS = 100;

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

    private List<ContactRow> mSelectedContacts;

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
        mSelectedContacts = new ArrayList<>();

        checkPermission();
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
                mNavigationManager.navigateBack(getActivity());
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

        mContactsList.setAdapter(mContactAdapter);

        //@formatter:off
        RxTextView.textChangeEvents(mContactsFilterEditText)
                        .debounce(MyRulesConstants.DELAY_BEFORE_PROCESSING, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(textChangedEvent -> mContactAdapter.getFilter().filter(textChangedEvent.text().toString()));
        //@formatter:on

    }

    private void queryContacts() {
        List<ContactRow> contacts = mPhoneContactManager.getContacts();
        mContactAdapter.clear();
        mContactAdapter.addAll(contacts);
        mContactAdapter.notifyDataSetChanged();
    }


    private void notifyContactSelectorListener(List<ContactRow> contacts) {
        if (mContactsSelectorListener != null) {
            mContactsSelectorListener.onContactsSelected(contacts);
        }
    }

    @OnItemClick(R.id.contact_list)
    void onItemClick(int position) {

        ContactRow contactRow = mContactAdapter.getItem(position);
        if (mSelectedContacts.contains(contactRow)) {
            mSelectedContacts.remove(contactRow);
        } else {
            mSelectedContacts.add(contactRow);
        }

        // update the fragment's title
        String title = getString(R.string.contact_selected) + " " + mSelectedContacts.size();
        initActionBar(true, title);

        // notify the adapter to update the items
        mContactAdapter.notifyDataSetChanged();
    }


    /**
     * Returns true if the contact row is selected
     *
     * @param contactRow
     * @return
     */
    private boolean isSelected(ContactRow contactRow) {
        return mSelectedContacts.contains(contactRow);
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

    // ------------------------------------------------------------------------
    // TEMP
    // ------------------------------------------------------------------------

    /**
     * Checks if the user granted access to use fine location data.
     * If not then we have to force the user to enable it for us,
     */
    protected void checkPermission() {
        if (!isPermissionGiven(Manifest.permission.READ_CONTACTS)) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CONTACTS)) {

                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Please give access to the contacts");
                builder.setPositiveButton(android.R.string.ok, (dialog, id) -> {
                    requestPermission(Manifest.permission.READ_CONTACTS, PERMISSIONS_REQUEST_CONTACTS);
                });
                builder.setCancelable(false);
                // Create the AlertDialog and sho it.
                builder.create().show();

            } else {
                requestPermission(Manifest.permission.READ_CONTACTS, PERMISSIONS_REQUEST_CONTACTS);
            }
        } else {
            queryContacts();
        }
    }


    public boolean isPermissionGiven(String permission) {
        return ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Request the location permission
     */
    private void requestPermission(String permission, int code) {
        // The callback method gets the result of the request.
        ActivityCompat.requestPermissions(getActivity(), new String[] { permission }, code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CONTACTS: {
                queryContacts();
            }

        }
    }
}
