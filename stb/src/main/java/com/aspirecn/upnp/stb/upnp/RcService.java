package com.aspirecn.upnp.stb.upnp;

import android.util.Log;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.StateVariable;
import org.cybergarage.upnp.control.ActionListener;

/**
 * 遥控器
 * Created by yinghuihong on 16/5/3.
 */
public class RcService implements ActionListener {

    private final static String TAG = RcService.class.getSimpleName();

    private StateVariable mKeyCode;

    public RcService(Device device) {
        Action rc = device.getAction("RC");
        rc.setActionListener(this);

        mKeyCode = device.getStateVariable("KeyCode");
    }

    @Override
    public boolean actionControlReceived(Action action) {
        if (action.getName().equals("RC")) {
            Argument keyCodeArg = action.getArgument("KeyCode");
            mKeyCode.setValue(keyCodeArg.getValue());// Send Events
            // TODO implements operator key code
            Log.d(TAG, "Execute key code : " + keyCodeArg.getValue());

            Argument resultArg = action.getArgument("Result");
            resultArg.setValue("成功");
            return true;
        }
        return false;
    }

}
