package com.akp.janupkar.database;

import android.app.Activity;


public final class AppSettings extends OSettings {
    public static final String PREFS_MAIN_FILE = "PREFS_ZOZIMA_FILE";
    public static final String accessToken = "accessToken";
    public static final String descript = "descript";

    public static final String searchArray = "searchArrays";
    public static final String fcmId = "fcmId";
    public static final String Password = "Password";

    public AppSettings(Activity mActivity) {
        super(mActivity);
    }


}


