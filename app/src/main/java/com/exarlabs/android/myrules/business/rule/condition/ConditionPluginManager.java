package com.exarlabs.android.myrules.business.rule.condition;


import java.util.LinkedHashMap;
import java.util.Map;

import com.exarlabs.android.myrules.ui.conditions.ConditionPluginFragment;

/**
 * Manages all the available condition plugin. It provides a single point access for every other component to the
 * condition plugins.
 * Created by becze on 12/18/2015.
 */
public class ConditionPluginManager {

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
     * We use 2 maps to associate condition codes and plugins with type
     */
    private Map<Integer, Condition.Type> mConditionTypeToTypeMap;
    private Map<Class<? extends ConditionPlugin>, Condition.Type> mConditionPluginToTypeMap;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public ConditionPluginManager() {

        /*
         * For each type it links the id and plugin to the type and generates a new instance.
         */

        mConditionTypeToTypeMap = new LinkedHashMap<>();
        mConditionPluginToTypeMap = new LinkedHashMap<>();

        // Fill in the maps with the values
        for (Condition.Type type : Condition.Type.values()) {

            // link the id with the type
            mConditionTypeToTypeMap.put(type.getType(), type);

            // Link the with the type
            mConditionPluginToTypeMap.put(type.getPlugin(), type);
        }
    }


    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    /**
     * Creator for the condition plugins.
     *
     * @param code
     * @return a new condition plugin, if no plugin found a default AlwaysTrueConditionPlugin is created.
     */
    public ConditionPlugin createNewPluginInstance(int code) {
        try {
            Condition.Type type = getFromConditionTypeCode(code);
            return type.getPlugin().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Creator for the condition plugin fragments.
     *
     * @param code
     * @return a new condition plugin fragment, if no plugin found a default is created
     */
    public ConditionPluginFragment createNewPluginFragmentInstance(int code) {

        try {
            Condition.Type fromEventCode = getFromConditionTypeCode(code);
            return fromEventCode.getPluginFragment().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * @param code
     * @return the Event.Type associated with this code
     */
    public Condition.Type getFromConditionTypeCode(int code) {
        return mConditionTypeToTypeMap.get(code);
    }

    /**
     * @param plugin
     * @return the Condition.Type associated with this plugin.
     */
    public Condition.Type getFromPlugin(Class<? extends ConditionPlugin> plugin) {
        return mConditionPluginToTypeMap.get(plugin);
    }


    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}

