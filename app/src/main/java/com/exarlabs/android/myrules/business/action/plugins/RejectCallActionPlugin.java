package com.exarlabs.android.myrules.business.action.plugins;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.inject.Inject;

import android.content.Context;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.exarlabs.android.myrules.business.action.ActionPlugin;
import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.business.event.Event;
import com.exarlabs.android.myrules.model.dao.RuleActionProperty;

import com.android.internal.telephony.ITelephony;

/**
 * Action plugin which rejects a call
 * Created by atiyka on 1/20/2016.
 */
public class RejectCallActionPlugin extends ActionPlugin {

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    private static final String TAG = RejectCallActionPlugin.class.getSimpleName();

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    @Inject
    public Context mContext;

    private TelephonyManager telephonyManager;
    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    public RejectCallActionPlugin(){
        DaggerManager.component().inject(this);
        telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
    }

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------
    @Override
    public void initialize(List<RuleActionProperty> properties) {
        super.initialize(properties);
    }

    @Override
    public boolean run(Event event) {
        rejectCall();
        return true;
    }

    // rejects a call by finding the ITelephony methods with reflection
    private void rejectCall(){
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audioManager.getRingerMode();
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

        try {
            Class telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());

            Method iTelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");

            iTelephonyMethod.setAccessible(true);
            ITelephony telephonyService = (ITelephony) iTelephonyMethod.invoke(telephonyManager);

            telephonyService.endCall();

        } catch (ClassNotFoundException e) {
            Log.w(TAG, e);
        } catch (NoSuchMethodException e) {
            Log.w(TAG, e);
        } catch (IllegalAccessException e) {
            Log.w(TAG, e);
        } catch (InvocationTargetException e) {
            Log.w(TAG, e);
        }

        audioManager.setRingerMode(ringerMode);
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------

}
