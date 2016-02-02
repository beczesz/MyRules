package com.exarlabs.android.myrules.business.rule.condition.plugins.math;

import java.util.HashSet;
import java.util.Set;

import android.util.Log;

import com.exarlabs.android.myrules.business.rule.condition.ConditionPlugin;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.business.rule.event.plugins.math.NumberEvent;

/**
 * Example implementation where this plugin can decide if a number is prime or not
 * Created by becze on 1/14/2016.
 */
public class IsNumberPrimeConditionPlugin extends ConditionPlugin {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = IsNumberPrimeConditionPlugin.class.getSimpleName();


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
        if (event instanceof NumberEvent) {
            int value = ((NumberEvent) event).getValue();
            boolean prime = isPrime(value);
            Log.w(TAG, "Number: " + value + " is prime -> " + prime);
            return prime;
        }

        // Always return true if we can not process this event
        return true;
    }

    //checks whether an int is prime or not.
    boolean isPrime(int n) {
        for (int i = 2; 2 * i < n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    @Override
    public Set<String> getRequiredPermissions() {
        return new HashSet<>();

    }


    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
