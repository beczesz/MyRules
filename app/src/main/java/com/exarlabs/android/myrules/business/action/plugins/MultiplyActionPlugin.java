package com.exarlabs.android.myrules.business.action.plugins;

import java.util.List;

import android.util.Log;

import com.exarlabs.android.myrules.business.action.ActionPlugin;
import com.exarlabs.android.myrules.business.event.Event;
import com.exarlabs.android.myrules.business.event.plugins.math.NumberEvent;
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

    public MultiplyActionPlugin(int type){
        super(type);
    }
    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------
    @Override
    public void initialize(List<RuleActionProperty> properties) {
        super.initialize(properties);

        mValue = Double.parseDouble(getProperty(KEY_VALUE).getValue());
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
}
