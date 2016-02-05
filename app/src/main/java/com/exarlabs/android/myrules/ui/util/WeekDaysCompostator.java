package com.exarlabs.android.myrules.ui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import android.content.Context;

import com.exarlabs.android.myrules.business.dagger.DaggerManager;
import com.exarlabs.android.myrules.ui.R;

/**
 * Created by atiyka on 2016.02.05..
 */
public class WeekDaysCompostator {
    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC INITIALIZERS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    @Inject
    public Context mContext;

    // ------------------------------------------------------------------------
    // INITIALIZERS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------
    public WeekDaysCompostator(){
        DaggerManager.component().inject(this);
    }
    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------
    public int compactList(List<String> selectedDays) {
        List<String> weekDays = Arrays.asList(mContext.getResources().getStringArray(R.array.list_of_weekdays));

        // init the with binary 0
        int daysCompact = 0;
        for (String day : weekDays) {
            // if the day is selected -> adds 1
            if (selectedDays.contains(day)) {
                daysCompact += 1;
            }
            // each iteration shifts the number
            daysCompact = (daysCompact << 1);
        }
        // revert the last shift
        daysCompact = (daysCompact >> 1);

        return daysCompact;
    }

    public List<String> getListFromCompacted(int daysCompact){
        List<String> list = new ArrayList<>();
        List<String> weekDays = Arrays.asList(mContext.getResources().getStringArray(R.array.list_of_weekdays));
        for (int i = weekDays.size()-1; i >= 0; i--) {
            boolean isSet = (daysCompact & 1) == 1;
            if(isSet){
                list.add(weekDays.get(i));
            }
            daysCompact = (daysCompact >> 1);

        }

        return list;
    }
}
