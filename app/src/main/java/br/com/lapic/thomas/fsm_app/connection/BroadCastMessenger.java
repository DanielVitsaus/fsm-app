package br.com.lapic.thomas.fsm_app.connection;

import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import br.com.lapic.thomas.fsm_app.utils.AppConstants;

/**
 * Created by thomas on 09/09/17.
 */

public class BroadCastMessenger {

//    public void startReceiver() {
//        try {
//            //Keep a socket open to listen to all the UDP trafic that is destined for this port
//            DatagramSocket socket = new DatagramSocket(AppConstants.CONFIG_MULTICAST_PORT, InetAddress.getByName("0.0.0.0"));
//            socket.setBroadcast(true);
//
//            while (true) {
//                Log.i(TAG,"Ready to receive broadcast packets!");
//
//                //Receive a packet
//                byte[] recvBuf = new byte[15000];
//                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
//                socket.receive(packet);
//
//                //Packet received
//                Log.i(TAG, "Packet received from: " + packet.getAddress().getHostAddress());
//                String data = new String(packet.getData()).trim();
//                Log.i(TAG, "Packet received; data: " + data);
//
//                // Send the packet data back to the UI thread
//                Intent localIntent = new Intent(AppConstants.BROADCAST_ACTION)
//                        // Puts the data into the Intent
//                        .putExtra(AppConstants.EXTENDED_DATA_STATUS, data);
//                // Broadcasts the Intent to receivers in this app.
//                LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
//            }
//        } catch (IOException ex) {
//            Log.i(TAG, "Oops" + ex.getMessage());
//        }
//    }
//
//    public void sendBroadcast(String messageStr) {
//        // Hack Prevent crash (sending should be done using an async task)
//        StrictMode.ThreadPolicy policy = new   StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        try {
//            //Open a random port to send the package
//            DatagramSocket socket = new DatagramSocket();
//            socket.setBroadcast(true);
//            byte[] sendData = messageStr.getBytes();
//            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, getBroadcastAddress(), AppConstants.CONFIG_MULTICAST_PORT);
//            socket.send(sendPacket);
//            System.out.println(getClass().getName() + "Broadcast packet sent to: " + getBroadcastAddress().getHostAddress());
//        } catch (IOException e) {
//            Log.e(TAG, "IOException: " + e.getMessage());
//        }
//    }
//
//    InetAddress getBroadcastAddress() throws IOException {
//        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        DhcpInfo dhcp = wifi.getDhcpInfo();
//        // handle null somehow
//
//        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
//        byte[] quads = new byte[4];
//        for (int k = 0; k < 4; k++)
//            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
//        return InetAddress.getByAddress(quads);
//    }

}
