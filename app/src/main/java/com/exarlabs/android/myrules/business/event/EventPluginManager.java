package com.exarlabs.android.myrules.business.event;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.exarlabs.android.myrules.business.event.plugins.math.NumberEventHandlerPlugin;

/**
 * The plugin manager keeps track of al the plugins written and their actual state.
 * Created by becze on 1/15/2016.
 */
public class EventPluginManager {

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

    private Map<Class<? extends EventHandlerPlugin>, EventHandlerPlugin> mPluginMap;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------


    public EventPluginManager() {
        mPluginMap = new HashMap<>();

        // Add the plugins
        mPluginMap.put(NumberEventHandlerPlugin.class, new NumberEventHandlerPlugin());
    }

    /**
     * @return the list of plugins
     */
    public Collection<EventHandlerPlugin> getPlugins() {
        return mPluginMap.values();
    }

    /**
     * Returns the instance of the plugin.
     *
     * @param key
     * @return
     */
    public EventHandlerPlugin get(Class<? extends EventHandlerPlugin> key) {
        return mPluginMap.get(key);
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
