package app.helpers;

import android.util.Log;

/**
 * Created by Paddym1991 on 11/10/2017.
 */

public class LogHelpers {
    public static void info(Object parent, String message) {

        Log.i(parent.getClass().getSimpleName(), message);
    }
}
