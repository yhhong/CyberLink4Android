package com.aspirecn.upnp.stb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by yinghuihong on 16/4/20.
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "BOOT COMPLETED", Toast.LENGTH_SHORT).show();
        System.out.println("BOOT COMPLETED");
        context.startService(new Intent(context, STBService.class));
        //        context.startActivity(new Intent(context, MainActivity.class));
    }
}
