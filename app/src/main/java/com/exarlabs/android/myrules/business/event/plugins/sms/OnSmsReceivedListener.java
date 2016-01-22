package com.exarlabs.android.myrules.business.event.plugins.sms;

/**
 * Created by atiyka on 2016.01.21..
 */
public interface OnSmsReceivedListener {
    void getSms(String sender, String message);
}
