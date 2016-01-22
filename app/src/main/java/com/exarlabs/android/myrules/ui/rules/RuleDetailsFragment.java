package com.exarlabs.android.myrules.ui.rules;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.exarlabs.android.myrules.business.action.ActionCardsFragment;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.event.EventPluginManager;
import com.exarlabs.android.myrules.business.rule.RuleManager;
import com.exarlabs.android.myrules.model.dao.RuleRecord;
import com.exarlabs.android.myrules.ui.BaseFragment;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.conditions.ConditionTreeFragment;

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
    public EventPluginManager mEventPluginManager;

    private RuleRecord mRuleRecord;

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
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.condition_card_container,
                        ConditionTreeFragment.newInstance(mRuleRecord.getRuleConditionTreeId())).commit();

        // Load the condition display fragment
        ActionCardsFragment actionCardsFragment = ActionCardsFragment.newInstance();
        actionCardsFragment.setRuleActions(mRuleRecord.getRuleActions());
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.actions_card_container, actionCardsFragment).commit();

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
        // Get the list of event plugins.
        List<String> eventPluginNames = new ArrayList<>();
        Observable.from(mEventPluginManager.getPlugins()).map(plugins -> plugins.getClass().getSimpleName()).subscribe(
                        conditionName -> eventPluginNames.add(conditionName));

        // setup the spinner
        mEventsSpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, eventPluginNames));
    }

    @OnClick(R.id.fab_add_condition)
    public void showAddConditionFragment() {
        Toast.makeText(getActivity(), "Select condition fragment", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fab_add_action)
    public void showAdActionFragment() {
        Toast.makeText(getActivity(), "Select action fragment", Toast.LENGTH_SHORT).show();
    }
}
