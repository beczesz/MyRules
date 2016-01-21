package com.exarlabs.android.myrules.business.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.exarlabs.android.myrules.business.action.plugins.FibonacciActionPlugin;

/**
 * The plugin manager keeps track of al the plugins written and their actual state.
 * Created by atiyka on 1/19/2016.
 */
public class ActionPluginManager {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private Map<Class<? extends ActionPlugin>, ActionPlugin> mPluginMap;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------


    public ActionPluginManager() {
        mPluginMap = new HashMap<>();

        // Add the plugins
        mPluginMap.put(FibonacciActionPlugin.class, new FibonacciActionPlugin());
    }

    /**
     * @return the list of plugins
     */
    public Collection<ActionPlugin> getPlugins() {
        return mPluginMap.values();
    }

    /**
     * Returns the instance of the plugin.
     *
     * @param key
     * @return
     */
    public ActionPlugin get(Class<? extends ActionPlugin> key) {
        return mPluginMap.get(key);
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
