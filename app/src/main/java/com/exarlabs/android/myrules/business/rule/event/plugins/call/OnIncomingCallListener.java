package com.exarlabs.android.myrules.business.rule.event.plugins.call;

/**
 * Forwards the incoming call to a listener class, which implements this interface
 *
 * Created by atiyka on 2016.01.22..
 */
public interface OnIncomingCallListener {
    void getCall(String caller);
}
