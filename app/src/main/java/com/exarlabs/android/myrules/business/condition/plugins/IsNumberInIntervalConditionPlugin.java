package com.exarlabs.android.myrules.business.condition.plugins;

import java.util.List;

import android.util.Log;

import com.exarlabs.android.myrules.business.condition.ConditionPlugin;
import com.exarlabs.android.myrules.business.event.Event;
import com.exarlabs.android.myrules.business.event.plugins.debug.NumberEvent;
import com.exarlabs.android.myrules.model.dao.RuleConditionProperty;

/**
 * Example implementation where this plugin can decide if a number is between two given number
 * Created by becze on 1/14/2016.
 */
public class IsNumberInIntervalConditionPlugin extends ConditionPlugin {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = IsNumberInIntervalConditionPlugin.class.getSimpleName();

    private static final String KEY_INTERVAL_MIN = "INTERVAL_MIN";
    private static final String KEY_INTERVAL_MAX = "INTERVAL_MAX";

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    private double mMin, mMax;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------


    @Override
    public void initialize(List<RuleConditionProperty> properties) {
        super.initialize(properties);


        mMin = Double.parseDouble(getProperty(KEY_INTERVAL_MIN).getValue());
        mMax = Double.parseDouble(getProperty(KEY_INTERVAL_MAX).getValue());
    }

    @Override
    public boolean evaluate(Event event) {
        if (event instanceof NumberEvent) {
            int value = ((NumberEvent) event).getValue();
            boolean isInside = mMin <= value && mMax >= value;
            Log.w(TAG, "Value: " + value + " is indide: " + isInside);
            return isInside;
        }

        // Always return true if we can not process this event
        return true;
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------


    public double getMin() {
        return mMin;
    }

    public void setMin(double min) {
        saveProperty(KEY_INTERVAL_MIN, Double.toString(min));
        mMin = min;
    }

    public double getMax() {
        return mMax;
    }

    public void setMax(double max) {
        saveProperty(KEY_INTERVAL_MAX, Double.toString(max));
        mMax = max;
    }
}
