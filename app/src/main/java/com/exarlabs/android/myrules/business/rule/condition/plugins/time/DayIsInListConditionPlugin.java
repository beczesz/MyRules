package com.exarlabs.android.myrules.business.rule.condition.plugins.time;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.exarlabs.android.myrules.business.rule.RuleComponentProperty;
import com.exarlabs.android.myrules.business.rule.condition.ConditionPlugin;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.business.rule.event.plugins.ContactEvent;
import com.exarlabs.android.myrules.model.dao.RuleConditionProperty;

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

    @Override
    public boolean evaluate(Event event) {
        if (event instanceof ContactEvent) {

//            return mContactRows.contains(((ContactEvent) event).getContact());
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
