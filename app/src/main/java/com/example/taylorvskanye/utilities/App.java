package com.example.taylorvskanye.utilities;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharePreferencesManagerV3.init(this);
        SignalManager.init(this);
    }
}
