package com.aspirecn.upnp.mobile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Icon;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * view holder
 * Created by yinghuihong on 16/4/14.
 */
public class DeviceViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.img_icon)
    ImageView imgIcon;

    @Bind(R.id.tv_name)
    TextView tvName;

    public DeviceViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Context context, String host, Device device) {
        if (device.getIconList() != null && device.getIconList().size() != 0) {
            Icon icon = (Icon) device.getIconList().get(0);
            Picasso.with(context).load(host + icon.getURL()).into(imgIcon);
        }
        tvName.setText(device.getFriendlyName());
    }

}
