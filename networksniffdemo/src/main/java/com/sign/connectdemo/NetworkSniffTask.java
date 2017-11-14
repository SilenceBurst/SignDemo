package com.sign.connectdemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.net.InetAddress;

/**
 * 获得同一局域网下的所有连接设备
 */
public class NetworkSniffTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "NetworkSniffTask";

    private WeakReference<Context> mContextRef;

    public NetworkSniffTask(Context context) {
        mContextRef = new WeakReference<Context>(context);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Context context = mContextRef.get();

            if (context != null) {
                //获取网络连接信息
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                Log.d(TAG, "activeNetwork: " + String.valueOf(activeNetwork));
                //wifi信息
                WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo connectionInfo = wm.getConnectionInfo();
                //获得当前设备的ip地址
                int ipAddress = connectionInfo.getIpAddress();
                String ipString = Formatter.formatIpAddress(ipAddress);
                Log.d(TAG, "ipString: " + String.valueOf(ipString));
                //获取ip地址的网段
                String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
                Log.d(TAG, "prefix: " + prefix);
                //0是保留的表示网络的，而255是当前网络的广播地址
                for (int i = 1; i < 255; i++) {
                    String testIp = prefix + String.valueOf(i);

                    InetAddress address = InetAddress.getByName(testIp);
                    boolean reachable = address.isReachable(1000);
                    String hostName = address.getCanonicalHostName();

                    if (reachable) {
                        Log.i(TAG, "Host: " + String.valueOf(hostName) + "(" + String.valueOf(testIp) + ") is reachable!");
                    }
                }
            }
        } catch (Throwable t) {
            Log.e(TAG, "Well that's not good.", t);
        }
        return null;
    }
}
