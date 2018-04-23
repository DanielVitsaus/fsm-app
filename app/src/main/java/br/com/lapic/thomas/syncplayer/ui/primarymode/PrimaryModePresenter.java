package br.com.lapic.thomas.syncplayer.ui.primarymode;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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

import br.com.lapic.thomas.syncplayer.R;
import br.com.lapic.thomas.syncplayer.data.model.App;
import br.com.lapic.thomas.syncplayer.network.unicast.ServerSocketThread;
import br.com.lapic.thomas.syncplayer.data.model.Anchor;
import br.com.lapic.thomas.syncplayer.data.model.Group;
import br.com.lapic.thomas.syncplayer.data.model.Media;
import br.com.lapic.thomas.syncplayer.helper.PreferencesHelper;
import br.com.lapic.thomas.syncplayer.helper.StringHelper;
import br.com.lapic.thomas.syncplayer.network.multicast.MulticastGroup;
import br.com.lapic.thomas.syncplayer.utils.AppConstants;

/**
 * Created by thomas on 19/08/17.
 */

public class PrimaryModePresenter
        extends MvpBasePresenter<PrimaryModeView> {

    private String TAG = this.getClass().getSimpleName();
    private ArrayList<Media> mMedias;
    private MulticastGroup callbackMulticastGroup;
    private MulticastGroup mainMulticastGroup;
    private MulticastGroup downloadMulticastGroup;
    private boolean useLocalApp;
    private String storageId;
    private static int fileCount = 0;
    private ArrayList<String> mediasToDownload = new ArrayList<>();

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
        mPreferencesHelper.clearMode();
        stopMulticastGroups();
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
                                anchor.setBegin(StringHelper.removeAllChar(anchorObject.getString(AppConstants.BEGIN)));
                                anchor.setEnd(StringHelper.removeAllChar(anchorObject.getString(AppConstants.END)));
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
            if (useLocalApp) {
                getView().showLoading(R.string.reading_document);
                try {
                    getView().showLoading(R.string.reading_document);
                    if (documentParser()) {
                        setStorageId("app/");
                        onSuccessPrepared();
                    } else
                        onError(R.string.error_parsing);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    onError(R.string.error_parsing);
                }
            } else {
                getView().showLoading(R.string.downloading_content_application);
                startDownloadMedias();
            }
        }
    }

    private void startDownloadMedias() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        mediasToDownload = new ArrayList<>();
        for (Media mMedia : mMedias) {
            mediasToDownload.add(storageId + mMedia.getSrc());
            for (Group group : mMedia.getGroups()) {
                for (Media media : group.getMedias()) {
                    if ((!media.getType().equals(AppConstants.URL)) && (!media.getType().equals(AppConstants.APP))) {
                        mediasToDownload.add(storageId + media.getSrc());
                    }
                }
            }
        }
        fileCount = 0;
        for (String pathMedia : mediasToDownload) {
            File folder = new File(AppConstants.FILE_PATH_DOWNLOADS, storageId + "medias");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            String fileName = pathMedia.substring(pathMedia.lastIndexOf("/") + 1);
            final File file = new File(folder, fileName);
            if (!file.exists()) {
                storageRef.child(pathMedia).getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        addCountFiles();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, file.getAbsolutePath());
                    }
                });
            } else
                addCountFiles();
        }
    }

    private void addCountFiles() {
        fileCount++;
        if (fileCount == mediasToDownload.size()) {
            onSuccessPrepared();
        }
    }

    private void startMulticastGroup() throws IOException {
        if (isViewAttached()) {
            mainMulticastGroup = new MulticastGroup(this,
                    getView().getMyContext(),
                    AppConstants.GROUP_CONFIG,
                    AppConstants.CONFIG_MULTICAST_IP,
                    AppConstants.CONFIG_MULTICAST_PORT);
//            mainMulticastGroup.sendMessage(true, mMedias.get(0).getGroups().size() +
//                            getLocalHostLANAddress().toString() + "/" +
//                            storageId.substring(0, storageId.length()-1));
            StringBuilder types = new StringBuilder();
            for (Group group : mMedias.get(0).getGroups()) {
                if (group.getMode().equals(AppConstants.MODE_PASSIVE))
                    types.append("1,");
                else {
                    String typeMedias = getMediaFormats(group);
                    types.append("2"+ typeMedias +",");
                }
            }
            types.deleteCharAt(types.length()-1);
            mainMulticastGroup.sendMessage(true, mMedias.get(0).getGroups().size() + "/" +
                    types.toString() +
                    getLocalHostLANAddress().toString() + "/" +
                    storageId.substring(0, storageId.length() -1));

            downloadMulticastGroup = new MulticastGroup(this,
                    getView().getMyContext(),
                    AppConstants.TO_DOWNLOAD,
                    AppConstants.DOWNLOAD_MULTCAST_IP,
                    AppConstants.DOWNLOAD_MULTICAST_PORT);
            String message = "";
            int groupNumber = 1;
            for (Group group : mMedias.get(0).getGroups()) {
                if (group.getMode().equals(AppConstants.MODE_ACTIVE)) {
                    message += groupNumber + ":";
                    for (Media media : group.getMedias()) {
                        if ((!media.getType().equals(AppConstants.URL)) && (!media.getType().equals(AppConstants.APP)))
                            message += media.getSrc().substring(media.getSrc().lastIndexOf("/") + 1) + ",";
                    }
                    message = message.substring(0, message.length() - 1);
                    message += "/";
                }
                groupNumber++;
            }
            message = message.substring(0, message.length() - 1);
            downloadMulticastGroup.sendMessage(true, message);
        }
    }

    private void stopMulticastGroups() {
        if (mainMulticastGroup != null)
            mainMulticastGroup.stopKeepAlive();
        if (downloadMulticastGroup != null)
            downloadMulticastGroup.stopKeepAlive();
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
                    Log.e(TAG, socketPort + "");
                    ServerSocketThread serverSocketThread = new ServerSocketThread(socketPort, storageId, media);
                    serverSocketThread.start();
                    listServerThread.add(serverSocketThread);
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

    public void onSuccessPrepared() {
        if (isViewAttached()) {
            try {
                getView().setListMedias(mMedias);
                startMulticastGroup();
                startReceiverThread();
                getView().showContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startCallbackMulticastGroup() {
        callbackMulticastGroup = new MulticastGroup(this,
                getView().getMyContext(),
                AppConstants.GROUP_CALLBACK,
                AppConstants.CALLBACK_MULTICAST_IP,
                AppConstants.CALLBACK_MULTICAST_PORT);
        callbackMulticastGroup.startMessageReceiver();
    }

    public void onClickStart() {
        if (isViewAttached()) {
            startCallbackMulticastGroup();
            getView().callPlayer();
        }
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

    public void setUseLocalApp(boolean value) {
        this.useLocalApp = value;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId + "/";
    }

    public void setMedias(ArrayList<Media> medias) {
        mMedias = medias;
    }


    public void showActionFromSecondDevice(String message) {
        if (isViewAttached())
            getView().showAlert(message);
    }

    private String getMediaFormats(Group group) {
        String formats = "(";
        for (Media media : group.getMedias()) {
            if (media.getType().equals(AppConstants.APP)) {
                formats += media.getType() + ";";
            } else {
                String ext = media.getSrc().substring(media.getSrc().lastIndexOf(".") + 1);
                if (!formats.contains(ext))
                    formats += ext + ";";
            }
        }
        formats = formats.substring(0, formats.length() - 1) + ")";
        Log.e(TAG, formats);
        return formats;
    }

}

