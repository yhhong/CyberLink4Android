package com.aspirecn.upnp.stb.device;

import org.cybergarage.net.HostInterface;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.StateVariable;
import org.cybergarage.upnp.control.ActionListener;
import org.cybergarage.upnp.control.QueryListener;
import org.cybergarage.upnp.device.InvalidDescriptionException;

/**
 * Created by yinghuihong on 16/4/18.
 */
public class AppStoreDevice extends Device implements ActionListener, QueryListener {

    public AppStoreDevice(String descriptionFileName) throws InvalidDescriptionException {
        super(descriptionFileName);
        setSSDPBindAddress(HostInterface.getInetAddress(HostInterface.IPV4_BITMASK, null));
        setHTTPBindAddress(HostInterface.getInetAddress(HostInterface.IPV4_BITMASK, null));
    }

    @Override
    public boolean actionControlReceived(Action action) {
        return false;
    }

    @Override
    public boolean queryControlReceived(StateVariable stateVariable) {
        return false;
    }
}
