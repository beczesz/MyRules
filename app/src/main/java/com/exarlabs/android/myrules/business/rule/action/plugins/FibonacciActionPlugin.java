package com.exarlabs.android.myrules.business.rule.action.plugins;

import android.util.Log;

import com.exarlabs.android.myrules.business.rule.action.ActionPlugin;
import com.exarlabs.android.myrules.business.rule.event.Event;
import com.exarlabs.android.myrules.business.rule.event.plugins.math.NumberEvent;

/**
 * Example action plugin which calculates a Fibonacci number
 * Created by becze on 1/11/2016.
 */
public class FibonacciActionPlugin extends ActionPlugin {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = FibonacciActionPlugin.class.getSimpleName();
    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------
    private long mResult;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------
    public FibonacciActionPlugin(int type){
        super(type);
    }
    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    @Override
    public boolean run(Event event) {
        if (event instanceof NumberEvent) {
            int value = ((NumberEvent) event).getValue();
            mResult = fib(value);
            Log.w(TAG, "Fib of: " + value + " is " + mResult);
        }
        return true;
    }


    /**
     * Naiva implementation of fibonacci
     *
     * @param n
     * @return
     */
    public long fib(int n) {
        if (n <= 1) return n;
        else return fib(n - 1) + fib(n - 2);
    }

    @Override
    public String toString() {
        return "Calculates Fibonacci";
    }

    @Override
    public String[] getRequiredPermissions() {
        return new String[] {};
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------

    public long getResult() {
        return mResult;
    }
}
