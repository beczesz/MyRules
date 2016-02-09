package com.exarlabs.android.myrules.ui.actions.plugins.contact;

import java.util.List;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.action.plugins.sms.SendSmsActionPlugin;
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
public class SendSMSToGroupActionPluginFragment extends ActionPluginFragment {

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
    public static SendSMSToGroupActionPluginFragment newInstance() {
        return new SendSMSToGroupActionPluginFragment();
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    @Inject
    public NavigationManager mNavigationManager;

    @Bind (R.id.selected_contacts)
    public TextView mSelectedContacts;

    @Bind (R.id.radio_group)
    public RadioGroup mRadioGroup;

    @Bind (R.id.select_group_layout)
    public LinearLayout mSelectGroupLayout;

    @Bind (R.id.sms_template_edit_text)
    public EditText mSMSTemplateEditText;

    @Bind (R.id.select_contact_button)
    public Button mSelecButton;

    private List<Contact> mSelectedContactsList;
    private boolean mIdSendToContactFromEvent;
    private String mSmsTemplate;

    private RuleAction mAction;
    private SendSmsActionPlugin mPlugin;


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
        if (action.getActionPlugin() instanceof SendSmsActionPlugin) {
            mPlugin = (SendSmsActionPlugin) action.getActionPlugin();
            mSelectedContactsList = mPlugin.getContacts();
            mIdSendToContactFromEvent = mPlugin.isSendToContactFromEvent();
            mSmsTemplate = mPlugin.getMessage();
        }
    }

    @Override
    protected void refreshUI() {
        StringBuilder contactsToString = new StringBuilder();
        for (Contact contactRow : mSelectedContactsList) {
            contactsToString.append(contactRow + "\n");
        }

        mSelectedContacts.setText(contactsToString.toString());
        // select the radio
        mRadioGroup.check(mIdSendToContactFromEvent ? R.id.radio_send_to_contact_from_event : R.id.radio_send_to_group);

        // set (en/dis)abled the select contacts button
        mSelecButton.setEnabled(!mIdSendToContactFromEvent);

        // set the saved text in edit mode
        if (mAction.getId() != null)
            mSMSTemplateEditText.setText(mSmsTemplate);
    }

    @Override
    protected boolean saveChanges() {
        // in edit mode, if the plugin is built with another type, it should be regenerate the plugin, to be able to set the values
        if (mAction.getId() != null) {
            mPlugin = (SendSmsActionPlugin) mAction.reGenerateActionPlugin();
        }

        mPlugin.setContacts(mSelectedContactsList);
        mPlugin.setMessage(mSMSTemplateEditText.getText().toString().trim());
        mPlugin.setSendToContactFromEvent(mIdSendToContactFromEvent);
        return true;
    }

    @OnClick (R.id.select_contact_button)
    public void selectContacts() {
        mNavigationManager.startContactsSelectorFragment(contacts -> {
            mSelectedContactsList = contacts;
            refreshUI();
        });

    }

    @OnClick ({ R.id.radio_send_to_contact_from_event, R.id.radio_send_to_group })
    public void selectionChanged() {
        mSelectGroupLayout.setEnabled(false);
        mIdSendToContactFromEvent = false;
        mSelecButton.setEnabled(false);

        switch (mRadioGroup.getCheckedRadioButtonId()) {
            case R.id.radio_send_to_contact_from_event:
                mIdSendToContactFromEvent = true;
                break;

            case R.id.radio_send_to_group:
                mSelectGroupLayout.setEnabled(true);
                mSelecButton.setEnabled(true);
                break;
        }
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
