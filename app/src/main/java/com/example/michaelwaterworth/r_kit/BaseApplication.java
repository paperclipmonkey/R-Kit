package com.example.michaelwaterworth.r_kit;

import com.alexbbb.uploadservice.UploadService;

/**
 * Provides the base application class by extending from SugarORM
 * Additioanlly initialises Upload Service
 */
public class BaseApplication extends com.orm.SugarApp {
    //Shared preferences filename for whole application
    public static final String SHAREDPREFERENCES = "rkit";

    @Override
    public void onCreate() {
        super.onCreate();
        UploadService.NAMESPACE = "com.example.michaelwaterworth.r_kit";
    }
}
