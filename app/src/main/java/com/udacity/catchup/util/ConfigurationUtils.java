package com.udacity.catchup.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.udacity.catchup.R;

public class ConfigurationUtils {

    private static ConfigurationUtils instance;

    private int usablePortraitHeight;

    private static ConfigurationUtils getInstance() {
        if (instance == null) {
            instance = new ConfigurationUtils();
        }
        return instance;
    }

    public static boolean isInLandscapeMode(Context context) {
        int orientation = context.getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static int getUsablePortraitHeight(Activity activity) {
        ConfigurationUtils configurationUtils = getInstance();
        if (configurationUtils.getUsablePortraitHeight() == 0) {
            configurationUtils
                    .setUsablePortraitHeight(configurationUtils.calculateUsableHeight(activity));
        }
        return configurationUtils.getUsablePortraitHeight();
    }

    private int getUsablePortraitHeight() {
        return usablePortraitHeight;
    }

    private void setUsablePortraitHeight(int usablePortraitHeight) {
        this.usablePortraitHeight = usablePortraitHeight;
    }

    private int calculateUsableHeight(Activity activity) {
        ConfigurationUtils configurationUtils = getInstance();
        WindowManager windowManager = activity.getWindowManager();
        int displayHeight = configurationUtils.getDisplayHeight(windowManager);
        int navigationBarHeight =
                configurationUtils.isNavigationBarShowing(activity)
                        ? configurationUtils.getNavigationBarHeight(windowManager)
                        : 0;
        int cardMargin = activity.getResources().getDimensionPixelSize(R.dimen.card_margin);
        int actionBarHeight = configurationUtils.getActionBarHeight(activity);
        return displayHeight - navigationBarHeight - actionBarHeight - (2 * cardMargin);
    }

    private int getDisplayHeight(WindowManager windowManager) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private boolean isNavigationBarShowing(Activity activity) {
        if (isInLandscapeMode(activity)) {
            return false;
        }
        Resources resources = activity.getResources();
        int id =
                resources
                        .getIdentifier(
                                "config_showNavigationBar",
                                "bool",
                                "android");
        return id > 0 && resources.getBoolean(id);
    }

    private int getNavigationBarHeight(WindowManager windowManager) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return 0;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        display.getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    private int getActionBarHeight(Activity activity) {
        final TypedArray styledAttributes =
                activity.getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int actionBarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return actionBarHeight;
    }
}