package br.com.lapic.thomas.fsm_app.ui.primarymode;

import android.util.Log;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import br.com.lapic.thomas.fsm_app.R;
import br.com.lapic.thomas.fsm_app.data.model.Media;
import br.com.lapic.thomas.fsm_app.helper.PreferencesHelper;

/**
 * Created by thomas on 19/08/17.
 */

public class PrimaryModePresenter
        extends MvpBasePresenter<PrimaryModeView> {

    private static final String MEDIAS = "medias";
    private static final String ID = "id";
    private static final String SRC = "src";
    private static final String TYPE = "type";
    private String TAG = this.getClass().getSimpleName();

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

    private void documentParser() throws IOException, JSONException {
        JSONObject obj = new JSONObject(loadJSONFromAsset());
//        Log.e(TAG, obj.toString());
        JSONArray mediasJSONArray = obj.getJSONArray(MEDIAS);
        if (mediasJSONArray != null) {
            for (int i=0; i < mediasJSONArray.length(); i++) {
                JSONObject mediaObject = mediasJSONArray.getJSONObject(i);
                Media media = new Media();
                media.setId(mediaObject.getString(ID));
                media.setType(mediaObject.getString(TYPE));
                media.setSrc(mediaObject.getString(SRC));
            }
        } else
            Log.e(TAG, "ERRO ao ler JSONARRAYMedias");
    }

    public void onStart() {
        try {
            getView().showLoading();
            documentParser();
            getView().hideLoading();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            getView().showError(R.string.error_parsing);
        }
    }
}
