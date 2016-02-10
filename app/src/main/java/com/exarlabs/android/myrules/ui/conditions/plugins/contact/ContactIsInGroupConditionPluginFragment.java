package com.exarlabs.android.myrules.ui.conditions.plugins.contact;

import java.util.ArrayList;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.condition.ConditionPlugin;
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
        super.init(condition);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plugin_contact_is_in_group_layout, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    protected void refreshUI() {
        ConditionPlugin plugin = getPlugin();
        if (plugin != null && plugin instanceof ContactIsInGroupConditionPlugin) {
            ContactIsInGroupConditionPlugin contactPlugin = (ContactIsInGroupConditionPlugin) plugin;

            mContactGroupFlowLayout.addAll(contactPlugin.getContactRows());
            mContactGroupFlowLayout.refreshLayout();
        }
    }

    @Override
    protected boolean saveChanges() {
        super.saveChanges();

        // save the changes
        ContactIsInGroupConditionPlugin plugin = (ContactIsInGroupConditionPlugin) getPlugin();
        plugin.setContactRows(mContactGroupFlowLayout.getContacts());
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
