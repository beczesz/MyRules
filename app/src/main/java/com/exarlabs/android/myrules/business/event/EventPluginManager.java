package com.exarlabs.android.myrules.business.event;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.exarlabs.android.myrules.business.event.plugins.call.CallEventHandlerPlugin;
import com.exarlabs.android.myrules.business.event.plugins.math.NumberEventHandlerPlugin;
import com.exarlabs.android.myrules.business.event.plugins.sms.SmsEventHandlerPlugin;

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
        mPluginMap = new LinkedHashMap<>();

        // Add the plugins
        mPluginMap.put(NumberEventHandlerPlugin.class, new NumberEventHandlerPlugin());
        mPluginMap.put(SmsEventHandlerPlugin.class, new SmsEventHandlerPlugin());
        mPluginMap.put(CallEventHandlerPlugin.class, new CallEventHandlerPlugin());

    }

    /**
     * @return the list of plugins
     */
    public ArrayList<EventHandlerPlugin> getPlugins() {
        return new ArrayList<>(mPluginMap.values());
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
