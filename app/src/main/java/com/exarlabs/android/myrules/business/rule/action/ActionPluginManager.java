package com.exarlabs.android.myrules.business.rule.action;

import java.util.LinkedHashMap;
import java.util.Map;

import com.exarlabs.android.myrules.ui.actions.ActionPluginFragment;

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

    /*
     * We use 2 maps to associate action codes and plugins with type
     */
    private Map<Integer, Action.Type> mActionTypeToTypeMap;
    private Map<Class<? extends ActionPlugin>, Action.Type> mActionPluginToTypeMap;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------


    public ActionPluginManager() {
        /*
         * For each type it links the id and plugin to the type and generates a new instance.
         */

        mActionTypeToTypeMap = new LinkedHashMap<>();
        mActionPluginToTypeMap = new LinkedHashMap<>();

        // Fill in the maps with the values
        for (Action.Type type : Action.Type.values()) {

            // link the id with the type
            mActionTypeToTypeMap.put(type.getType(), type);

            // Link the plugin with the type
            mActionPluginToTypeMap.put(type.getPlugin(), type);
        }
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    /**
     * Creator for the plugins.
     *
     * @param code
     * @return a new action plugin, if no plugin found a default is created.
     */
    public ActionPlugin createNewPluginInstance(int code) {
        try {
            Action.Type type = getFromActionTypeCode(code);
            return type.getPlugin().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Creator for the action plugin fragments.
     *
     * @param code
     * @return a new action plugin fragment, if no plugin found a default is created
     */
    public ActionPluginFragment createNewPluginFragmentInstance(int code) {

        try {
            Action.Type type = getFromActionTypeCode(code);
            return type.getPluginFragment().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * @param code
     * @return the Action.Type associated with this code
     */
    public Action.Type getFromActionTypeCode(int code) {
        return mActionTypeToTypeMap.get(code);
    }

    /**
     * @param plugin
     * @return the Action.Type associated with this plugin.
     */
    public Action.Type getFromPlugin(Class<? extends ActionPlugin> plugin) {
        return mActionPluginToTypeMap.get(plugin);
    }
}
