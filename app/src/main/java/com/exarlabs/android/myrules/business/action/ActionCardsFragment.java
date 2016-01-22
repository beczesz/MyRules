package com.exarlabs.android.myrules.business.action;

import java.util.List;

import javax.inject.Inject;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exarlabs.android.myrules.business.condition.ConditionManager;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.ui.BaseFragment;
import com.exarlabs.android.myrules.ui.R;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Displays one level of hierarchy in a condition tree.
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

    private List<RuleAction> mRuleActions;


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

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Remove all the cards
        mActionsContainer.removeAllViews();

        // TODO change it to a drag and drop list view
        for (RuleAction child : mRuleActions) {
            // get the child condition
            View card = inflater.inflate(R.layout.card_layout, null);
            ActionCardViewHolder viewHolder = new ActionCardViewHolder(card);
            viewHolder.mTitle.setText(child.getActionName());
            viewHolder.mDescription.setText(child.getActionPlugin().toString());

            // add the current card
            mActionsContainer.addDragView(card, viewHolder.mDragHandle);
        }

        mDefaultMessageTextView.setVisibility(mActionsContainer.getChildCount() > 0 ? View.GONE : View.VISIBLE);
    }

    private void updateOperator() {
        // set the background color
        mOperatorTextView.setBackgroundColor(getResources().getColor(R.color.operator_and));
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
