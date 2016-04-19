package com.aspirecn.upnp.mobile;

import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.device.NotifyListener;
import org.cybergarage.upnp.device.SearchResponseListener;
import org.cybergarage.upnp.event.EventListener;
import org.cybergarage.upnp.ssdp.SSDPPacket;

/**
 * Created by yinghuihong on 16/4/13.
 */

public class UpnpDump extends ControlPoint implements NotifyListener, EventListener, SearchResponseListener
{
    ////////////////////////////////////////////////
    //	Constractor
    ////////////////////////////////////////////////

    public UpnpDump()
    {
        addNotifyListener(this);
        addSearchResponseListener(this);
        addEventListener(this);
    }

    ////////////////////////////////////////////////
    //	Listener
    ////////////////////////////////////////////////

    /**
     * @Note 设备发起Notify,控制点监听
     * @param packet
     */
    public void deviceNotifyReceived(SSDPPacket packet)
    {
        System.out.println(packet.toString());

        if (packet.isDiscover() == true) {
            String st = packet.getST();
            System.out.println("ssdp:discover : ST = " + st);
        }
        else if (packet.isAlive() == true) {
            String usn = packet.getUSN();
            String nt = packet.getNT();
            String url = packet.getLocation();
            System.out.println("ssdp:alive : uuid = " + usn + ", NT = " + nt + ", location = " + url);
        }
        else if (packet.isByeBye() == true) {
            String usn = packet.getUSN();
            String nt = packet.getNT();
            System.out.println("ssdp:byebye : uuid = " + usn + ", NT = " + nt);
        }
    }

    /**
     * @Note 控制点发起搜索,设备响应,控制点监听
     * @param packet
     */
    public void deviceSearchResponseReceived(SSDPPacket packet)
    {
        String uuid = packet.getUSN();
        String st = packet.getST();
        String url = packet.getLocation();
        System.out.println("device search res : uuid = " + uuid + ", ST = " + st + ", location = " + url);
    }

    /**
     * @Note 控制点监听,设备发起事件
     * @param uuid
     * @param seq
     * @param name
     * @param value
     */
    public void eventNotifyReceived(String uuid, long seq, String name, String value)
    {
        System.out.println("event notify : uuid = " + uuid + ", seq = " + seq + ", name = " + name + ", value =" + value);
    }

    ////////////////////////////////////////////////
    //	main
    ////////////////////////////////////////////////

    public static void main(String args[])
    {
        UpnpDump upnpDump = new UpnpDump();
        upnpDump.start();
        upnpDump.search();
    }
}
