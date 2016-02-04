package com.exarlabs.android.myrules.business.rule.event.plugins.call;

import com.exarlabs.android.myrules.model.contact.Contact;

/**
 * Forwards the incoming call to a listener class, which implements this interface
 * <p>
 * Created by atiyka on 2016.01.22..
 */
public interface OnIncomingCallListener {


    /**
     * Callback on new incoming call
     *
     * @param caller
     */
    void onIncomingCall(Contact caller);
}
