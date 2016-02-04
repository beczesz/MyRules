package com.exarlabs.android.myrules.ui.rules;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.Rule;
import com.exarlabs.android.myrules.business.rule.RuleManager;
import com.exarlabs.android.myrules.business.rule.condition.ConditionManager;
import com.exarlabs.android.myrules.business.rule.condition.ConditionTree;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.business.rule.event.EventHandlerPlugin;
import com.exarlabs.android.myrules.business.rule.event.EventPluginManager;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.model.dao.RuleConditionTree;
import com.exarlabs.android.myrules.model.dao.RuleRecord;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.RuleComponentDetailsFragment;
import com.exarlabs.android.myrules.ui.actions.ActionCardsFragment;
import com.exarlabs.android.myrules.ui.conditions.ConditionTreeFragment;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;
import com.exarlabs.android.myrules.ui.util.ui.spinner.SpinnerItemViewHolder;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.Bind;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Displays a rule with  it's events, conditions and actions.
 * Created by becze on 1/22/2016.
 */
public class RuleDetailsFragment extends RuleComponentDetailsFragment implements AdapterView.OnItemSelectedListener {


    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    private class EventPluginAdapter extends ArrayAdapter<Event.Type> {

        private final int mLayout;

        public EventPluginAdapter(Context context, int layout) {
            super(context, layout);
            mLayout = layout;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SpinnerItemViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(mLayout, null);
                viewHolder = new SpinnerItemViewHolder(convertView);
                convertView.setTag(viewHolder);
            }

            viewHolder = (SpinnerItemViewHolder) convertView.getTag();

            Event.Type item = getItem(position);
            viewHolder.mItemText.setText(getResources().getText(item.getTitleResId()));

            return convertView;
        }
    }

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String KEY_RULE_ID = "RULE_ID";

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    /**
     * @return newInstance of RuleDetailsFragment
     */
    public static RuleDetailsFragment newInstance() {
        return newInstance(-1);
    }

    public static RuleDetailsFragment newInstance(long ruleId) {
        Bundle args = new Bundle();
        args.putLong(KEY_RULE_ID, ruleId);
        RuleDetailsFragment fragment = new RuleDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    private View mRootView;

    @Bind (R.id.progress_bar)
    public ProgressBar mProgressBar;

    @Bind (R.id.rule_name)
    public EditText mRuleName;

    @Bind (R.id.spinner_events)
    public MaterialSpinner mEventsSpinner;


    @Inject
    public RuleManager mRuleManager;

    @Inject
    public ConditionManager mConditionManager;

    @Inject
    public EventPluginManager mEventPluginManager;

    @Inject
    public NavigationManager mNavigationManager;

    private RuleRecord mRuleRecord;

    private ActionCardsFragment mActionCardsFragment;
    private ConditionTreeFragment mConditionTreeFragment;
    private boolean isInitialized = false;

    private EventPluginAdapter mSpinnerAdapter;
    private Event.Type mSelectedEvent;
    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerManager.component().inject(this);
        setHasOptionsMenu(true);

        // If we have a rule Id then just extract the rule from the database. Otherwise create a new one
        long ruleId = (long) getArguments().get(KEY_RULE_ID);
        if (ruleId != -1) {
            mRuleRecord = mRuleManager.load(ruleId);
            mRuleRecord.build();
        } else {
            mRuleRecord = new RuleRecord();
            mRuleRecord.setState(Rule.State.STATE_ACTIVE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.rule_details_fragment, null);
        }
        return mRootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // the trash appears only in edit mode
        if(mRuleRecord.isAttached()) {
            inflater.inflate(R.menu.delete_item, menu);
            menu.findItem(R.id.delete_item).setIcon(
                            new IconicsDrawable(getContext(), FontAwesome.Icon.faw_trash_o).colorRes(R.color.text_dark_bg_secondary));
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
            case R.id.delete_item:
                deleteRule();
                break;
        }
        return false;
    }

    /**
     * Shows the dialog if the user really wants to delete the rule
     */
    protected void deleteRule() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(R.string.really_delete_rule).setCancelable(false).
                        setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            mRuleManager.deleteRule(mRuleRecord);
                            goBack();
                        }).setNegativeButton(android.R.string.cancel, null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!isInitialized) {
            isInitialized = true;

            // Initialize the spinner
            mSpinnerAdapter = new EventPluginAdapter(getActivity(), R.layout.spinner_item);
            mSpinnerAdapter.addAll(Event.Type.values());
            mEventsSpinner.setAdapter(mSpinnerAdapter);
            mEventsSpinner.setOnItemSelectedListener(this);

            // In edit mode: init the name field and the select the corresponding item in spinner
            if (mRuleRecord.isAttached()) {

                // Set the condition title
                String ruleName = !TextUtils.isEmpty(mRuleRecord.getRuleName()) ? mRuleRecord.getRuleName() : "";
                mRuleName.setText(ruleName);

                int position = mSpinnerAdapter.getPosition(mEventPluginManager.getFromEventCode(mRuleRecord.getEventCode()));
                mEventsSpinner.setSelection(++position);
            }

            // Load the condition display fragment
            if (mConditionTreeFragment == null) {
                long id = mRuleRecord.getRuleConditionTreeId() == null ? -1 : mRuleRecord.getRuleConditionTreeId();

                mConditionTreeFragment = ConditionTreeFragment.newInstance(id);
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.condition_card_container, mConditionTreeFragment).commit();
            }

            // Load the action display fragment
            if (mActionCardsFragment == null) {
                mActionCardsFragment = ActionCardsFragment.newInstance();
                if (mRuleRecord.getId() != null) {
                    mActionCardsFragment.setRuleActions(mRuleRecord.getRuleActions());
                }
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.actions_card_container, mActionCardsFragment).commit();
            }

            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != -1) {
            mSelectedEvent = (Event.Type) mEventsSpinner.getItemAtPosition(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @OnClick (R.id.fab_add_condition)
    public void showAddConditionFragment() {
        mNavigationManager.startConditionsSelectorFragment(conditions -> {
            for (RuleCondition condition : conditions) {
                mConditionTreeFragment.addConditionToContainer(condition);
            }
        });
    }

    @OnClick (R.id.fab_add_action)
    public void showAdActionFragment() {
        mNavigationManager.startActionsSelectorFragment(actions -> {
            for (RuleAction action : actions) {
                mActionCardsFragment.addActionToTheContainer(action);
            }
        });
    }



    @Override
    @OnClick (R.id.button_save)
    protected void saveComponent() {
        super.saveComponent();
    }

    @Override
    protected Set<String> getRequiredPermissions() {
        // Get the array of permissions.
        EventHandlerPlugin plugin = mEventPluginManager.getPluginInstance(mSelectedEvent);
        return plugin != null ? plugin.getRequiredPermissions() : null;
    }


    /**
     * Validate the fields.
     *
     * @return true if the fields are valid.
     */
    @Override
    protected boolean validateComponent() {

        // Check the title
        if (TextUtils.isEmpty(mRuleName.getText().toString().trim())) {
            mRuleName.setError(getActivity().getString(R.string.error_mandatory_field));
            return false;
        }

        // Check if the user has selected a field.
        if (mSelectedEvent == null) {
            mEventsSpinner.setError(R.string.error_mandatory_field);
            return false;
        }

        return true;
    }

    @Override
    protected void onComponentReadyToSave() {
        // update the rule names
        mRuleRecord.setRuleName(mRuleName.getText().toString().trim());

        // Update the event type
        mRuleRecord.setEventCode(mSelectedEvent.getType());

        // Update the conditions
        ConditionTree.Builder builder = mConditionTreeFragment.generateCurrentConditionTree();

        RuleConditionTree ruleConditionTree;
        if (mRuleRecord.getRuleConditionTreeId() != null) {
            ruleConditionTree = mConditionManager.rebuildConditionTree(mRuleRecord.getRuleConditionTree(), builder);
        } else {
            ruleConditionTree = mConditionManager.buildConditionTree(builder);
        }

        mRuleRecord.setRuleConditionTree(ruleConditionTree);

        // Update the actions
        List<RuleAction> actions = mActionCardsFragment.getCurrentActionsList();
        if (mRuleRecord.getId() != null) {
            mRuleRecord.getRuleActionLinks().clear();
        }

        mRuleRecord.addRuleActions(actions);

        // Save the rule record
        mRuleManager.saveRuleRecord(mRuleRecord);

        goBack();
    }


    @OnClick (R.id.button_cancel)
    public void onCancel() {
        goBack();
    }

    private void goBack() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        mNavigationManager.navigateBack(getActivity());
    }
}
