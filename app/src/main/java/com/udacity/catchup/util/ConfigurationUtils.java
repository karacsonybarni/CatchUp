package com.udacity.catchup.util;

import android.content.Context;
import android.content.res.Configuration;

public class ConfigurationUtils {

    public static boolean isInLandscapeMode(Context context) {
        int orientation = context.getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}