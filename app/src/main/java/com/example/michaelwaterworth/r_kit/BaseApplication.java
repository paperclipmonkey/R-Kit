package com.example.michaelwaterworth.r_kit;

import com.alexbbb.uploadservice.UploadService;

/**
 * Created by michaelwaterworth on 30/07/15. Copyright Michael Waterworth
 */

public class BaseApplication extends com.orm.SugarApp {

    @Override
    public void onCreate() {
        super.onCreate();
        UploadService.NAMESPACE = "com.example.michaelwaterworth.r_kit";
    }
}
