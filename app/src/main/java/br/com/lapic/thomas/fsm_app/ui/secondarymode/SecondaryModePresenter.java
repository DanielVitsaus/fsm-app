package br.com.lapic.thomas.fsm_app.ui.secondarymode;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import br.com.lapic.thomas.fsm_app.R;
import br.com.lapic.thomas.fsm_app.helper.NsdHelper;
import br.com.lapic.thomas.fsm_app.helper.PreferencesHelper;

/**
 * Created by thomas on 19/08/17.
 */

public class SecondaryModePresenter extends MvpBasePresenter<SecondaryModeView> {

    NsdHelper mNsdHelper;

    @Inject
    protected PreferencesHelper mPreferencesHelper;

    @Inject
    public SecondaryModePresenter(PreferencesHelper preferencesHelper) {
        mPreferencesHelper = preferencesHelper;
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
                    
            getView().hideLoading();
        }
    }



}
