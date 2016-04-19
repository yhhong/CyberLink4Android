package com.aspirecn.upnp.mobile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.cybergarage.upnp.Device;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
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

    public void bind(Context context, Device device) {
        System.out.println("!!device " + device.toString());
//        Picasso.with(context).load(device.getSmallestIcon().getURL()).into(imgIcon);
        tvName.setText(device.getFriendlyName());
    }

}
