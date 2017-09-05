package br.com.lapic.thomas.fsm_app.ui.secondarymode;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import br.com.lapic.thomas.fsm_app.R;
import br.com.lapic.thomas.fsm_app.helper.ChatConnection;
import br.com.lapic.thomas.fsm_app.helper.NsdHelper;
import br.com.lapic.thomas.fsm_app.helper.PreferencesHelper;
import br.com.lapic.thomas.fsm_app.helper.StringHelper;
import br.com.lapic.thomas.fsm_app.utils.AppConstants;

/**
 * Created by thomas on 19/08/17.
 */

public class SecondaryModePresenter extends MvpBasePresenter<SecondaryModeView> {

    private final String TAG = this.getClass().getSimpleName();
    private int mGroup = -1;
    private NsdHelper mNsdHelper;
    private Handler mUpdateHandler;
    private ChatConnection mConnection;

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
    }

    private void startChatConnection() {
        mUpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = msg.getData().getString("msg");
                if (message.contains(AppConstants.TOTAL_GROUPS)) {
                    showDialogChoiceGroup(StringHelper.getParam(message));
                }
                Log.e(TAG, message);
            }
        };
        mConnection = new ChatConnection(mUpdateHandler);
    }

    private void showDialogChoiceGroup(String amountGroups) {
        if (isViewAttached()) {
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

    public void onServiceFound(NsdServiceInfo service) {
        if (isViewAttached()) {
            getView().hideLoading();
            getView().showMessage("Service: " + service.getServiceName() + " IP: " + service.getHost());
        }
    }

    public void onResolveSuccess(NsdServiceInfo mService) {
        if (isViewAttached()) {
            connectToServer(mService);
            getView().initWifiP2P(mService);
            getView().hideLoading();
        }
    }

    private void connectToServer(NsdServiceInfo mService) {
        if (mService != null) {
            Log.d(TAG, "Connecting.");
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


    public void setGroup(int group) {
        this.mGroup = group;
//        if (mConnection != null) {
//            mConnection.sendMessage(AppConstants.DEVICE);
//        }
    }
}
