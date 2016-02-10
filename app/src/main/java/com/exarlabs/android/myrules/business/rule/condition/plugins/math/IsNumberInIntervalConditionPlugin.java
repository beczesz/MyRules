package com.exarlabs.android.myrules.business.rule.condition.plugins.math;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.util.Log;

import com.exarlabs.android.myrules.business.rule.RuleComponentProperty;
import com.exarlabs.android.myrules.business.rule.condition.ConditionPlugin;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.business.rule.event.plugins.math.NumberEvent;
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
    private static final String KEY_INTERVAL_IS_OUT = "INTERVAL_IS_OUT";

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    private double mMin, mMax;
    private boolean mIsOutside;


    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------


    @Override
    public void initialize(List<? extends RuleComponentProperty> properties) {
        super.initialize(properties);

        RuleConditionProperty minProperty = getProperty(KEY_INTERVAL_MIN);
        mMin = minProperty != null ? Double.parseDouble(minProperty.getValue()) : 0;
        RuleConditionProperty maxProperty = getProperty(KEY_INTERVAL_MAX);
        mMax = maxProperty != null ? Double.parseDouble(maxProperty.getValue()) : 0;
        RuleComponentProperty isOut = getProperty(KEY_INTERVAL_IS_OUT);
        mIsOutside = isOut != null && Boolean.parseBoolean(isOut.getValue());
    }

    @Override
    public boolean evaluate(Event event) {
        if (event instanceof NumberEvent) {
            int value = ((NumberEvent) event).getValue();
            boolean result = mMin <= value && mMax >= value;
            if (mIsOutside) {
                result = !result;
            }
            Log.w(TAG, "Value: " + value + " is " + (mIsOutside ? "outside: " : "inside: ") + result);
            return result;
        }

        // Always return true if we can not process this event
        return true;
    }

    @Override
    public Set<String> getRequiredPermissions() {
        return new HashSet<>();

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

    public boolean isOutside() {
        return mIsOutside;
    }

    public void setOutside(boolean isOutside) {
        saveProperty(KEY_INTERVAL_IS_OUT, Boolean.toString(isOutside));
        this.mIsOutside = isOutside;
    }
}
