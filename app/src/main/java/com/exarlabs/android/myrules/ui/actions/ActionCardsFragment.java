package com.exarlabs.android.myrules.ui.actions;

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

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.condition.ConditionManager;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.ui.BaseFragment;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Displays the rule's actions
 *
 * Created by becze on 1/22/2016.
 */
public class ActionCardsFragment extends BaseFragment {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    public static class ActionCardViewHolder {
        public ActionCardViewHolder(View v) {
            ButterKnife.bind(this, v);
        }

        @Bind(R.id.drag_handle)
        public TextView mDragHandle;

        @Bind(R.id.card_title)
        public TextView mTitle;

        @Bind(R.id.card_description)
        public TextView mDescription;

        @Bind(R.id.card_body)
        public LinearLayout mCardBody;

        @Bind(R.id.delete_card)
        public TextView mDeleteCard;
    }

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------


    /**
     * @return newInstance of ActionCardsFragment
     */
    public static ActionCardsFragment newInstance() {
        return new ActionCardsFragment();
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    private View mRootView;

    @Bind(R.id.operator_text)
    public TextView mOperatorTextView;

    @Bind(R.id.condition_card_container)
    public DragLinearLayout mActionsContainer;

    @Bind(R.id.default_message)
    public TextView mDefaultMessageTextView;

    @Inject
    public ConditionManager mConditionManager;

    @Inject
    public NavigationManager mNavigationManager;


    private List<RuleAction> mRuleActions;
    private LayoutInflater mInflater;

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDefaultMessageTextView.setText(R.string.lbl_no_action_added);
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
        if (isAdded()) {
            // update the operator
            updateOperator();
            updateConditions();
        }
    }

    /**
     * Generates a card for each of the conditions.
     */
    private void updateConditions() {

        if (mRuleActions == null) {
            return;
        }


        // Remove all the cards
        mActionsContainer.removeAllViews();

        for (RuleAction child : mRuleActions) {
            addActionToTheContainer(child);
        }
    }

    public void addActionToTheContainer(RuleAction ruleAction){
        // create the card view
        View card = mInflater.inflate(R.layout.card_layout, null);
        card.setTag(ruleAction);

        ActionCardViewHolder viewHolder = new ActionCardViewHolder(card);
        viewHolder.mTitle.setText(ruleAction.getActionName());
        viewHolder.mDescription.setText(ruleAction.getActionPlugin().toString());

        // setup the link to the action editor
        viewHolder.mCardBody.setOnClickListener(v -> mNavigationManager.startActionDetails(ruleAction.getId()));
        viewHolder.mDeleteCard.setOnClickListener(l -> mActionsContainer.removeDragView(card));

        // add the current card
        mActionsContainer.addDragView(card, viewHolder.mDragHandle);

        mDefaultMessageTextView.setVisibility(mActionsContainer.getChildCount() > 0 ? View.GONE : View.VISIBLE);
    }

    /**
     * @return the current state of the action list
     */
    public List<RuleAction> getCurrentActionsList() {
        List<RuleAction> actions = new ArrayList<>();

        for (int i = 0; i < mActionsContainer.getChildCount(); i++) {
            View actionCard = mActionsContainer.getChildAt(i);
            actions.add((RuleAction) actionCard.getTag());
        }

        return actions;
    }


    private void updateOperator() {
        // set the background color
        mOperatorTextView.setBackgroundResource(R.drawable.operator_and);

        mOperatorTextView.setText(R.string.action_sequence);
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTERS
    // ------------------------------------------------------------------------


    public List<RuleAction> getRuleActions() {
        return mRuleActions;
    }

    public void setRuleActions(List<RuleAction> ruleActions) {
        mRuleActions = ruleActions;
        updateUI();
    }
}
