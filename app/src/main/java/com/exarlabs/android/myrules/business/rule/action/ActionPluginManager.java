package com.exarlabs.android.myrules.business.rule.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.exarlabs.android.myrules.business.rule.action.plugins.FibonacciActionPlugin;
import com.exarlabs.android.myrules.business.rule.action.plugins.MultiplyActionPlugin;
import com.exarlabs.android.myrules.business.rule.action.plugins.RejectCallActionPlugin;
import com.exarlabs.android.myrules.business.rule.action.plugins.SendSmsActionPlugin;

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
        mPluginMap = new LinkedHashMap<>();

        // Add the plugins
        mPluginMap.put(FibonacciActionPlugin.class,  ActionPluginFactory.create(Action.Type.ARITHMETRIC_ACTION_FIBONACCI));
        mPluginMap.put(MultiplyActionPlugin.class,   ActionPluginFactory.create(Action.Type.ARITHMETRIC_ACTION_MULTIPLY));
        mPluginMap.put(RejectCallActionPlugin.class, ActionPluginFactory.create(Action.Type.REJECT_CALL_ACTION));
        mPluginMap.put(SendSmsActionPlugin.class,    ActionPluginFactory.create(Action.Type.SEND_SMS_ACTION));

    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------

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

    /**
     * Returns the plugin type from the given position in the map
     *
     * @param position
     * @return
     */
    public int getTypeByPosition(int position){
        List<ActionPlugin> plugins = new ArrayList<>(mPluginMap.values());
        return plugins.get(position).getType();
    }

    /**
     * Returns the position in the map
     *
     * @param type
     * @return
     */
    public int getPositionInMap(int type){
        Class className = ActionPluginFactory.create(type).getClass();
        Iterator iterator = mPluginMap.keySet().iterator();

        int i = 0;
        while(iterator.hasNext()) {
            if (iterator.next().equals(className))
                return i;
            i++;
        }

        return 0;
    }
}
