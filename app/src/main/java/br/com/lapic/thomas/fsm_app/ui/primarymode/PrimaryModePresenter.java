package br.com.lapic.thomas.fsm_app.ui.primarymode;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.inject.Inject;

import br.com.lapic.thomas.fsm_app.R;
import br.com.lapic.thomas.fsm_app.data.model.Anchor;
import br.com.lapic.thomas.fsm_app.data.model.Device;
import br.com.lapic.thomas.fsm_app.data.model.Group;
import br.com.lapic.thomas.fsm_app.data.model.GroupDevice;
import br.com.lapic.thomas.fsm_app.data.model.Media;
import br.com.lapic.thomas.fsm_app.helper.ChatConnection;
import br.com.lapic.thomas.fsm_app.helper.NsdHelper;
import br.com.lapic.thomas.fsm_app.helper.PreferencesHelper;
import br.com.lapic.thomas.fsm_app.helper.StringHelper;
import br.com.lapic.thomas.fsm_app.utils.AppConstants;

/**
 * Created by thomas on 19/08/17.
 */

public class PrimaryModePresenter
        extends MvpBasePresenter<PrimaryModeView> {

    private String TAG = this.getClass().getSimpleName();
    private ArrayList<Media> mMedias;
    private NsdHelper mNsdHelper;
    private ArrayList<GroupDevice> groupDeviceList;

    @Inject
    protected PreferencesHelper mPreferencesHelper;

    @Inject
    public PrimaryModePresenter(PreferencesHelper preferencesHelper) {
        mPreferencesHelper = preferencesHelper;
    }

    @Override
    public void attachView(PrimaryModeView view) {
        super.attachView(view);
        startChatConnection();
    }

    private void startChatConnection(){
        mUpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = msg.getData().getString("msg");
                if (message.contains(AppConstants.GET_AMOUNT_GROUPS))
                    mConnection.sendMessage(AppConstants.TOTAL_GROUPS + mMedias.get(0).getGroups().size());
                else if(message.contains(AppConstants.DEVICE))
                    addDeviceToGroup(StringHelper.getParam(message));
            }
        };
        mConnection = new ChatConnection(mUpdateHandler);
    }

    private void addDeviceToGroup(String str) {
        String[] params = str.split("#");
        Device device = new Device(params[0], params[1], params[2], params[3]);
        if (groupDeviceList.contains(Integer.parseInt(params[4])))
            groupDeviceList.get(Integer.parseInt(params[4])).addDevice(device);
        else {
            GroupDevice groupDevice = new GroupDevice(params[4]);
            groupDevice.addDevice(device);
            groupDeviceList.add(Integer.parseInt(params[4]), groupDevice);
        }
    }

    public void onLeavePrimaryMode() {
        mPreferencesHelper.clear();
        if (isViewAttached())
            getView().callModeActivity();
    }

    private String loadJSONFromAsset() throws IOException {

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), AppConstants.PATH_METADATA_FILE);
        FileInputStream fileInputStream = new FileInputStream(file);
        String json = null;

        FileChannel fileChannel = fileInputStream.getChannel();
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());

        json = Charset.defaultCharset().decode(mappedByteBuffer).toString();
        fileInputStream.close();

        return json;

