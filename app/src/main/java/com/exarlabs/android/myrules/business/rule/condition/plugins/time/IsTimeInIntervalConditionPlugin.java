package com.exarlabs.android.myrules.business.rule.condition.plugins.time;

import android.util.Log;

import com.exarlabs.android.myrules.business.rule.condition.plugins.math.IsNumberInIntervalConditionPlugin;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.util.TimeUtil;

/**
 * Plugin decides whether the current time is in/out time interval.
 * Created by jarek on 2/3/16.
 */
public class IsTimeInIntervalConditionPlugin extends IsNumberInIntervalConditionPlugin {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = IsTimeInIntervalConditionPlugin.class.getSimpleName();

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public boolean evaluate(Event event) {
        int value = TimeUtil.currentTimeToMinutes();
        boolean result = getMin() <= value && getMax() >= value;
        if (isOutside()) {
            result = !result;
        }
        Log.w(TAG, "Value: " + value + " is " + (isOutside() ? "outside: " : "inside: ") + result);
        return result;
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
