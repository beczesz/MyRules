package com.exarlabs.android.myrules.ui.conditions.plugins.contact;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.model.contact.ContactRow;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.conditions.ConditionPluginFragment;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Displays the settings for selecting a group of contacts.
 * Created by becze on 1/28/2016.
 */
public class ContactIsInGroupConditionPluginFragment extends ConditionPluginFragment{

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------
    
    /**
     * @return newInstance of ContactIsInGroupConditionPluginFragment
     */
    public static ContactIsInGroupConditionPluginFragment newInstance() {
        return new ContactIsInGroupConditionPluginFragment();
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    @Inject
    public NavigationManager mNavigationManager;
    
    @Bind(R.id.selected_contacts)
    public EditText mSelectedContacts;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerManager.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plugin_contact_is_in_group_layout, null);
    }


    @Override
    protected void init(RuleCondition condition) {

    }

    @Override
    protected void refreshUI() {

    }

    @Override
    protected void saveChanges() {

    }

    @OnClick(R.id.select_contact_button)
    public void selectContacts() {
        mNavigationManager.startContactsSelectorFragment(contacts -> {
            StringBuilder contactsToString = new StringBuilder();
            for (ContactRow contactRow : contacts) {
                contactsToString.append(contactRow + "\n");
            }

            mSelectedContacts.setText(contactsToString.toString());
        });

    }
    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
