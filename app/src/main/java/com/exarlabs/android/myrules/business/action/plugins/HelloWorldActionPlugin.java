package com.exarlabs.android.myrules.business.action.plugins;

import java.util.List;

import android.util.Log;

import com.exarlabs.android.myrules.business.action.ActionPlugin;
import com.exarlabs.android.myrules.business.event.Event;
import com.exarlabs.android.myrules.model.dao.RuleActionProperty;

/**
 * Example action plugin
 * Created by becze on 1/11/2016.
 */
public class HelloWorldActionPlugin implements ActionPlugin {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = HelloWorldActionPlugin.class.getSimpleName();
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
    public void initialize(List<RuleActionProperty> properties) {

    }

    @Override
    public List<RuleActionProperty> generateProperties() {
        return null;
    }

    @Override
    public boolean run(Event event) {
        Log.w(TAG, "run: " + this + " Hello World!");
        return true;
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
