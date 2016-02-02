package com.exarlabs.android.myrules.business.rule.condition.plugins.math;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.util.Log;

import com.exarlabs.android.myrules.business.rule.RuleComponentProperty;
import com.exarlabs.android.myrules.business.rule.condition.ConditionPlugin;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.business.rule.event.plugins.math.NumberEvent;

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
    public IsNumberEqualConditionPlugin(int pluginType) {
        super(pluginType);
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------


    @Override
    public void initialize(List<? extends RuleComponentProperty> properties) {
        super.initialize(properties);

        mValue = Double.parseDouble(getProperty(KEY_VALUE).getValue());
    }

    @Override
    public boolean evaluate(Event event) {
        if (event instanceof NumberEvent) {
            int value = ((NumberEvent) event).getValue();
            boolean isEqual = mValue == value;
            Log.w(TAG, "Value: " + value + " is indeed: " + isEqual);
            return isEqual;
        }

        // Always return true if we can not process this event
        return true;
    }

    @Override
    public String toString() {
        return "Check if number equals with" + mValue;
    }

    @Override
    public Set<String> getRequiredPermissions() {
        return new HashSet<>();
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
