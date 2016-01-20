package com.android.internal.telephony;

/**
 * Created by atiyka on 2016.01.04..
 */
public interface ITelephony {
    boolean endCall();

    void answerRingingCall();

    void silenceRinger();
}
