package com.fanny.bmb;

import android.app.Application;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import cn.bmob.v3.Bmob;

/**
 * Created by Fanny on 17/4/25.
 */

public class MyApp extends Application {

    private static final String TAG = "MyApp";



    @Override
    public void onCreate() {
        super.onCreate();
        //初始化bmob
        Bmob.initialize(this, "5737aead2c235755f21e71e3630359df");

        //初始化环信
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);
        options.setAutoLogin(true);
        EMClient.getInstance().init(this, options);
        EMClient.getInstance().setDebugMode(true);


    }








}
