package com.exarlabs.android.myrules.ui.conditions;

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
import com.exarlabs.android.myrules.business.rule.condition.ConditionManager;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.ui.BaseFragment;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;
import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Let's the user to select some conditions. When the user is finished it notifies back the originator fragment.
 *
 * Created by atiyka on 2016.01.29..
 */
public class ConditionsSectorFragment extends BaseFragment {
    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    /**
     * Listener interface for selecting multiple conditions
     */
    public interface ConditionsSelectorListener {

        /**
         * Callback method for the selected conditions.
         *
         * @param conditions
         */
        void onConditionsSelected(List<RuleCondition> conditions);
    }

    public static class ConditionsRowViewHolder {
        public ConditionsRowViewHolder(View v) {
            ButterKnife.bind(this, v);
        }

        @Bind(R.id.condition_id)
        public TextView mConditionId;

        @Bind(R.id.condition_name)
        public TextView mConditionName;

        @Bind(R.id.condition_selected_cb)
        public CheckBox mConditionSelected;
    }

    public class ConditionAdapter extends ArrayAdapter<RuleCondition> {

        private final Context mContext;

        public ConditionAdapter(Context context) {
            super(context, R.layout.condition_row_layout);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ConditionsRowViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.condition_row_layout, null);
                viewHolder = new ConditionsRowViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            viewHolder = (ConditionsRowViewHolder) convertView.getTag();

            RuleCondition ruleCondition = getItem(position);

            viewHolder.mConditionId.setText(Long.toString(ruleCondition.getId()));
            viewHolder.mConditionName.setText(ruleCondition.getConditionName());
            viewHolder.mConditionSelected.setChecked(isSelected(ruleCondition));
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
     * @return newInstance of ConditionsSectorFragment
     */
    public static ConditionsSectorFragment newInstance() {
        return new ConditionsSectorFragment();
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private ConditionsSelectorListener mConditionsSelectorListener;
    private ConditionAdapter mConditionAdapter;

    @Inject
    public NavigationManager mNavigationManager;

    @Inject
    public ConditionManager mConditionManager;

    @Bind(R.id.condition_list)
    public ListView mConditionsList;

    @Bind(R.id.conditions_filter)
    public EditText mConditionsFilterEditText;

    private List<RuleCondition> mSelectedConditions;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public ConditionsSectorFragment() {
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
        mConditionAdapter = new ConditionAdapter(getActivity());
        mSelectedConditions = new ArrayList<>();

        queryConditions();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.condition_selector_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
            case R.id.selection_completed:
                notifyConditionSelectorListener(mSelectedConditions);
                goBack();
                break;
        }
        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.conditions_selector_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mConditionsList.setAdapter(mConditionAdapter);

        //@formatter:off
        RxTextView.textChangeEvents(mConditionsFilterEditText)
                        .debounce(MyRulesConstants.DELAY_BEFORE_PROCESSING, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(textChangedEvent -> mConditionAdapter.getFilter().filter(textChangedEvent.text().toString()));
        //@formatter:on

    }

    private void queryConditions() {
        List<RuleCondition> conditions = mConditionManager.loadAllConditions();
        mConditionAdapter.clear();
        mConditionAdapter.addAll(conditions);
        mConditionAdapter.notifyDataSetChanged();
    }


    private void notifyConditionSelectorListener(List<RuleCondition> conditions) {
        if (mConditionsSelectorListener != null) {
            mConditionsSelectorListener.onConditionsSelected(conditions);
        }
    }

    @OnItemClick(R.id.condition_list)
    void onItemClick(int position) {
        RuleCondition RuleCondition = mConditionAdapter.getItem(position);
        if (mSelectedConditions.contains(RuleCondition)) {
            mSelectedConditions.remove(RuleCondition);
        } else {
            mSelectedConditions.add(RuleCondition);
        }

        // update the fragment's title
        String title = getString(R.string.condition_selected) + " " + mSelectedConditions.size();
        initActionBar(true, title);

        // notify the adapter to update the items
        mConditionAdapter.notifyDataSetChanged();
    }


    /**
     * Returns true if the condition row is selected
     *
     * @param ruleCondition
     * @return
     */
    private boolean isSelected(RuleCondition ruleCondition) {
        return mSelectedConditions.contains(ruleCondition);
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

    public ConditionsSelectorListener getConditionsSelectorListener() {
        return mConditionsSelectorListener;
    }

    public void setConditionsSelectorListener(ConditionsSelectorListener conditionsSelectorListener) {
        mConditionsSelectorListener = conditionsSelectorListener;
    }
}
