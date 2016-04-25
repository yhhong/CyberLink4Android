package com.aspirecn.upnp.stb;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aspirecn.upnp.stb.device.LightDevice;
import com.aspirecn.upnp.stb.tool.AssertUtil;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * 机顶盒后台设备服务
 * Created by yinghuihong on 16/4/13.
 */
public class STBService extends Service {

    private final static String TAG = STBService.class.getSimpleName();

    private LightDevice mLightDevice;

    private IBinder iBinder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        if (iBinder == null)
            iBinder = new ServiceBinder();
        return iBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate()");

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, filter);

        // 拷贝assert资源到指定的磁盘目录
        AssertUtil.copyAssets(this, "description", getCacheDir() + "/description/");

        // 创建设备
        try {
            mLightDevice = new LightDevice(getCacheDir() + "/description/description.xml");
            mLightDevice.setLeaseTime(30 * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setRepaintListener(LightDevice.OnRepaintListener listener) {
        mLightDevice.setOnRepaintListener(listener);
    }

    private void startDevice() {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                mLightDevice.start();
            }
        }).subscribeOn(Schedulers.newThread()).subscribe();
    }

    private void stopDevice() {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                mLightDevice.stop();
            }
        }).subscribeOn(Schedulers.newThread()).subscribe();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand()");
        startDevice();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy()");
        stopDevice();
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        private ConnectivityManager connectivityManager;
        private NetworkInfo info;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.d(TAG, "网络状态已经改变");
                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
                    String name = info.getTypeName();
                    Log.d(TAG, "当前网络名称：" + name);
                    startDevice();
                } else {
                    Log.d(TAG, "没有可用网络");
                }
            }
        }
    };

    class ServiceBinder extends Binder {
        public STBService getService() {
            return STBService.this;
        }
    }
}
