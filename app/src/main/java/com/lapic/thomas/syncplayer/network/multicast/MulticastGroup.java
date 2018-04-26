package com.lapic.thomas.syncplayer.network.multicast;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;

import com.lapic.thomas.syncplayer.data.model.Media;
import com.lapic.thomas.syncplayer.player.Player;
import com.lapic.thomas.syncplayer.player.PlayerFragment;
import com.lapic.thomas.syncplayer.player.VideoVLCActivity;
import com.lapic.thomas.syncplayer.ui.primarymode.PrimaryModeActivity;
import com.lapic.thomas.syncplayer.ui.primarymode.PrimaryModePresenter;
import com.lapic.thomas.syncplayer.ui.secondarymode.SecondaryModePresenter;
import com.lapic.thomas.syncplayer.utils.AppConstants;

/**
 * Created by thomas on 09/09/17.
 */

public class MulticastGroup extends MulticastManager {

    private final String TAG = this.getClass().getSimpleName();
    private PrimaryModePresenter primaryModePresenter;
    private SecondaryModePresenter secondaryModePresenter;
    private PlayerFragment playerFragment;
    private Context mContext;
    private Player player;

    public MulticastGroup(MvpBasePresenter basePresenter, Context context, String tag, String multicastIp, int multicastPort) {
        super(context, tag, multicastIp, multicastPort);
        this.mContext = context;
        if (basePresenter instanceof PrimaryModePresenter)
            primaryModePresenter = (PrimaryModePresenter) basePresenter;
        else if (basePresenter instanceof SecondaryModePresenter)
            secondaryModePresenter = (SecondaryModePresenter)basePresenter;
    }

    public MulticastGroup(Player player, String tag, String multicastIp, int multicastPort) {
        super(player, tag, multicastIp, multicastPort);
        this.player = player;
    }

    public void setPlayerFragment(PlayerFragment fragment) {
        this.playerFragment = fragment;
    }

    @Override
    protected Runnable getIncomingMessageAnalyseRunnable() {

        if (player != null) {
            Log.e(TAG, incomingMessage.getMessage());
            player.nextVideo(Integer.parseInt(incomingMessage.getMessage()));
        }

        if (primaryModePresenter != null) {
            primaryModePresenter.showActionFromSecondDevice(incomingMessage.getMessage());
        } else if (secondaryModePresenter != null) {
            if (incomingMessage.getTag().equals(AppConstants.GROUP_CONFIG)) {
                String[] msgSplited = incomingMessage.getMessage().split("/");
                secondaryModePresenter.setPathApp("/" + msgSplited[3]);
                secondaryModePresenter.showDialogChoiceGroup(msgSplited[0], msgSplited[1], msgSplited[2]);
            } else if (incomingMessage.getTag().equals(AppConstants.TO_DOWNLOAD)) {
                String message = incomingMessage.getMessage();
                Log.e(TAG, incomingMessage.getMessage());
                String[] mediasToDownload = message.split("/");
                secondaryModePresenter.setMediasToDownload(mediasToDownload);
            }
        } else if (playerFragment != null) {
            if (incomingMessage.getTag().equals(AppConstants.ACTION)) {
                final String msg = incomingMessage.getMessage();
                if (msg.contains("rtp://")) { //deve fazer o streaming
                    String[] msgSplited = msg.split(" ");
                    String action = msgSplited[0];
                    String param = msgSplited[1];
                    if (action.equals(AppConstants.START)) {
                        Log.e(TAG, "DEVE INICIAR");
                        Intent intent = new Intent(mContext, VideoVLCActivity.class);
                        intent.putExtra(AppConstants.STREAMING_URL, param);
                        mContext.startActivity(intent);
                    } else if (action.contains(AppConstants.STOP)) {
                        Log.e(TAG, "DEVE PARAR");
                        playerFragment.sendMessageToFinishVideoVLCActivity();
                    }
                    Log.e(TAG, msg);
                } else if (msg.contains("START:app:")) {
                    final String urlInstantApp = msg.replace("START:app:","");
                    playerFragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Uri uri = Uri.parse(urlInstantApp);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            playerFragment.getActivity().startActivity(intent);
                        }
                    });
                } else if (msg.contains("STOP:app")) {

                } else {
                    //START:media2,video,-1,medias/media2.mp4+
                    String param = msg.substring(msg.indexOf(":") + 1);
                    //media2,video,-1,medias/media2.mp4+
                    String[] mediasString = param.split("\\+");
                    //media2,video,-1,medias/media2.mp4
                    final ArrayList<Media> arrayListMedias = new ArrayList<>();
                    for (int i = 0; i < mediasString.length; i++) {
                        String[] mediaStr = mediasString[i].split(",");
                        Media media = new Media();
                        media.setId(mediaStr[0]);
                        media.setType(mediaStr[1]);
                        media.setDuration(Integer.parseInt(mediaStr[2]));
                        media.setSrc(mediaStr[3]);
                        arrayListMedias.add(media);
                    }
                    if (msg.contains(AppConstants.START)) {
                        playerFragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                playerFragment.playMedias(arrayListMedias);
                            }
                        });
                    } else {
                        playerFragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                playerFragment.stopMedias(arrayListMedias);
                            }
                        });
                    }
                }
            }
        }
        return null;
    }
}
