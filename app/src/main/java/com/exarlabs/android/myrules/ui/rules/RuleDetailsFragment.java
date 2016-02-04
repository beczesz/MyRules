package com.exarlabs.android.myrules.ui.rules;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.RuleManager;
import com.exarlabs.android.myrules.business.rule.condition.ConditionManager;
import com.exarlabs.android.myrules.business.rule.condition.ConditionTree;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.business.rule.event.EventHandlerPlugin;
import com.exarlabs.android.myrules.business.rule.event.EventPluginManager;
import com.exarlabs.android.myrules.business.rx.CallbackSubscriber;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.model.dao.RuleConditionTree;
import com.exarlabs.android.myrules.model.dao.RuleRecord;
import com.exarlabs.android.myrules.ui.BaseFragment;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.actions.ActionCardsFragment;
import com.exarlabs.android.myrules.ui.conditions.ConditionTreeFragment;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;
import com.exarlabs.android.myrules.ui.util.ui.spinner.SpinnerItemViewHolder;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.Bind;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Displays a rule with  it's events, conditions and actions.
 * Created by becze on 1/22/2016.
 */
public class RuleDetailsFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {


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

        // If we have a rule Id then just extract the rule from the database. Otherwise create a new one
        long ruleId = (long) getArguments().get(KEY_RULE_ID);
        if (ruleId != -1) {
            mRuleRecord = mRuleManager.load(ruleId);
            mRuleRecord.build();
        } else {
            mRuleRecord = new RuleRecord();
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
            mSelectedEvent = mSpinnerAdapter.getItem(position);
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

    @OnClick (R.id.button_save)
    public void saveRule() {
        if (isValid()) {
            checkPermissions();
        }
    }

    private void doSave() {
        updateRule();
        goBack();
    }

    /**
     * Checks if all the necessary permission is granted for this rule
     */
    private void checkPermissions() {
        // Get the array of permissions.
        EventHandlerPlugin plugin = mEventPluginManager.getPluginInstance(mSelectedEvent);


        Set<String> permissionsSet = plugin.getRequiredPermissions();
        String[] permissions = permissionsSet.toArray(new String[permissionsSet.size()]);

        if (permissions.length > 0) {

            //@formatter:off
            // Must be done during an initialization phase like onCreate
            RxPermissions.getInstance(getActivity())
                            .request( permissions)
                            .subscribe(new CallbackSubscriber<Boolean>() {
                                           @Override
                                           public void onResult(Boolean result, Throwable e) {
                                               if (result) {
                                                  doSave();
                                               } else {
                                                   Toast.makeText(getActivity(), R.string.message_error_permission_denied, Toast.LENGTH_SHORT).show();
                                               }
                                           }
                                       });

            //@formatter:on
        } else {
            doSave();
        }
    }


    /**
     * Validate the fields.
     * @return true if the fields are valid.
     */
    private boolean isValid() {

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

    /**
     * Updates the rule record.
     */
    private void updateRule() {
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
    }


    @OnClick (R.id.button_cancel)
    public void cancelRule() {
        goBack();
    }

    private void goBack() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        mNavigationManager.navigateBack(getActivity());
    }
}
