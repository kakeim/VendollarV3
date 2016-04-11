package com.example.kevin.vendollarv3;

import android.app.Application;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.log.LogLevel;

/**
 * Created by Kevin on 2/26/2016.
 */
public class VendollarV3App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        KontaktSDK.initialize("wvFaZmilypJIauSMJMYDQEDCekHhoyTc")
                .setDebugLoggingEnabled(BuildConfig.DEBUG)
                .setLogLevelEnabled(LogLevel.DEBUG, true);;
    }
}
