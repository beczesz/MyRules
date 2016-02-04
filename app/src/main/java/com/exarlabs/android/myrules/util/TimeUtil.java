package com.exarlabs.android.myrules.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Time utility methods.
 * Created by jarek on 2/3/16.
 */
public class TimeUtil {
    public static final Calendar CALENDAR = Calendar.getInstance();
    /**
     * Pattern for displaying the time.
     */
    private static final String TIME_PATTERN = "HH:mm";
    public static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat(TIME_PATTERN);

    // ------------------------------------------------------------------------
    // TYPES
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    /**
     * @return minutes since 00:00 today
     */

    public static int currentTimeToMinutes() {
        return CALENDAR.get(Calendar.HOUR_OF_DAY) * 60 + CALENDAR.get(Calendar.MINUTE);
    }

    public static int hourMinuteToMinutes(int hour, int minute) {
        return hour * 60 + minute;
    }


    /**
     * @param hour
     * @param minute
     * @return formatted string of the time
     */
    public static String hourMinuteToString(int hour, int minute) {
        CALENDAR.set(Calendar.HOUR_OF_DAY, hour);
        CALENDAR.set(Calendar.MINUTE, minute);
        return TIME_FORMATTER.format(CALENDAR.getTime());
    }

    /**
     * @param intTime time in minutes
     * @return formatted string of the time
     */
    public static String minutesToString(int intTime) {
        return hourMinuteToString(intTime / 60, intTime % 60);
    }

    /**
     * @param timeString
     * @return minutes as int
     */
    //TODO to handle exeption
    public static int stringToMinutes(String timeString) {
        try {
            CALENDAR.setTime(TIME_FORMATTER.parse(timeString));
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
        return CALENDAR.get(Calendar.HOUR_OF_DAY) * 60 + CALENDAR.get(Calendar.MINUTE);
    }

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
