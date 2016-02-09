package com.exarlabs.android.myrules.ui.conditions.plugins.contact;

import java.util.ArrayList;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.condition.plugins.contact.ContactIsInGroupConditionPlugin;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.conditions.ConditionPluginFragment;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;
import com.exarlabs.android.myrules.ui.util.ui.ContactGroupFlowLayout;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Displays the settings for selecting a group of contacts.
 * Created by becze on 1/28/2016.
 */
public class ContactIsInGroupConditionPluginFragment extends ConditionPluginFragment {

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

    @Bind (R.id.selected_contacts)
    public ContactGroupFlowLayout mContactGroupFlowLayout;

    private RuleCondition mCondition;
    private ContactIsInGroupConditionPlugin mPlugin;

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

    @Override
    protected void init(RuleCondition condition) {
        mCondition = condition;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plugin_contact_is_in_group_layout, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*
         * Check if the condition has the right mPlugin type, and we are in edit mode
         */
        if (mCondition.getConditionPlugin() instanceof ContactIsInGroupConditionPlugin) {
            mPlugin = (ContactIsInGroupConditionPlugin) mCondition.getConditionPlugin();
            mContactGroupFlowLayout.addAll(mPlugin.getContactRows());
            mContactGroupFlowLayout.refreshLayout();
        }
    }


    @Override
    protected void refreshUI() {
    }

    @Override
    protected boolean saveChanges() {
        // in edit mode, if the plugin is built with another type, it should be regenerate the plugin, to be able to set the values
        if(mCondition.getId() != null) {
            mPlugin = (ContactIsInGroupConditionPlugin) mCondition.reGenerateConditionPlugin();
        }

        // save the changes
        mPlugin.setContactRows(mContactGroupFlowLayout.getContacts());
        return true;
    }

    @OnClick (R.id.select_contact_button)
    public void selectContacts() {
        mNavigationManager.startContactsSelectorFragment(contacts -> {
            // Add the latest contacts
            mContactGroupFlowLayout.clear();
            mContactGroupFlowLayout.addAll( new ArrayList<>(contacts));
            mContactGroupFlowLayout.refreshLayout();
        }, new ArrayList<>(mContactGroupFlowLayout.getContacts()));
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
