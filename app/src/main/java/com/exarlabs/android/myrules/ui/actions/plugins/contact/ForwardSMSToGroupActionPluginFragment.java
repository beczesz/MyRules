package com.exarlabs.android.myrules.ui.actions.plugins.contact;

import java.util.List;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.action.plugins.sms.ForwardSmsActionPlugin;
import com.exarlabs.android.myrules.model.contact.Contact;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.actions.ActionPluginFragment;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by becze on 1/28/2016.
 */
public class ForwardSMSToGroupActionPluginFragment extends ActionPluginFragment {

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
    public static ForwardSMSToGroupActionPluginFragment newInstance() {
        return new ForwardSMSToGroupActionPluginFragment();
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    @Inject
    public NavigationManager mNavigationManager;

    @Bind (R.id.selected_contacts)
    public TextView mSelectedContacts;

    @Bind (R.id.select_group_layout)
    public LinearLayout mSelectGroupLayout;

    @Bind (R.id.select_contact_button)
    public Button mSelecButton;

    private List<Contact> mSelectedContactsList;

    private RuleAction mAction;
    private ForwardSmsActionPlugin mPlugin;


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
        return inflater.inflate(R.layout.plugin_action_forward_sms_to_group_layout, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUI();
    }

    @Override
    protected void init(RuleAction action) {
        mAction = action;

          /*
         * Check if the condition has the right mPlugin type, and we are in edit mode
         */
        if (action.getActionPlugin() instanceof ForwardSmsActionPlugin) {
            mPlugin = (ForwardSmsActionPlugin) action.getActionPlugin();
            mSelectedContactsList = mPlugin.getContacts();
        }
    }

    @Override
    protected void refreshUI() {
        StringBuilder contactsToString = new StringBuilder();
        for (Contact contactRow : mSelectedContactsList) {
            contactsToString.append(contactRow + "\n");
        }

        mSelectedContacts.setText(contactsToString.toString());
    }

    @Override
    protected boolean saveChanges() {
        // in edit mode, if the plugin is built with another type, it should be regenerate the plugin, to be able to set the values
        if (mAction.getId() != null) {
            mPlugin = (ForwardSmsActionPlugin) mAction.reGenerateActionPlugin();
        }
        mPlugin.setContacts(mSelectedContactsList);

        return true;
    }

    @OnClick (R.id.select_contact_button)
    public void selectContacts() {
        mNavigationManager.startContactsSelectorFragment(contacts -> {
            mSelectedContactsList = contacts;
            refreshUI();
        });

    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
