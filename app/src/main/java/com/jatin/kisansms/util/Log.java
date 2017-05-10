package com.jatin.kisansms.util;

import com.jatin.kisansms.BuildConfig;

/**
 * Created by modi on 02/03/16.
 */
public class Log {

    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static void d(String tag, String message) {
        if (DEBUG) {
            android.util.Log.d(tag, message);
        }
    }

    public static void e(String tag, String message, Exception e) {
        if (DEBUG) {
            android.util.Log.e(tag, message, e);
        }
    }

    public static void i(String tag, String message) {
        if (DEBUG) {
            android.util.Log.i(tag, message);
        }
    }

    public static void d(String message) {
        if (DEBUG)
            d("zambo",message);
    }

    public static void i(String message) {
        if (DEBUG)
            i("zambo",message);
    }

    public static void e(String message, Exception e) {
        if (DEBUG)
            e("zambo", message, e);
    }
}
