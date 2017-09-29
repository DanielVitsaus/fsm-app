package br.com.lapic.thomas.syncplayer.ui.secondarymode;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import br.com.lapic.thomas.syncplayer.R;
import br.com.lapic.thomas.syncplayer.connection.ClientRxThread;
import br.com.lapic.thomas.syncplayer.helper.PreferencesHelper;
import br.com.lapic.thomas.syncplayer.multicast.MulticastGroup;
import br.com.lapic.thomas.syncplayer.utils.AppConstants;

/**
 * Created by thomas on 19/08/17.
 */

public class SecondaryModePresenter extends MvpBasePresenter<SecondaryModeView> {

    private final String TAG = this.getClass().getSimpleName();
    private int mGroup = -1;
    private MulticastGroup mainMulticastGroup;
    private MulticastGroup downloadMulticastGroup;
    private ArrayList<String> mediasToDownload;
    private int amountGroups;
    private int mediaDownloadCount = 0;
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

    private void startDownloadMulticastGroup() {
        downloadMulticastGroup = new MulticastGroup(this,
                getView().getMyContext(),
                AppConstants.TO_DOWNLOAD,
                AppConstants.DOWNLOAD_MULTCAST_IP,
                AppConstants.DOWNLOAD_MULTICAST_PORT);
        downloadMulticastGroup.startMessageReceiver();
    }

    public void showDialogChoiceGroup(String totalGroups, String hostIP) {
        this.hostIp = hostIP;
        if (isViewAttached() && mGroup < 0) {
            this.amountGroups = Integer.parseInt(totalGroups);
            getView().showDialogChoiceGroup(amountGroups);
        }
    }

    public void setMediasToDownload(String[] medias) {
        downloadMulticastGroup.stopMessageReceiver();
        String[] mediastoDownload = medias[mGroup-1].split(",");
        mediasToDownload = new ArrayList<>();
        Collections.addAll(mediasToDownload, mediastoDownload);
        sendFile();
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
                int socketPort = AppConstants.DOWNLOAD_SOCKET_PORT + ((mGroup-1) * 100) + i;
                ClientRxThread clientRxThread = new ClientRxThread(this, hostIp, socketPort, mediasToDownload.get(i));
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
            bundle.putInt(AppConstants.MY_GROUP, mGroup);
            if (isViewAttached()) getView().startFragmentPlayer(bundle);
        } else {
            if (isViewAttached())
                getView().incrementProgressBar((mediaDownloadCount*100) / mediasToDownload.size());
        }
    }


    public void setGroup(final int group) {
        this.mGroup = group;
        startDownloadMulticastGroup();
    }

    public void onPermissionsOk(Context context) {
        Log.e(TAG, "permission ok");
    }
}
