package com.exarlabs.android.myrules.ui.conditions.plugins.time;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.rule.condition.plugins.time.DayIsInListConditionPlugin;
import com.exarlabs.android.myrules.model.dao.RuleCondition;
import com.exarlabs.android.myrules.ui.R;
import com.exarlabs.android.myrules.ui.conditions.ConditionPluginFragment;
import com.exarlabs.android.myrules.ui.navigation.NavigationManager;
import com.exarlabs.android.myrules.ui.util.WeekDaysCompostator;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Displays the settings for selecting week days
 * Created by atiyka on 2016.02.04..
 */
public class DayIsInListConditionPluginFragment extends ConditionPluginFragment implements AdapterView.OnItemClickListener {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    static class ViewHolder
    {
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.grid_view_item)
        public TextView mItemText;
    }

    private class WeekDaysAdapter extends ArrayAdapter<String> {

        private final int mLayout;

        public WeekDaysAdapter(Context context, int layout) {
            super(context, layout);
            mLayout = layout;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(mLayout, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }

            viewHolder = (ViewHolder) convertView.getTag();

            String itemText = getItem(position);
            viewHolder.mItemText.setText(itemText);

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
     * @return newInstance of DayIsInListConditionPluginFragment
     */
    public static DayIsInListConditionPluginFragment newInstance() {
        return new DayIsInListConditionPluginFragment();
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    @Inject
    public NavigationManager mNavigationManager;

    @Bind(R.id.list_of_days)
    public GridView mListOfDays;

    private RuleCondition mCondition;
    private DayIsInListConditionPlugin mPlugin;

    private List<String> mSelectedDays;

    private WeekDaysCompostator mWeekDaysCompostator;
    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerManager.component().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plugin_day_is_in_list_layout, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<String> dayNames = Arrays.asList(getContext().getResources().getStringArray(R.array.list_of_weekdays));

        WeekDaysAdapter adapter = new WeekDaysAdapter(getContext(), R.layout.grid_view_item);
        adapter.addAll(dayNames);
        adapter.notifyDataSetChanged();

        mListOfDays.setAdapter(adapter);
        mListOfDays.setOnItemClickListener(this);
        refreshUI();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(!view.getTag().equals(0)){
            view.setTag(0);
            view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }else{
            view.setTag(1);
            view.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }

    @Override
    protected void init(RuleCondition condition) {
        mCondition = condition;
        /*
         * Check if the condition has the right mPlugin type, and we are in edit mode
         */
        if (condition.getConditionPlugin() instanceof DayIsInListConditionPlugin) {
            mPlugin = (DayIsInListConditionPlugin) condition.getConditionPlugin();
            if(condition.isAttached()) {
                int selectedDays = mPlugin.getSelectedDays();
                mWeekDaysCompostator = new WeekDaysCompostator();
                List<String> selectedDaysList = mWeekDaysCompostator.getListFromCompacted(selectedDays);
                mSelectedDays = selectedDaysList;
            }
        }
    }

    @Override
    protected void refreshUI() {
        // ToDo: it's called twice or more. Why?

        if(mListOfDays != null) {
            int childCount = mListOfDays.getChildCount();
            for (int i = 0; i < childCount; i++) {
                TextView child = (TextView) mListOfDays.getChildAt(i);
                if (mSelectedDays.contains(child.getText().toString())) {
                    child.setTag(0);
                    child.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
            }
        }
    }

    @Override
    protected void saveChanges() {
        int childCount = mListOfDays.getChildCount();
        List<String> selectedDays = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            TextView child = (TextView) mListOfDays.getChildAt(i);
            if(child.getTag().equals(0)){
                selectedDays.add(child.getText().toString());
            }
        }

        int compacted = mWeekDaysCompostator.compactList(selectedDays);

        // save the changes
        mPlugin.setSelectedDays(compacted);
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
