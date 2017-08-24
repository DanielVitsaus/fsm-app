package br.com.lapic.thomas.fsm_app.helper;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

/**
 * Created by thomas on 24/08/17.
 */

public class NsdHelper {

    Context mContext;
    NsdManager mNsdManager;
    NsdManager.RegistrationListener mRegistrationListener;

    public static final String SERVICE_TYPE = "_http._tcp";
    public static String mServiceName = "SyncService";
    public final String TAG = this.getClass().getSimpleName();

    public NsdHelper(Context context) {
        mContext = context;
        mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }

    public String getServiceName() {
        if (mServiceName != null)
            return mServiceName;
        else
            return "";
    }

    public void registerService(int port) {
        mRegistrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                mServiceName = NsdServiceInfo.getServiceName();
                Log.e(TAG, "Service Registered: " + mServiceName);
            }
            @Override
            public void onRegistrationFailed(NsdServiceInfo arg0, int arg1) {}
            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {}
            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {}
        };

        NsdServiceInfo serviceInfo  = new NsdServiceInfo();
        serviceInfo.setPort(port);
        serviceInfo.setServiceName(mServiceName);
        serviceInfo.setServiceType(SERVICE_TYPE);
        mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
    }

    public void tearDown() {
        mNsdManager.unregisterService(mRegistrationListener);
    }

}
