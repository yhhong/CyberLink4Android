package com.aspirecn.upnp.stb;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;

import com.aspirecn.upnp.stb.upnp.LightService;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * main activity
 * Created by yinghuihong on 16/4/18.
 */
public class MainActivity extends AppCompatActivity implements LightService.OnStatusChangedListener {

    @Bind(R.id.cb_light)
    CheckBox cbLight;

    private STBService stbService;
    private ServiceConnection conn = new ServiceConnection() {
        /** 获取服务对象时的操作 */
        public void onServiceConnected(ComponentName name, IBinder service) {
            stbService = ((STBService.ServiceBinder) service).getService();
            stbService.setRepaintListener(MainActivity.this);
        }

        /** 无法获取到服务对象时的操作 */
        public void onServiceDisconnected(ComponentName name) {
            stbService = null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        startService(new Intent(this, STBService.class));
        bindService(new Intent(this, STBService.class), conn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
//        stopService(new Intent(this, STBService.class));
        super.onDestroy();
    }

    @Override
    public void onStatusChanged(final boolean status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cbLight.setChecked(status);
            }
        });
    }
}
