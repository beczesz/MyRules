package com.exarlabs.android.myrules.ui.actions.plugins.contact;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.model.contact.ContactRow;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.actions.ActionPluginFragment;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by becze on 1/28/2016.
 */
public class SendSMSToGroupActionPlugin extends ActionPluginFragment {

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
     * @return newInstance of SendSMSToGroupActionPlugin
     */
    public static SendSMSToGroupActionPlugin newInstance() {
        return new SendSMSToGroupActionPlugin();
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    @Inject
    public NavigationManager mNavigationManager;

    @Bind(R.id.selected_contacts)
    public TextView mSelectedContacts;

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
        return inflater.inflate(R.layout.plugin_action_send_sms_to_group_layout, null);
    }

    @Override
    protected void init(RuleAction action) {

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

            String text = contactsToString.toString();
            Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        });

    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
