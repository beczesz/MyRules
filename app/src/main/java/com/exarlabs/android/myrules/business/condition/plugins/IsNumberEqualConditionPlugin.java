package com.exarlabs.android.myrules.business.condition.plugins;

import java.util.List;

import android.util.Log;

import com.exarlabs.android.myrules.business.condition.ConditionPlugin;
import com.exarlabs.android.myrules.business.event.Event;
import com.exarlabs.android.myrules.business.event.plugins.debug.NumberEvent;
import com.exarlabs.android.myrules.model.dao.RuleConditionProperty;

/**
 * Example implementation where this plugin can decide if a number is equal with another number
 * Created by becze on 1/14/2016.
 */
public class IsNumberEqualConditionPlugin extends ConditionPlugin {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = IsNumberEqualConditionPlugin.class.getSimpleName();

    private static final String KEY_VALUE = "VALUE";

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    private double mValue;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------


    @Override
    public void initialize(List<RuleConditionProperty> properties) {
        super.initialize(properties);

        mValue = Double.parseDouble(getProperty(KEY_VALUE).getValue());
    }

    @Override
    public boolean evaluate(Event event) {
        if (event instanceof NumberEvent) {
            int value = ((NumberEvent) event).getValue();
            boolean isEqual = mValue == value;
            Log.w(TAG, "Value: " + value + " is indide: " + isEqual);
            return isEqual;
        }

        // Always return true if we can not process this event
        return true;
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------


    public double getValue() {
        return mValue;
    }

    public void setValue(double value) {
        saveProperty(KEY_VALUE, Double.toString(value));
        mValue = value;
    }
}
