package com.aspirecn.upnp.stb.upnp;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.ServiceList;
import org.cybergarage.upnp.StateVariable;
import org.cybergarage.upnp.control.ActionListener;
import org.cybergarage.upnp.control.QueryListener;

/**
 * 灯泡
 * Created by yinghuihong on 16/5/3.
 */
public class LightService implements ActionListener, QueryListener {

    /**
     * 可将变更通知到subscribers
     */
    private StateVariable mPowerVar;

    public LightService(Device device) {
        Action getPowerAction = device.getAction("GetPower");
        getPowerAction.setActionListener(this);

        Action setPowerAction = device.getAction("SetPower");
        setPowerAction.setActionListener(this);

        ServiceList serviceList = device.getServiceList();
        Service service = serviceList.getService(0);
        service.setQueryListener(this);

        mPowerVar = device.getStateVariable("Power");
    }


    ////////////////////////////////////////////////
    //	on/off
    ////////////////////////////////////////////////

    /**
     * @Note 标记当前状态
     */
    private boolean onFlag = false;

    public boolean isOn() {
        return onFlag;
    }

    /**
     * @param state "on" or "off"
     * @return 成功或失败
     * @Note 更改状态
     */
    public boolean setPowerState(String state) {
        if (state == null) {
            onFlag = false;
            mPowerVar.setValue("off");
            return false;
        }
        if (state.compareTo("on") == 0) {
            onFlag = true;
            mPowerVar.setValue("on");
            return true;
        }
        if (state.compareTo("off") == 0) {
            onFlag = false;
            mPowerVar.setValue("off");
            return true;
        }
        return false;
    }

    /**
     * @return "on" or "off"
     * @Note 获取状态
     */
    public String getPowerState() {
        if (onFlag)
            return "on";
        return "off";
    }

    ////////////////////////////////////////////////
    // ActionListener	@Note 接收到控制消息,执行操作
    ////////////////////////////////////////////////

    public boolean actionControlReceived(Action action) {
        String actionName = action.getName();

        boolean ret = false;

        if (actionName.equals("GetPower")) {
            String state = getPowerState();
            Argument powerArg = action.getArgument("Power");// @Note 返回Power状态变量
            powerArg.setValue(state);// @Note 设置该action对象的数值,再将该action对象作为响应数据
            ret = true;
        }
        if (actionName.equals("SetPower")) {
            Argument powerArg = action.getArgument("Power");
            String state = powerArg.getValue();
            boolean rst = setPowerState(state);    // @Note 设置数值,并将变更通知订阅者们,返回操作结果

//			state = getPowerState(); // @Note edit by yinghuihong
            Argument resultArg = action.getArgument("Result");// @Note 返回Result状态变量,成功或失败的数值
            resultArg.setValue(rst ? "成功" : "失败");    // @Note 取当前本地数值,作为响应数值
            ret = true;
        }

        // call back to repaint ui
        if (onStatusChangedListener != null) {
            onStatusChangedListener.onStatusChanged(isOn());
        }
        return ret;
    }

    ////////////////////////////////////////////////
    // QueryListener	@Note 不同于Action,采用StateVariable
    ////////////////////////////////////////////////

    public boolean queryControlReceived(StateVariable stateVar) {
        stateVar.setValue(getPowerState());// @Note 返回当前状态
        return true;
    }

    ////////////////////////////////////////////////
    // repaint
    ////////////////////////////////////////////////

    private OnStatusChangedListener onStatusChangedListener;

    public void setOnStatusChangedListener(OnStatusChangedListener listener) {
        this.onStatusChangedListener = listener;
    }

    public interface OnStatusChangedListener {
        void onStatusChanged(boolean status);
    }


}
