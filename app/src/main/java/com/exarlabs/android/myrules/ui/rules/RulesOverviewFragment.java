package com.exarlabs.android.myrules.ui.rules;

import java.util.ArrayList;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.exarlabs.android.myrules.business.condition.Condition;
import com.exarlabs.android.myrules.business.condition.ConditionManager;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.devel.DevelManager;
import com.exarlabs.android.myrules.business.rule.RuleManager;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.ui.BaseFragment;
import com.exarlabs.android.myrules.ui.BuildConfig;
import com.exarlabs.android.myrules.ui.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Lists all the rules which are defined by the user.
 * Created by becze on 11/25/2015.
 */
public class RulesOverviewFragment extends BaseFragment {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = RulesOverviewFragment.class.getSimpleName();

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    /**
     * @return newInstance of SampleFragment
     */
    public static RulesOverviewFragment newInstance() {
        return new RulesOverviewFragment();
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    @Bind(R.id.build_info)
    public TextView mDevelInfo;

    @Bind(R.id.rules_list)
    public ListView mRulesListView;


    private View mRootView;

    @Inject
    public DevelManager mDevelManager;

    @Inject
    public RuleManager mRuleManager;

    @Inject
    public ConditionManager mConditionManager;

    private ArrayAdapter<RuleCondition> mAdapter;
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.rules_overview_layout, null);
        }


        return mRootView;


    }

    private void testConditions() {

        mConditionManager.deleteAll();

        // create a condition structure
        RuleCondition c1 = generateNew(Condition.Type.DEBUG_ALWAYS_TRUE, Condition.Operator.AND);
        RuleCondition c2 = generateNew(Condition.Type.DEBUG_ALWAYS_TRUE, Condition.Operator.AND);
        RuleCondition c3 = generateNew(Condition.Type.DEBUG_ALWAYS_TRUE, Condition.Operator.AND);
        RuleCondition c4 = generateNew(Condition.Type.DEBUG_ALWAYS_TRUE, Condition.Operator.AND);
        RuleCondition c5 = generateNew(Condition.Type.DEBUG_ALWAYS_TRUE, Condition.Operator.AND);

        mConditionManager.insert(c1);
        mConditionManager.insert(c2);
        mConditionManager.insert(c3);
        mConditionManager.insert(c4);
        mConditionManager.insert(c5);

        // build up the relations
        c2.setParentCondition(c1.getId());
        c3.setParentCondition(c1.getId());
        c4.setParentCondition(c3.getId());
        c5.setParentCondition(c3.getId());

        c1.getChildConditions().add(c2);
        c1.getChildConditions().add(c3);
        c3.getChildConditions().add(c4);
        c3.getChildConditions().add(c5);

        mConditionManager.update(c1);
        mConditionManager.update(c2);
        mConditionManager.update(c3);
        mConditionManager.update(c4);
        mConditionManager.update(c5);

    }

    private void readCondtions() {
        RuleCondition condition = mConditionManager.load(1L);
        condition.build();
        boolean evaluate = condition.evaluate(null);
        Log.w(TAG, "evaluated: " + evaluate);
    }

    private RuleCondition generateNew(int type, int operator) {
        RuleCondition c = new RuleCondition();
        c.setOperator(operator);
        c.setType(type);
        return c;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, new ArrayList<RuleCondition>());
        mRulesListView.setAdapter(mAdapter);

        if (BuildConfig.DEBUG) {
            mDevelInfo.setText(mDevelManager.getBuildDescription());
            mDevelInfo.setVisibility(View.VISIBLE);
        }

        updateUI();

    }

    /**
     * Update the list of rules
     */
    private void updateUI() {
        mAdapter.clear();
        mAdapter.addAll(mConditionManager.loadAllConditions());
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.generate_random)
    public void generateRandomRule() {
        readCondtions();
        mRuleManager.insert(RuleManager.generateRandom());
        updateUI();
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
