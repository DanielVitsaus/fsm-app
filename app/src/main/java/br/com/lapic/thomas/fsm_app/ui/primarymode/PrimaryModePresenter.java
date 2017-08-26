package br.com.lapic.thomas.fsm_app.ui.primarymode;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.util.ArrayList;

import javax.inject.Inject;

import br.com.lapic.thomas.fsm_app.R;
import br.com.lapic.thomas.fsm_app.data.model.Anchor;
import br.com.lapic.thomas.fsm_app.data.model.Group;
import br.com.lapic.thomas.fsm_app.data.model.Media;
import br.com.lapic.thomas.fsm_app.helper.NsdHelper;
import br.com.lapic.thomas.fsm_app.helper.PreferencesHelper;
import br.com.lapic.thomas.fsm_app.helper.StringHelper;

/**
 * Created by thomas on 19/08/17.
 */

public class PrimaryModePresenter
        extends MvpBasePresenter<PrimaryModeView> {

    private static final String MEDIAS = "medias";
    private static final String ID = "id";
    private static final String SRC = "src";
    private static final String TYPE = "type";
    private static final String GROUPS = "groups";
    private static final String ANCHORS = "anchors";
    private static final String BEGIN = "begin";
    private static final String END = "end";
    private String TAG = this.getClass().getSimpleName();
    private ArrayList<Media> mMedias;

    NsdHelper mNsdHelper;

    @Inject
    protected PreferencesHelper mPreferencesHelper;

    @Inject
    public PrimaryModePresenter(PreferencesHelper preferencesHelper) {
        mPreferencesHelper = preferencesHelper;
    }

    @Override
    public void attachView(PrimaryModeView view) {
        super.attachView(view);
    }

    public void onLeavePrimaryMode() {
        mPreferencesHelper.clear();
        if (isViewAttached())
            getView().callModeActivity();
    }

    private String loadJSONFromAsset() throws IOException {
        String json = null;
        InputStream is = getView().getAssetManager().open("content.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        json = new String(buffer, "UTF-8");
        return json;
    }

    private boolean documentParser() throws IOException, JSONException {
        mMedias = new ArrayList<>();
        JSONObject obj = new JSONObject(loadJSONFromAsset());
        JSONArray mediasJSONArray = obj.getJSONArray(MEDIAS);
        if (mediasJSONArray != null) {
            for (int i=0; i < mediasJSONArray.length(); i++) { // Medias externas
                JSONObject mediaObject = mediasJSONArray.getJSONObject(i);
                Media media = new Media();
                media.setId(mediaObject.getString(ID));
                media.setType(mediaObject.getString(TYPE));
                media.setSrc(mediaObject.getString(SRC));
                JSONArray groupsJSONArray = mediaObject.getJSONArray(GROUPS);
                if (groupsJSONArray != null) {
                    ArrayList<Group> groups = new ArrayList<>();
                    for (int j=0; j < groupsJSONArray.length(); j++) { //groups de uma media
                        Group group = new Group();
                        JSONObject groupJSONObject = groupsJSONArray.getJSONObject(j);
                        JSONArray subMediasArray = groupJSONObject.getJSONArray(MEDIAS);
                        if (subMediasArray != null) {
                            for (int k=0; k < subMediasArray.length(); k++) { // medias de um group
                                JSONObject subMediaObject = subMediasArray.getJSONObject(k);
                                Media subMedia = new Media();
                                subMedia.setId(subMediaObject.getString(ID));
                                subMedia.setSrc(subMediaObject.getString(SRC));
                                subMedia.setType(subMediaObject.getString(TYPE));
                                group.addMedia(subMedia);
                            }
                        } else {
                            Log.e(TAG, "Erro ao ler subMediasArray da media: " + j);
                            return false;
                        }
                        JSONArray anchorsArray = groupJSONObject.getJSONArray(ANCHORS);
                        if (anchorsArray != null) {
                            for (int k=0; k < anchorsArray.length(); k++) { // anchors de um group
                                JSONObject anchorObject = anchorsArray.getJSONObject(k);
                                Anchor anchor = new Anchor();
                                anchor.setId(anchorObject.getString(ID));
                                anchor.setBegin(Integer.parseInt(StringHelper.removeAllChar(anchorObject.getString(BEGIN))));
                                anchor.setEnd(Integer.parseInt(StringHelper.removeAllChar(anchorObject.getString(END))));
                                JSONArray mediasIdJSONArray = anchorObject.getJSONArray(MEDIAS);
                                if (mediasIdJSONArray != null) {
                                    for (int w=0; w < mediasIdJSONArray.length(); w++) // medias de uma anchor
                                        anchor.addMedia(mediasIdJSONArray.getString(w));
                                } else {
                                    Log.e(TAG, "Erro ao ler mediasIdArray da anchor: " + k + " da media: " + j);
                                    return false;
                                }
                                group.addAnchor(anchor);
                            }
                        } else {
                            Log.e(TAG, "ERRO ao ler anchorsArray da media: " + j);
                            return false;
                        }
                        media.addGroup(group);
                    }
                } else {
                    Log.e(TAG, "ERRO ao ler groupsJSONArray da media: " + i);
                    return false;
                }
                mMedias.add(media);
            }
        } else {
            Log.e(TAG, "ERRO ao ler JSONARRAYMedias");
            return false;
        }
        return true;
    }

    public void onStart(Context context) {
        if (isViewAttached()) {
            try {
                getView().showLoading(R.string.reading_document);
                if (documentParser()) {
                    getView().setListMedias(mMedias);
                    registerService(context);
                } else
                    onError(R.string.error_parsing);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                onError(R.string.error_parsing);
            }
        }
    }

    public void onDestroy() {
        if (mNsdHelper != null)
            mNsdHelper.tearDown();
        mNsdHelper = null;
    }

    private void registerService(Context context) throws IOException {
        mNsdHelper = new NsdHelper(context);
        ServerSocket mServerSocket = new ServerSocket(0);
        int mLocalPort = mServerSocket.getLocalPort();
        mNsdHelper.registerService(mLocalPort);
    }

    private void onSuccess() {
        if (isViewAttached()) {
            getView().hideLoading();
        }
    }

    private void onError(int resIdMessage) {
        if (isViewAttached()) {
            getView().hideLoading();
            getView().showError(resIdMessage);
        }
    }

}
