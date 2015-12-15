package com.exarlabs.android.myrules.util;

import android.content.Context;

/**
 * Contains uitlity functions for random generations
 * Created by becze on 11/18/2015.
 */
public class RandomUtil {
    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    /**
     * @param items
     * @param <T>
     * @return Returns a random element from the array.
     */
    public static <T> T getRandom(T[] items) {
        return items[((int) (items.length * Math.random()))];
    }

    /**
     * @return Returns a random element from a string array
     */
    public static String getRandom(Context context, int stringArrRes) {
        String[] stringArray = context.getResources().getStringArray(stringArrRes);
        return getRandom(stringArray);
    }

    /**
     * @return Returns a random number in string format
     */
    public static String getRandomStringNumber(int length) {
        String number = "";
        for (int i = 0; i < length; i++) {
            number += "" + (int)(Math.random()*10);
        }
        return number;
    }

}
