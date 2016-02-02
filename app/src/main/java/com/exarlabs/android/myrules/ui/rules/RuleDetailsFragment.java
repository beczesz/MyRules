package com.exarlabs.android.myrules.ui.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;

/**
 * Displays a rule with  it's events, conditions and actions.
 * Created by becze on 1/22/2016.
 */
public class RuleDetailsFragment extends BaseFragment {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

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

    @Bind(R.id.progress_bar)
    public ProgressBar mProgressBar;

    @Bind(R.id.rule_name)
    public EditText mRuleName;

    @Bind(R.id.spinner_events)
    public Spinner mEventsSpinner;


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

        // Load the condition display fragment
        if(mConditionTreeFragment == null) {
            long id = mRuleRecord.getRuleConditionTreeId() == null ? -1 : mRuleRecord.getRuleConditionTreeId();

            mConditionTreeFragment = ConditionTreeFragment.newInstance(id);
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.condition_card_container, mConditionTreeFragment).commit();
        }

        // Load the condition display fragment
        if(mActionCardsFragment == null) {
            mActionCardsFragment = ActionCardsFragment.newInstance();
            if(mRuleRecord.getId() != null)
                mActionCardsFragment.setRuleActions(mRuleRecord.getRuleActions());
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.actions_card_container, mActionCardsFragment).commit();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        // Update the rule name.
        mRuleName.setText(mRuleRecord.getRuleName());

        // update the events list
        setUpEvents();

        mProgressBar.setVisibility(View.GONE);
    }

    private void setUpEvents() {
        // Initialize the event spinenr
        List<EventHandlerPlugin> plugins = mEventPluginManager.getPlugins();

        //@formatter:off
        // Get the list of event plugins.
        List<String> eventPluginNames = new ArrayList<>();
        Observable.from(plugins)
                        .map(plugin -> mEventPluginManager.getFromEventCode(plugin.getType()))
                        .subscribe(type -> eventPluginNames.add(getContext().getResources().getString(type.getTitleResId())));
        //@formatter:on

        // setup the spinner
        mEventsSpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, eventPluginNames));

        // Initialize the spinner.
        Event.Type eventType = mEventPluginManager.getFromEventCode(mRuleRecord.getEventCode());
        EventHandlerPlugin plugin = mEventPluginManager.getPluginInstance(eventType);

        if (plugin != null) {
            int selectedIndex = plugins.indexOf(plugin);
            mEventsSpinner.setSelection(selectedIndex);
        }

    }

    @OnClick(R.id.fab_add_condition)
    public void showAddConditionFragment() {
        mNavigationManager.startConditionsSelectorFragment(conditions -> {
            for (RuleCondition condition : conditions) {
                mConditionTreeFragment.addConditionToContainer(condition);
            }
        });
    }

    @OnClick(R.id.fab_add_action)
    public void showAdActionFragment() {
        mNavigationManager.startActionsSelectorFragment(actions -> {
            for (RuleAction action : actions) {
                mActionCardsFragment.addActionToTheContainer(action);
            }
        });
    }

    @OnClick(R.id.button_save)
    public void saveRule() {

        checkPermissions();

    }

    private void doSave() {
        if (validateRule()) {
            updateRule();
            goBack();
        }
    }

    /**
     * Checks if all the necessary permission is granted for this rule
     */
    private void checkPermissions() {
        // Get the array of permissions.
        Set<String> permissionsSet = mRuleManager.getPermissions(mRuleRecord);
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
     * Updates the rule record.
     */
    private void updateRule() {
        // update the rule names
        mRuleRecord.setRuleName(mRuleName.getText().toString().trim());

        // Update the event type
        int selectedItemPosition = mEventsSpinner.getSelectedItemPosition();
        EventHandlerPlugin plugin = mEventPluginManager.getPlugins().get(selectedItemPosition);
        mRuleRecord.setEventCode(plugin.getType());

        // Update the conditions
        ConditionTree.Builder builder = mConditionTreeFragment.generateCurrentConditionTree();

        RuleConditionTree ruleConditionTree;
        if(mRuleRecord.getRuleConditionTreeId() != null)
            ruleConditionTree = mConditionManager.rebuildConditionTree(mRuleRecord.getRuleConditionTree(), builder);
        else
            ruleConditionTree = mConditionManager.buildConditionTree(builder);

        mRuleRecord.setRuleConditionTree(ruleConditionTree);

        // Update the actions
        List<RuleAction> actions = mActionCardsFragment.getCurrentActionsList();
        if(mRuleRecord.getId() != null)
            mRuleRecord.getRuleActionLinks().clear();

        mRuleRecord.addRuleActions(actions);

        // Save the rule record
        mRuleManager.saveRuleRecord(mRuleRecord);
    }

    /**
     * Validates the rule record
     *
     * @return
     */
    private boolean validateRule() {
        if (mRuleName.getText().length() == 0) {
            mRuleName.setError(getActivity().getString(R.string.message_rule_name_mandatory));
            return false;
        }
        return true;
    }

    @OnClick(R.id.button_cancel)
    public void cancelRule() {
        goBack();
    }

    private void goBack() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        mNavigationManager.navigateBack(getActivity());
    }
}
