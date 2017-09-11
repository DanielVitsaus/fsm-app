package br.com.lapic.thomas.fsm_app.ui.primarymode;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.inject.Inject;

import br.com.lapic.thomas.fsm_app.R;
import br.com.lapic.thomas.fsm_app.connection.ServerSocketThread;
import br.com.lapic.thomas.fsm_app.data.model.Anchor;
import br.com.lapic.thomas.fsm_app.data.model.Group;
import br.com.lapic.thomas.fsm_app.data.model.Media;
import br.com.lapic.thomas.fsm_app.helper.PreferencesHelper;
import br.com.lapic.thomas.fsm_app.helper.StringHelper;
import br.com.lapic.thomas.fsm_app.multicast.MulticastGroup;
import br.com.lapic.thomas.fsm_app.utils.AppConstants;

/**
 * Created by thomas on 19/08/17.
 */

public class PrimaryModePresenter
        extends MvpBasePresenter<PrimaryModeView> {

    private String TAG = this.getClass().getSimpleName();
    private ArrayList<Media> mMedias;
    private MulticastGroup mainMulticastGroup;
    private MulticastGroup downloadMulticastGroup;

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

        File file = new File(AppConstants.FILE_PATH_DOWNLOADS, AppConstants.PATH_METADATA_FILE);
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
                    getView().setListMedias(mMedias);
                    startMulticastGroup();
                    startReceiverThread();
                    getView().showContent();
                } else
                    onError(R.string.error_parsing);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                onError(R.string.error_parsing);
            }
        }
    }

    private void startMulticastGroup() throws IOException {
        if (isViewAttached()) {
            mainMulticastGroup = new MulticastGroup(this,
                    getView().getMyContext(),
                    AppConstants.GROUP_CONFIG,
                    AppConstants.CONFIG_MULTICAST_IP,
                    AppConstants.CONFIG_MULTICAST_PORT);
            mainMulticastGroup.sendMessage(true, mMedias.get(0).getGroups().size() + getLocalHostLANAddress().toString());
            downloadMulticastGroup = new MulticastGroup(this,
                    getView().getMyContext(),
                    AppConstants.TO_DOWNLOAD,
                    AppConstants.DOWNLOAD_MULTCAST_IP,
                    AppConstants.DOWNLOAD_MULTICAST_PORT);
            String message = "";
            for (Group group : mMedias.get(0).getGroups()) {
                for (Media media : group.getMedias()) {
                    if (!media.getType().equals(AppConstants.URL))
                        message += media.getSrc().substring(media.getSrc().lastIndexOf("/")+1) + ",";
                }
                message = message.substring(0, message.length() -1);
                message += "/";
            }
            message = message.substring(0, message.length() - 1);
            downloadMulticastGroup.sendMessage(true, message);
        }
    }

    public void onStart() {
        if (isViewAttached()) {
            getView().checkPermissions();
        }
    }

    private void startReceiverThread() {
        ArrayList<ServerSocketThread> listServerThread = new ArrayList<>();
        int flagMediaUrl = 0;
        for (int i = 0; i < mMedias.get(0).getGroups().size(); i++) {
            for (int j = 0; j < mMedias.get(0).getGroup(i).getMedias().size(); j++) {
                Media media = mMedias.get(0).getGroup(i).getMedia(j);
                if (media.getType().equals(AppConstants.URL))
                    flagMediaUrl++;
                else {
                    int socketPort = (AppConstants.DOWNLOAD_SOCKET_PORT + (i * 100) + j) - flagMediaUrl;
                    ServerSocketThread serverSocketThrea = new ServerSocketThread(socketPort, media);
                    serverSocketThrea.start();
                    listServerThread.add(serverSocketThrea);
                }
            }
            flagMediaUrl = 0;
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

    public static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            return inetAddr;
                        }
                        else if (candidateAddress == null) {
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        }
        catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }

}
