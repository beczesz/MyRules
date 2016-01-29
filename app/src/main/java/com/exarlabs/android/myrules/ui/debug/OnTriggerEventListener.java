package com.exarlabs.android.myrules.ui.debug;

import com.exarlabs.android.myrules.business.rule.event.EventHandlerPlugin;

/**
 * Is the listener for the button clicks on trigger event button
 * Created by atiyka on 2016.01.19..
 */
public interface OnTriggerEventListener {
    void triggerEvent(EventHandlerPlugin event);
}
