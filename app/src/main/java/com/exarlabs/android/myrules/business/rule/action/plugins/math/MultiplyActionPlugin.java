package com.exarlabs.android.myrules.business.rule.action.plugins.math;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.util.Log;

import com.exarlabs.android.myrules.business.rule.RuleComponentProperty;
import com.exarlabs.android.myrules.business.rule.action.ActionPlugin;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.business.rule.event.plugins.math.NumberEvent;
import com.exarlabs.android.myrules.model.dao.RuleActionProperty;

/**
 * Example action plugin which calculates the multiplication of a number
 * Created by becze on 1/11/2016.
 */
public class MultiplyActionPlugin extends ActionPlugin {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = MultiplyActionPlugin.class.getSimpleName();
    private static final String KEY_VALUE = "VALUE";

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    private double mResult;
    private double mValue;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------
    @Override
    public void initialize(List<? extends RuleComponentProperty> properties) {
        super.initialize(properties);

        RuleActionProperty value = getProperty(KEY_VALUE);
        mValue = value != null ? Double.parseDouble(value.getValue()) : 0;
    }


    @Override
    public boolean run(Event event) {
        if (event instanceof NumberEvent) {
            int value = ((NumberEvent) event).getValue();
            mResult = mValue * value;
            Log.w(TAG, "" + value + " x " + mValue + " = " + mResult);
        }
        return true;
    }

    @Override
    public String toString() {
        return "Multiply with " + mValue;
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------

    public double getResult() {
        return mResult;
    }

    public double getValue() {

        return mValue;
    }

    public void setValue(double value) {
        saveProperty(KEY_VALUE, Double.toString(value));
        mValue = value;
    }

    @Override
    public Set<String> getRequiredPermissions() {
        return new HashSet<>();
    }
}