//        String json = null;
//        InputStream is = getView().getAssetManager().open("content.json");
//        int size = is.available();
//        byte[] buffer = new byte[size];
//        is.read(buffer);
//        is.close();
//        json = new String(buffer, "UTF-8");
//        return json;
    }

    private boolean documentParser() throws IOException, JSONException {
        mMedias = new ArrayList<>();
        JSONObject obj = new JSONObject(loadJSONFromAsset());
        JSONArray mediasJSONArray = obj.getJSONArray(AppConstants.MEDIAS);
        if (mediasJSONArray != null) {
            for (int i=0; i < mediasJSONArray.length(); i++) { // Medias externas
                JSONObject mediaObject = mediasJSONArray.getJSONObject(i);
                Media media = new Media();
                media.setId(mediaObject.getString(AppConstants.ID));
                media.setType(mediaObject.getString(AppConstants.TYPE));
                media.setSrc(mediaObject.getString(AppConstants.SRC));
                if (media.getType().equals(AppConstants.IMAGE) || media.getType().equals(AppConstants.URL))
                    media.setDuration(Integer.parseInt(StringHelper.removeAllChar(mediaObject.getString(AppConstants.DUR))));
                JSONArray groupsJSONArray = mediaObject.getJSONArray(AppConstants.GROUPS);
                if (groupsJSONArray != null) {
                    ArrayList<Group> groups = new ArrayList<>();
                    for (int j=0; j < groupsJSONArray.length(); j++) { //groups de uma media
                        Group group = new Group();
                        JSONObject groupJSONObject = groupsJSONArray.getJSONObject(j);
                        JSONArray subMediasArray = groupJSONObject.getJSONArray(AppConstants.MEDIAS);
                        if (subMediasArray != null) {
                            for (int k=0; k < subMediasArray.length(); k++) { // medias de um group
                                JSONObject subMediaObject = subMediasArray.getJSONObject(k);
                                Media subMedia = new Media();
                                subMedia.setId(subMediaObject.getString(AppConstants.ID));
                                subMedia.setSrc(subMediaObject.getString(AppConstants.SRC));
                                subMedia.setType(subMediaObject.getString(AppConstants.TYPE));
                                group.addMedia(subMedia);
                            }
                        } else {
                            Log.e(TAG, "Erro ao ler subMediasArray da media: " + j);
                            return false;
                        }
                        JSONArray anchorsArray = groupJSONObject.getJSONArray(AppConstants.ANCHORS);
                        if (anchorsArray != null) {
                            for (int k=0; k < anchorsArray.length(); k++) { // anchors de um group
                                JSONObject anchorObject = anchorsArray.getJSONObject(k);
                                Anchor anchor = new Anchor();
                                anchor.setId(anchorObject.getString(AppConstants.ID));
                                anchor.setBegin(Integer.parseInt(StringHelper.removeAllChar(anchorObject.getString(AppConstants.BEGIN))));
                                anchor.setEnd(Integer.parseInt(StringHelper.removeAllChar(anchorObject.getString(AppConstants.END))));
                                JSONArray mediasIdJSONArray = anchorObject.getJSONArray(AppConstants.MEDIAS);
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

    public void onPermissionsOk(Context context) {
        if (isViewAttached()) {
            getView().showLoading(R.string.reading_document);
            try {
                getView().showLoading(R.string.reading_document);
                if (documentParser()) {
                    if (groupDeviceList == null)
                        groupDeviceList = new ArrayList<>();
                    getView().setListMedias(mMedias);
                    registerService(context);
                    getView().showContent();
                } else
                    onError(R.string.error_parsing);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                onError(R.string.error_parsing);
            }
        }
    }

    public void onStart() {
        if (isViewAttached()) {
            getView().checkPermissions();
        }
    }

    public void onDestroy() {
        if (mNsdHelper != null)
            mNsdHelper.tearDown();
        if (mConnection != null)
            mConnection.tearDown();
        mNsdHelper = null;
        mConnection = null;
    }

    private Handler mUpdateHandler;
    private ChatConnection mConnection;

    private void registerService(Context context) throws IOException {
        if ((mNsdHelper == null) && (mConnection.getLocalPort() > -1)) {
            mNsdHelper = new NsdHelper(context);
            mNsdHelper.registerService(this, mConnection.getLocalPort());
        } else
            Log.e(TAG, "Nsd is instancied or ServerSocket isn't bound.");
    }

    public void onSuccessRegisteredService(NsdServiceInfo serviceInfo) {
        if (isViewAttached()) {
            getView().initWifiP2P();
            getView().hideLoading();
            getView().showContent();
        }
    }

    public void onError(int resIdMessage) {
        if (isViewAttached()) {
            getView().hideLoading();
            getView().showError(resIdMessage);
        }
    }

    public void onClickStart() {
        if (isViewAttached())
            getView().callPlayer();
    }



}
