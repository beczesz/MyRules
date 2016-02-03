package com.exarlabs.android.myrules.ui.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.exarlabs.android.myrules.business.MyRulesConstants;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.action.ActionManager;
import com.exarlabs.android.myrules.model.dao.RuleAction;
import com.exarlabs.android.myrules.ui.BaseFragment;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;
import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Let's the user to select some actions. When the user is finished it notifies back the originator fragment.
 *
 * Created by atiyka on 2016.02.01..
 */
public class ActionsSectorFragment extends BaseFragment {
    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    /**
     * Listener interface for selecting multiple actions
     */
    public interface ActionsSelectorListener {

        /**
         * Callback method for the selected actions.
         *
         * @param actions
         */
        void onActionsSelected(List<RuleAction> actions);
    }

    public static class ActionsRowViewHolder {
        public ActionsRowViewHolder(View v) {
            ButterKnife.bind(this, v);
        }

        @Bind(R.id.action_id)
        public TextView mActionId;

        @Bind(R.id.action_name)
        public TextView mActionName;

        @Bind(R.id.action_selected_cb)
        public CheckBox mActionSelected;
    }

    public class ActionAdapter extends ArrayAdapter<RuleAction> {

        private final Context mContext;

        public ActionAdapter(Context context) {
            super(context, R.layout.action_row_layout);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ActionsRowViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.action_row_layout, null);
                viewHolder = new ActionsRowViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            viewHolder = (ActionsRowViewHolder) convertView.getTag();

            RuleAction ruleAction = getItem(position);

            viewHolder.mActionId.setText(Long.toString(ruleAction.getId()));
            viewHolder.mActionName.setText(ruleAction.getActionName());
            viewHolder.mActionSelected.setChecked(isSelected(ruleAction));
            return convertView;
        }
    }

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    /**
     * @return newInstance of ActionsSectorFragment
     */
    public static ActionsSectorFragment newInstance() {
        return new ActionsSectorFragment();
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private ActionsSelectorListener mActionsSelectorListener;
    private ActionAdapter mActionAdapter;

    @Inject
    public NavigationManager mNavigationManager;

    @Inject
    public ActionManager mActionManager;

    @Bind(R.id.action_list)
    public ListView mActionsList;

    @Bind(R.id.actions_filter)
    public EditText mActionsFilterEditText;

    private List<RuleAction> mSelectedActions;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public ActionsSectorFragment() {
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerManager.component().inject(this);
        setHasOptionsMenu(true);

        // create a new adapter and add it to the list view
        mActionAdapter = new ActionAdapter(getActivity());
        mSelectedActions = new ArrayList<>();

        queryActions();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_selector_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
            case R.id.selection_completed:
                notifyActionSelectorListener(mSelectedActions);
                goBack();
                break;
        }
        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.actions_selector_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActionsList.setAdapter(mActionAdapter);

        //@formatter:off
        RxTextView.textChangeEvents(mActionsFilterEditText)
                        .debounce(MyRulesConstants.DELAY_BEFORE_PROCESSING, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(textChangedEvent -> mActionAdapter.getFilter().filter(textChangedEvent.text().toString()));
        //@formatter:on

    }

    private void queryActions() {
        List<RuleAction> actions = mActionManager.loadAllActions();
        mActionAdapter.clear();
        mActionAdapter.addAll(actions);
        mActionAdapter.notifyDataSetChanged();
    }


    private void notifyActionSelectorListener(List<RuleAction> actions) {
        if (mActionsSelectorListener != null) {
            mActionsSelectorListener.onActionsSelected(actions);
        }
    }

    @OnItemClick(R.id.action_list)
    void onItemClick(int position) {
        RuleAction RuleAction = mActionAdapter.getItem(position);
        if (mSelectedActions.contains(RuleAction)) {
            mSelectedActions.remove(RuleAction);
        } else {
            mSelectedActions.add(RuleAction);
        }

        // update the fragment's title
        String title = getString(R.string.action_selected) + " " + mSelectedActions.size();
        initActionBarWithBackButton(title);

        // notify the adapter to update the items
        mActionAdapter.notifyDataSetChanged();
    }


    /**
     * Returns true if the action row is selected
     *
     * @param ruleAction
     * @return
     */
    private boolean isSelected(RuleAction ruleAction) {
        return mSelectedActions.contains(ruleAction);
    }

    /**
     * Hides the keyboard, and navigates back
     *
     */
    private void goBack(){
        // hide the keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

        mNavigationManager.navigateBack(getActivity());
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------

    public ActionsSelectorListener getActionsSelectorListener() {
        return mActionsSelectorListener;
    }

    public void setActionsSelectorListener(ActionsSelectorListener actionsSelectorListener) {
        mActionsSelectorListener = actionsSelectorListener;
    }
}
