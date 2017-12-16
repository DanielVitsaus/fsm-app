package br.com.lapic.thomas.syncplayer.ui.secondarymode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import br.com.lapic.thomas.syncplayer.R;
import br.com.lapic.thomas.syncplayer.data.model.App;
import br.com.lapic.thomas.syncplayer.helper.StringHelper;
import br.com.lapic.thomas.syncplayer.network.unicast.ClientRxThread;
import br.com.lapic.thomas.syncplayer.helper.PreferencesHelper;
import br.com.lapic.thomas.syncplayer.network.multicast.MulticastGroup;
import br.com.lapic.thomas.syncplayer.utils.AppConstants;

/**
 * Created by thomas on 19/08/17.
 */

public class SecondaryModePresenter extends MvpBasePresenter<SecondaryModeView> {

    private final String TAG = this.getClass().getSimpleName();
    private int mGroupNumber = -1;
    private MulticastGroup mainMulticastGroup;
    private MulticastGroup deviceClassMulticastGroup;
    private MulticastGroup downloadMulticastGroup;
    private ArrayList<String> mediasToDownload;
    private int amountGroups;
    private int mediaDownloadCount = 0;
    private String hostIp;
    private String pathApp;
    private String[] mClassesDevices;

    @Inject
    protected PreferencesHelper mPreferencesHelper;

    @Inject
    public SecondaryModePresenter(PreferencesHelper preferencesHelper) {
        mPreferencesHelper = preferencesHelper;
    }

    @Override
    public void attachView(SecondaryModeView view) {
        super.attachView(view);
        startDiscoveryMulticastGroup();
    }

    private void startDiscoveryMulticastGroup() {
        mainMulticastGroup = new MulticastGroup(this,
                getView().getMyContext(),
                AppConstants.GROUP_CONFIG,
                AppConstants.CONFIG_MULTICAST_IP,
                AppConstants.CONFIG_MULTICAST_PORT);
        Log.e(TAG, AppConstants.CONFIG_MULTICAST_IP);
        mainMulticastGroup.startMessageReceiver();
    }

    private void startMulticastGroupDeviceClass() {
        deviceClassMulticastGroup = new MulticastGroup(this,
                getView().getMyContext(),
                AppConstants.GROUP_DEVICE_CLASS,
                StringHelper.incrementIp(AppConstants.CONFIG_MULTICAST_IP, mGroupNumber),
                AppConstants.CONFIG_MULTICAST_PORT);
        deviceClassMulticastGroup.startMessageReceiver();
    }

    private void startDownloadMulticastGroup() {
        downloadMulticastGroup = new MulticastGroup(this,
                getView().getMyContext(),
                AppConstants.TO_DOWNLOAD,
                AppConstants.DOWNLOAD_MULTCAST_IP,
                AppConstants.DOWNLOAD_MULTICAST_PORT);
        Log.e(TAG, AppConstants.DOWNLOAD_MULTCAST_IP);
        downloadMulticastGroup.startMessageReceiver();
    }

    public void showDialogChoiceGroup(String totalGroups, String classesDevices, String hostIP) {
        this.hostIp = hostIP;
        this.mClassesDevices = classesDevices.split(",");
        if (isViewAttached() && mGroupNumber < 0) {
            this.amountGroups = Integer.parseInt(totalGroups);
            getView().showDialogChoiceGroup(amountGroups, mClassesDevices);
        }
    }

    public void setMediasToDownload(String[] medias) {
        downloadMulticastGroup.stopMessageReceiver();
        for (int i = 0; i < medias.length; i++) {
            String[] mediasSplited = medias[i].split(":");
            Log.e(TAG, mediasSplited[0]);
            Log.e(TAG, mediasSplited[1]);
            if (Integer.parseInt(mediasSplited[0]) == mGroupNumber) {
                String[] mediastoDownload = mediasSplited[1].split(",");
                mediasToDownload = new ArrayList<>();
                Collections.addAll(mediasToDownload, mediastoDownload);
                sendFile();
            }
        }
//        String[] mediastoDownload = medias[mGroupNumber -1].split(",");
//        mediasToDownload = new ArrayList<>();
//        Collections.addAll(mediasToDownload, mediastoDownload);
//        sendFile();
    }

    public void onLeaveSecondaryMode() {
        mPreferencesHelper.clear();
        if (isViewAttached())
            getView().callModeActivity();
    }

    public void onStart(Context context) {
        if (isViewAttached()) {
            getView().checkPermissions();
            getView().showLoading(R.string.Looking_primary_device);
        }
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

    private void sendFile() {
        if (hostIp != null && isViewAttached()) {
            getView().startDownloadMedias();
            ArrayList<ClientRxThread> listClientRxThread = new ArrayList<>();
            for (int i = 0; i < mediasToDownload.size(); i++) {
                int socketPort = AppConstants.DOWNLOAD_SOCKET_PORT + ((mGroupNumber -1) * 100) + i;
                ClientRxThread clientRxThread = new ClientRxThread(this, hostIp, socketPort, pathApp, mediasToDownload.get(i));
                clientRxThread.start();
                listClientRxThread.add(clientRxThread);
            }
        } else {
            Log.e(TAG, "Host IP is null");
        }
    }

    public void onMediaDownloadFinished() {
        this.mediaDownloadCount++;
        if (mediaDownloadCount == mediasToDownload.size()) {
            Bundle bundle = new Bundle();
            bundle.putInt(AppConstants.MY_GROUP, mGroupNumber);
            if (isViewAttached()) getView().startFragmentPlayer(bundle);
        } else {
            if (isViewAttached())
                getView().incrementProgressBar((mediaDownloadCount*100) / mediasToDownload.size());
        }
    }


    public void setGroup(final int groupNumber, int typeClassDevice) {
        this.mGroupNumber = groupNumber;
        if (typeClassDevice == AppConstants.PASSIVE_CLASS) {
            startMulticastGroupDeviceClass();
            Bundle bundle = new Bundle();
            bundle.putInt(AppConstants.MY_GROUP, mGroupNumber);
            if (isViewAttached()) getView().startFragmentPlayer(bundle);
        } else {
            startDownloadMulticastGroup();
        }
    }

    public void setPathApp(final String pathApp) {
        AppConstants.PATH_APP = pathApp;
        this.pathApp = pathApp;
    }

    public void onPermissionsOk(Context context) {
        Log.e(TAG, "permission ok");
    }

    public void onStop() {
        if (deviceClassMulticastGroup != null)
            deviceClassMulticastGroup.stopMessageReceiver();
        if (mainMulticastGroup != null)
            mainMulticastGroup.stopMessageReceiver();
        if (downloadMulticastGroup != null)
            downloadMulticastGroup.stopMessageReceiver();
    }
}
