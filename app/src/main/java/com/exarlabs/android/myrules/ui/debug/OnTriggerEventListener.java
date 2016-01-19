package com.exarlabs.android.myrules.ui.debug;

import com.exarlabs.android.myrules.business.event.EventHandlerPlugin;

public interface OnTriggerEventListener {
    void triggerEvent(EventHandlerPlugin event);
}
