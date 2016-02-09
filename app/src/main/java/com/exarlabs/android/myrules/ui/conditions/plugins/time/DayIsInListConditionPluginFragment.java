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

    static class ViewHolder {
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @Bind (R.id.grid_view_item)
        public TextView mItemText;
    }

    private class WeekDaysAdapter extends ArrayAdapter<String> {

        private final int mLayout;
        private final Context mContext;

        public WeekDaysAdapter(Context context, int layout) {
            super(context, layout);
            mLayout = layout;
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(mLayout, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }

            viewHolder = (ViewHolder) convertView.getTag();

            String itemText = getItem(position);
            viewHolder.mItemText.setText(itemText);


            boolean isItemSelected = mSelectedDays != null && mSelectedDays.contains(itemText);
            viewHolder.mItemText.setBackgroundColor(
                            isItemSelected ? getResources().getColor(R.color.colorAccent) : getResources().getColor(R.color.transparent));
            viewHolder.mItemText.setEnabled(isItemSelected);

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

    @Bind (R.id.list_of_days)
    public GridView mDaysGrid;

    private RuleCondition mCondition;
    private DayIsInListConditionPlugin mPlugin;

    private List<String> mSelectedDays;
    private WeekDaysAdapter mAdapter;

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

        mAdapter = new WeekDaysAdapter(getContext(), R.layout.grid_view_item);
        mAdapter.addAll(dayNames);

        mDaysGrid.setAdapter(mAdapter);
        mDaysGrid.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        boolean selected = !view.isEnabled();
        view.setEnabled(selected);
        view.setBackgroundColor(selected ? getResources().getColor(R.color.colorAccent) : getResources().getColor(R.color.transparent));
    }

    @Override
    protected void init(RuleCondition condition) {
        mCondition = condition;
        /*
         * Check if the condition has the right mPlugin type, and we are in edit mode
         */
        if (condition.getConditionPlugin() instanceof DayIsInListConditionPlugin) {
            mPlugin = (DayIsInListConditionPlugin) condition.getConditionPlugin();
            if (condition.isAttached()) {
                int selectedDays = mPlugin.getSelectedDays();
                WeekDaysCompostator weekDaysCompostator = new WeekDaysCompostator();
                List<String> selectedDaysList = weekDaysCompostator.getListFromCompacted(selectedDays);
                mSelectedDays = selectedDaysList;
            }
        }
    }

    @Override
    protected void refreshUI() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected boolean saveChanges() {
        // in edit mode, if the plugin is built with another type, it should be regenerate the plugin, to be able to set the values
        if (mCondition.getId() != null) {
            mPlugin = (DayIsInListConditionPlugin) mCondition.reGenerateConditionPlugin();
        }

        int childCount = mDaysGrid.getChildCount();
        List<String> selectedDays = new ArrayList<>();

        for (int i = 0; i < childCount; i++) {
            TextView child = (TextView) mDaysGrid.getChildAt(i);
            if (child.isEnabled()) {
                selectedDays.add(child.getText().toString());
            }
        }
        WeekDaysCompostator weekDaysCompostator = new WeekDaysCompostator();
        int compacted = weekDaysCompostator.compactList(selectedDays);

        // save the changes
        mPlugin.setSelectedDays(compacted);
        return true;
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
