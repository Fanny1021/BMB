package com.fanny.bmb.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by huang on 2017/3/16.
 */

public class SpUtil {

    private static final String name = "TalkRoom";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String IPADDRESS="ipaddress";
    public static final String IPPORT="ipport";
    private static SharedPreferences sp;

    public static String getUserAccount(){
        return getString(USERNAME);
    }

    public static String getUserToken(){
        return getString(PASSWORD);
    }
    private static String getString(String key) {
        return sp.getString(key,null);
    }


    public static void putString(Context context, String key, String value) {

        if (sp == null) {
            sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        }
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key, String value) {
        if (sp == null) {
            sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        }
        return sp.getString(key, value);
    }
}
