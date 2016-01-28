package com.exarlabs.android.myrules.business.condition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.exarlabs.android.myrules.business.condition.plugins.AlwaysFalseConditionPlugin;
import com.exarlabs.android.myrules.business.condition.plugins.AlwaysTrueConditionPlugin;
import com.exarlabs.android.myrules.business.condition.plugins.math.IsNumberEqualConditionPlugin;
import com.exarlabs.android.myrules.business.condition.plugins.math.IsNumberInIntervalConditionPlugin;
import com.exarlabs.android.myrules.business.condition.plugins.math.IsNumberPrimeConditionPlugin;

/**
 * The plugin manager keeps track of al the plugins written and their actual state.
 * Created by atiyka on 1/19/2016.
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

    private Map<Class<? extends ConditionPlugin>, ConditionPlugin> mPluginMap;

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------


    public ConditionPluginManager() {
        mPluginMap = new LinkedHashMap<>();

        // Add the plugins
        mPluginMap.put(AlwaysTrueConditionPlugin.class, ConditionPluginFactory.create(Condition.Type.DEBUG_ALWAYS_TRUE));
        mPluginMap.put(AlwaysFalseConditionPlugin.class, ConditionPluginFactory.create(Condition.Type.DEBUG_ALWAYS_FALSE));
        mPluginMap.put(IsNumberEqualConditionPlugin.class, ConditionPluginFactory.create(Condition.Type.ARITHMETRIC_IS_NUMBER_EQUAL));
        mPluginMap.put(IsNumberInIntervalConditionPlugin.class, ConditionPluginFactory.create(Condition.Type.ARITHMETRIC_IS_NUMBER_IN_INTERVAL));
        mPluginMap.put(IsNumberPrimeConditionPlugin.class, ConditionPluginFactory.create(Condition.Type.ARITHMETRIC_IS_NUMBER_PRIME));

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
    public Collection<ConditionPlugin> getPlugins() {
        return mPluginMap.values();
    }

    /**
     * Returns the instance of the plugin.
     *
     * @param key
     * @return
     */
    public ConditionPlugin get(Class<? extends ConditionPlugin> key) {
        return mPluginMap.get(key);
    }

    /**
     * Returns the plugin type from the given position in the map
     *
     * @param position
     * @return
     */
    public int getTypeByPosition(int position){
        List<ConditionPlugin> plugins = new ArrayList<>(mPluginMap.values());
        return plugins.get(position).getPluginType();
    }

    /**
     * Returns the position in the map
     *
     * @param type
     * @return
     */
    public int getPositionInMap(int type){
        Class className = ConditionPluginFactory.create(type).getClass();
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
