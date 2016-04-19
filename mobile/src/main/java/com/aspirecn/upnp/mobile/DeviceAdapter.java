package com.aspirecn.upnp.mobile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.cybergarage.upnp.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinghuihong on 16/4/14.
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceViewHolder> {

    private Context mContext;

    private List<Device> mDevices;

    public DeviceAdapter(Context context, ArrayList<Device> devices) {
        this.mContext = context;
        this.mDevices = devices;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DeviceViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_device_item, parent, false));
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        holder.bind(mContext, mDevices.get(position));
    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }
}
