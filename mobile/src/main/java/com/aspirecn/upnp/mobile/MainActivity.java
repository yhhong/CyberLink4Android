package com.aspirecn.upnp.mobile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.device.NotifyListener;
import org.cybergarage.upnp.device.SearchResponseListener;
import org.cybergarage.upnp.event.EventListener;
import org.cybergarage.upnp.ssdp.SSDPPacket;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * main activity
 * Created by yinghuihong on 16/4/13.
 */
public class MainActivity extends AppCompatActivity implements NotifyListener, SearchResponseListener, EventListener {

    @Bind(R.id.rv_devices)
    RecyclerView rvDevices;

    private ControlPoint mControlPoint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        mControlPoint = new ControlPoint();
        mControlPoint.addNotifyListener(this);
        mControlPoint.addSearchResponseListener(this);
        mControlPoint.addEventListener(this);
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                mControlPoint.start();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    protected void onDestroy() {
        mControlPoint.stop();
        super.onDestroy();
    }

    private void notifyDevices(final String host) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Device> devices = new ArrayList<>();
                devices.addAll(mControlPoint.getDeviceList());

                DeviceAdapter adapter = new DeviceAdapter(MainActivity.this, host, devices);
                rvDevices.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                rvDevices.setAdapter(adapter);
            }
        });
    }

    @Override
    public void eventNotifyReceived(String uuid, long seq, String name, String value) {
        show("event notify : uuid = " + uuid + ", seq = " + seq + ", name = " + name + ", value =" + value);
    }

    @Override
    public void deviceNotifyReceived(SSDPPacket ssdpPacket) {
        System.out.println(ssdpPacket.toString());

        if (ssdpPacket.isDiscover()) {
            String st = ssdpPacket.getST();
            show("ssdp:discover : ST = " + st);
        } else if (ssdpPacket.isAlive()) {
            String usn = ssdpPacket.getUSN();
            String nt = ssdpPacket.getNT();
            String url = ssdpPacket.getLocation();
            show("ssdp:alive : uuid = " + usn + ", NT = " + nt + ", location = " + url);
        } else if (ssdpPacket.isByeBye()) {
            String usn = ssdpPacket.getUSN();
            String nt = ssdpPacket.getNT();
            show("ssdp:byebye : uuid = " + usn + ", NT = " + nt);
        }
        String location = ssdpPacket.getLocation();
        notifyDevices(location.substring(0, location.lastIndexOf("/")));
    }

    @Override
    public void deviceSearchResponseReceived(SSDPPacket ssdpPacket) {
        String uuid = ssdpPacket.getUSN();
        String st = ssdpPacket.getST();
        String location = ssdpPacket.getLocation();
        show("device search res : uuid = " + uuid + ", ST = " + st + ", location = " + location);
        notifyDevices(location.substring(0, location.lastIndexOf("/")));
    }

    private void show(final String info) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println(info);
//                Toast.makeText(MainActivity.this, info, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
