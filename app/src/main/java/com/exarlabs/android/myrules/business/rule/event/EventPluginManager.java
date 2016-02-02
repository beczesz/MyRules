package com.exarlabs.android.myrules.business.rule.event;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    /*
     * We use 3 maps to associate event codes, event, plugins with type
     */
    private Map<Integer, Event.Type> mEventcodeToTypeMap;
    private Map<Class<? extends Event>, Event.Type> mEventToTypeMap;
    private Map<Class<? extends EventHandlerPlugin>, Event.Type> mEventPluginToTypeMap;


    /*
     * For each plugin we also keep one instance of that plugin.
     */
    private Map<Event.Type, EventHandlerPlugin> mPluginMap;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    /**
     * For each event type it links the id, event and plugin to the type and generates a new instance.
     */
    public EventPluginManager() {
        mEventcodeToTypeMap = new LinkedHashMap<>();
        mEventToTypeMap = new LinkedHashMap<>();
        mEventPluginToTypeMap = new LinkedHashMap<>();
        mPluginMap = new LinkedHashMap<>();

        // Fill in the maps with the values
        for (Event.Type type : Event.Type.values()) {

            // link the id with the type
            mEventcodeToTypeMap.put(type.getType(), type);

            // Link the event with the type
            mEventToTypeMap.put(type.getEvent(), type);

            // Link the event plugin with the type
            mEventPluginToTypeMap.put(type.getPlugin(), type);
        }
    }

    /**
     * Initialize the plugin instances.
     */
    public void initPlugins() {

        // Fill in the maps with the values
        for (Event.Type type : Event.Type.values()) {

            // Link the type with a new instance of this plugin
            EventHandlerPlugin plugin;
            try {
                plugin = type.getPlugin().newInstance();
                mPluginMap.put(type, plugin);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a new event implementation which is assosciated to the given event code.
     *
     * @param eventCode
     * @return
     */
    public Event createNew(int eventCode) {
        try {
            Event.Type fromEventCode = getFromEventCode(eventCode);
            return fromEventCode.getEvent().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @return the list of plugins
     */
    public List<EventHandlerPlugin> getPlugins() {
        return new ArrayList<>(mPluginMap.values());
    }

    /**
     * @param type
     * @return Returns the instance of the plugin.
     */
    public EventHandlerPlugin getPluginInstance(Event.Type type) {
        return type == null ? null : mPluginMap.get(type);
    }

    /**
     * @param code
     * @return the Event.Type associated with this code
     */
    public Event.Type getFromEventCode(int code) {
        return mEventcodeToTypeMap.get(code);
    }

    /**
     * @param event
     * @return the Event.Type associated with this Event
     */
    public Event.Type getFromEvent(Class<? extends Event> event) {
        return mEventToTypeMap.get(event);
    }

    /**
     * @param eventHandlerPlugin
     * @return the Event.Type associated with this plugin.
     */
    public Event.Type getFromEventPlugin(Class<? extends EventHandlerPlugin> eventHandlerPlugin) {
        return mEventPluginToTypeMap.get(eventHandlerPlugin);
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
