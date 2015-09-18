package com.example.michaelwaterworth.r_kit;

import com.alexbbb.uploadservice.UploadService;

/**
 * Created by michaelwaterworth on 30/07/15. Copyright Michael Waterworth
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
