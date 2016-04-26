package com.aspirecn.upnp.stb;

import android.app.Application;

import com.aspirecn.upnp.stb.tool.CrashHandler;

/**
 * Created by yinghuihong on 16/4/25.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
    }
}
