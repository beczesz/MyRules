package com.exarlabs.android.myrules.ui.conditions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exarlabs.android.myrules.business.rule.condition.ConditionManager;
import com.exarlabs.android.myrules.business.rule.condition.ConditionTree;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.model.dao.RuleConditionTree;
import com.exarlabs.android.myrules.ui.BaseFragment;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Displays one level of hierarchy in a condition tree.
 * Created by becze on 1/22/2016.
 */
public class ConditionTreeFragment extends BaseFragment {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    public static class ConditionCardViewHolder {
        public ConditionCardViewHolder(View v) {
            ButterKnife.bind(this, v);
        }

        @Bind(R.id.drag_handle)
        public TextView mDragHandle;

        @Bind(R.id.card_title)
        public TextView mConditionTitle;

        @Bind(R.id.card_description)
        public TextView mConditionDescription;

        @Bind(R.id.card_body)
        public LinearLayout mCardBody;
    }

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String KEY_CONDITION_TREE_ID = "CONDITION_TREE_ID";

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    public static ConditionTreeFragment newInstance(long conditionTreeId) {
        Bundle args = new Bundle();
        args.putLong(KEY_CONDITION_TREE_ID, conditionTreeId);
        ConditionTreeFragment fragment = new ConditionTreeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    private View mRootView;

    @Bind(R.id.operator_text)
    public TextView mOperatorTextView;

    @Bind(R.id.condition_card_container)
    public DragLinearLayout mConditionsContainer;

    @Bind(R.id.default_message)
    public TextView mDefaultMessageTextView;


    @Inject
    public ConditionManager mConditionManager;

    @Inject
    public NavigationManager mNavigationManager;

    private RuleConditionTree mRuleConditionTree;
    private LayoutInflater mInflater;
    // By default the selected operator will be and
    private int mSelectedOperator = ConditionTree.Operator.AND;
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

        long conditionTreeId = getArguments().getLong(KEY_CONDITION_TREE_ID);

        if (conditionTreeId != -1) {
            mRuleConditionTree = mConditionManager.loadConditionTree(conditionTreeId);
        } else {
            mRuleConditionTree = new RuleConditionTree();
        }

        // init with the operator
        mSelectedOperator = mRuleConditionTree.getOperator();

        mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.card_group_layout, null);
        }
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    /**
     * Regenerate the list of conditions.
     */
    private void updateUI() {

        // update the operator
        updateOperator();
        updateConditions();
    }

    /**
     * Generates a card for each of the conditions.
     */
    private void updateConditions() {
        // Remove all the cards
        mConditionsContainer.removeAllViews();

        for (RuleConditionTree child : mRuleConditionTree.getChildConditions()) {
            addConditionToContainer(child.getRuleCondition());
        }

        mDefaultMessageTextView.setVisibility(mConditionsContainer.getChildCount() > 0 ? View.GONE : View.VISIBLE);
    }

    /**
     * Creates a new card view with the condition, and adds to the container
     *
     * @param ruleCondition
     */
    public void addConditionToContainer(RuleCondition ruleCondition){
        // Inflate the card and add the child as a tag
        View card = mInflater.inflate(R.layout.card_layout, null);
        card.setTag(ruleCondition);

        // get the child condition
        ConditionCardViewHolder viewHolder = new ConditionCardViewHolder(card);
        viewHolder.mConditionTitle.setText(ruleCondition.getConditionName());
        viewHolder.mConditionDescription.setText(ruleCondition.getConditionPlugin().toString());

        // setup the link to the condition editor
        viewHolder.mCardBody.setOnClickListener(v -> mNavigationManager.startConditionsDetails(ruleCondition.getId()));

        // add the current card
        mConditionsContainer.addDragView(card, viewHolder.mDragHandle);
    }

    /**
     * @return the current state of the condition tree.
     */
    public RuleConditionTree.Builder generateCurrentConditionTree() {
        List<RuleCondition> conditions = new ArrayList<>();

        for (int i = 0; i < mConditionsContainer.getChildCount(); i++) {
            View conditionCard = mConditionsContainer.getChildAt(i);
            conditions.add((RuleCondition) conditionCard.getTag());
        }

        RuleConditionTree.Builder builder = new ConditionTree.Builder();
        builder.add(mConditionManager.getDefaultCondition(), conditions, mSelectedOperator);
        return builder;
    }

    private void updateOperator() {
        // set the background color
        mOperatorTextView.setBackgroundColor(
                        mSelectedOperator == ConditionTree.Operator.AND ? getResources().getColor(R.color.operator_and) : getResources().getColor(
                                        R.color.operator_or));

        mOperatorTextView.setText(mSelectedOperator == ConditionTree.Operator.AND ? R.string.operator_and : R.string.operator_or);
    }

    @OnClick(R.id.operator_text)
    public void changeOperator() {
        // switch the operator
        mSelectedOperator = mSelectedOperator == ConditionTree.Operator.AND ? ConditionTree.Operator.OR : ConditionTree.Operator.AND;
        updateOperator();
    }
}
