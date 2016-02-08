package com.exarlabs.android.myrules.business.rule.condition.plugins.time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.exarlabs.android.myrules.business.rule.RuleComponentProperty;
import com.exarlabs.android.myrules.business.rule.condition.ConditionPlugin;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.model.dao.RuleConditionProperty;
import com.exarlabs.android.myrules.ui.util.WeekDaysCompostator;

/**
 * Checks whether the day is member of a predefined list
 * Created by becze on 1/28/2016.
 */
public class DayIsInListConditionPlugin extends ConditionPlugin {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = DayIsInListConditionPlugin.class.getSimpleName();

    private static final String KEY_DAYS = "DAYS";

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    /**
     * This is the compacted list of selected days
     */
    private int mDaysCompacted;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public DayIsInListConditionPlugin() {

    }


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------


    @Override
    public void initialize(List<? extends RuleComponentProperty> properties) {
        super.initialize(properties);

        RuleConditionProperty property = getProperty(KEY_DAYS);
        mDaysCompacted = property != null ? Integer.parseInt(property.getValue()) : 0;
    }

    /**
     * Checks if the current day is in the saved list in the condition
     *
     * @param event The event which triggered this evaluation.
     * @return
     */
    @Override
    public boolean evaluate(Event event) {
        WeekDaysCompostator compostator = new WeekDaysCompostator();
        List<String> days = compostator.getListFromCompacted(mDaysCompacted);

        Date date = GregorianCalendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("EEE", Locale.ENGLISH);

        String day = dateFormat.format(date);
        if(days.contains(day)){
            return true;
        }
        return false;
    }

    @Override
    public Set<String> getRequiredPermissions() {
        HashSet<String> permissions = new HashSet<>();
        permissions.add(android.Manifest.permission.READ_CONTACTS);
        return permissions;
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------

    public void setSelectedDays(int daysCompacted) {
        saveProperty(KEY_DAYS, Integer.toString(daysCompacted));
        mDaysCompacted = daysCompacted;
    }

    public int getSelectedDays(){
        return mDaysCompacted;
    }
}
