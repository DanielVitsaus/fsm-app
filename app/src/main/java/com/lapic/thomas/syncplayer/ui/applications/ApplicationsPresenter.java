package com.lapic.thomas.syncplayer.ui.applications;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.lapic.thomas.syncplayer.R;
import com.lapic.thomas.syncplayer.data.model.App;

/**
 * Created by thomasmarquesbrandaoreis on 28/09/2017.
 */

public class ApplicationsPresenter
        extends MvpBasePresenter<ApplicationsView> {

    private Map<String, App> mApplications;
    private String TAG = this.getClass().getSimpleName();

    @Inject
    public ApplicationsPresenter() {}

    @Override
    public void attachView(ApplicationsView view) {
        super.attachView(view);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
    }

    public void getApplications() {
        if (isViewAttached()) {
            getView().showLoading(R.string.searching_apps);
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<Map<String, App>> genericTypeIndicator = new GenericTypeIndicator<Map<String, App>>() {};
                    mApplications = dataSnapshot.getValue(genericTypeIndicator);
                    if (mApplications != null)
                        onSuccess(mApplications);
                    else
                        onError(R.string.error_parsing);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    private void onError(int resId) {
        if (isViewAttached()) {
            getView().hideLoading();
            getView().showError(resId);
        }
    }

    private void onSuccess(Map<String, App> mApplications) {
        if (isViewAttached()) {
            getView().showContent();
            getView().setListApplications(new ArrayList<>(mApplications.values()));
        }
    }


}
