package br.com.lapic.thomas.fsm_app.ui.secondarymode;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import br.com.lapic.thomas.fsm_app.R;
import br.com.lapic.thomas.fsm_app.connection.ClientRxThread;
import br.com.lapic.thomas.fsm_app.connection.ConfigConnection;
import br.com.lapic.thomas.fsm_app.helper.NsdHelper;
import br.com.lapic.thomas.fsm_app.helper.PreferencesHelper;
import br.com.lapic.thomas.fsm_app.helper.StringHelper;
import br.com.lapic.thomas.fsm_app.multicast.MulticastGroup;
import br.com.lapic.thomas.fsm_app.utils.AppConstants;

/**
 * Created by thomas on 19/08/17.
 */

public class SecondaryModePresenter extends MvpBasePresenter<SecondaryModeView> {

    private final String TAG = this.getClass().getSimpleName();
    private int mGroup = -1;
    private NsdHelper mNsdHelper;
    private Handler mUpdateHandler;
    private ConfigConnection mConnection;
    private NsdServiceInfo mServiceInfo;
    private int SocketServerPORT = 8080;
    private MulticastGroup mainMulticastGroup;
    private String hostIp;

    @Inject
    protected PreferencesHelper mPreferencesHelper;

    @Inject
    public SecondaryModePresenter(PreferencesHelper preferencesHelper) {
        mPreferencesHelper = preferencesHelper;
    }

    @Override
    public void attachView(SecondaryModeView view) {
        super.attachView(view);
        startChatConnection();
        startDiscoveryMulticastGroup();
    }

    private void startDiscoveryMulticastGroup() {
        mainMulticastGroup = new MulticastGroup(this,
                getView().getMyContext(),
                AppConstants.GROUP_CONFIG,
                AppConstants.CONFIG_MULTICAST_IP,
                AppConstants.CONFIG_MULTICAST_PORT);
        mainMulticastGroup.startMessageReceiver();
    }

    private void startChatConnection() {
        mUpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = msg.getData().getString("msg");
//                if (message.contains(AppConstants.TOTAL_GROUPS)) {
//                    showDialogChoiceGroup(StringHelper.getParam(message));
//                }
//                Log.e(TAG, message);
            }
        };
        mConnection = new ConfigConnection(mUpdateHandler);
    }

    public void showDialogChoiceGroup(String amountGroups, String hostIP) {
        this.hostIp = hostIP;
        if (isViewAttached() && mGroup < 0) {
            getView().showDialogChoiceGroup(Integer.parseInt(amountGroups));
        }
    }

    public void onLeaveSecondaryMode() {
        mPreferencesHelper.clear();
        if (isViewAttached())
            getView().callModeActivity();
    }

    public void onShowHistoric() {
        if (isViewAttached())
            getView().showHistoric();
    }

    public void onStart(Context context) {
        if (isViewAttached()) {
            getView().checkPermissions();
            getView().showLoading(R.string.Looking_primary_device);
            discoverService(context);
        }
    }

    private void discoverService(Context context) {
        mNsdHelper = new NsdHelper(context);
        mNsdHelper.discoverServices(this);
    }

    public void onPause() {
        if (mNsdHelper != null) {
            mNsdHelper.stopDiscovery();
        }
    }

    public void onResume() {
    }

    public void onDestroy() {
        mConnection.tearDown();
    }

    private void onSuccess() {
        if (isViewAttached()) {
            getView().hideLoading();
        }
    }

    public void onError(int resIdMessage) {
        if (isViewAttached()) {
            getView().hideLoading();
            getView().showError(resIdMessage);
        }
    }

    public void onResolveSuccess(NsdServiceInfo nsdServiceInfo) {
        if (isViewAttached()) {
            mServiceInfo = nsdServiceInfo;
            connectToServer(mServiceInfo);
            getView().hideLoading();
        }
    }

    private void sendFile() {
        if (mServiceInfo != null) {
            ClientRxThread clientRxThread = new ClientRxThread(mServiceInfo.getHost().getHostAddress(), SocketServerPORT);
            clientRxThread.start();
        } else {
            Log.e(TAG, "NsdServiceInfo not found");
        }
    }

    private void connectToServer(NsdServiceInfo mService) {
        if (mService != null) {
            Log.d(TAG, "Connecting. port: " + mService.getPort());
            mConnection.connectToServer(mService.getHost(), mService.getPort());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mConnection.sendMessage(AppConstants.GET_AMOUNT_GROUPS);
                }
            }, 1000);
        } else {
            Log.d(TAG, "No service to connect to!");
        }
    }


    public void setGroup(final int group) {
        this.mGroup = group;
        if (isViewAttached()) {
            Bundle bundle = new Bundle();
            bundle.putInt(AppConstants.MY_GROUP, mGroup);
            getView().startFragmentPlayer(bundle);
        }
        // TODO Startar fragmento e iniciar multicast group
//        if (mConnection != null && mNsdHelper != null) {
//            new Thread(new Runnable() {
//                public void run() {
//                    mConnection.sendMessage(AppConstants.DEVICE +
//                            StringHelper.getDeviceName() + "#" +
//                            mNsdHelper.getMacAddress() + "#" +
//                            mNsdHelper.getLocalIpAddress() + "#" +
//                            mConnection.getLocalPort() + "#" +
//                            group);
//                }
//            }).start();
//            sendFile();
//        }
    }

    public void onPermissionsOk(Context context) {
        Log.e(TAG, "permission ok");
    }
}
