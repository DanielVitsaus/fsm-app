package br.com.lapic.thomas.syncplayer.network.multicast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;

import br.com.lapic.thomas.syncplayer.data.model.Media;
import br.com.lapic.thomas.syncplayer.player.PlayerFragment;
import br.com.lapic.thomas.syncplayer.player.VideoVLCActivity;
import br.com.lapic.thomas.syncplayer.ui.primarymode.PrimaryModeActivity;
import br.com.lapic.thomas.syncplayer.ui.primarymode.PrimaryModePresenter;
import br.com.lapic.thomas.syncplayer.ui.secondarymode.SecondaryModePresenter;
import br.com.lapic.thomas.syncplayer.utils.AppConstants;

/**
 * Created by thomas on 09/09/17.
 */

public class MulticastGroup extends MulticastManager {

    private final String TAG = this.getClass().getSimpleName();
    private PrimaryModePresenter primaryModePresenter;
    private SecondaryModePresenter secondaryModePresenter;
    private PlayerFragment playerFragment;
    private Context mContext;

    public MulticastGroup(MvpBasePresenter basePresenter, Context context, String tag, String multicastIp, int multicastPort) {
        super(context, tag, multicastIp, multicastPort);
        this.mContext = context;
        if (basePresenter instanceof PrimaryModePresenter)
            primaryModePresenter = (PrimaryModePresenter) basePresenter;
        else if (basePresenter instanceof SecondaryModePresenter)
            secondaryModePresenter = (SecondaryModePresenter)basePresenter;
    }

    public void setPlayerFragment(PlayerFragment fragment) {
        this.playerFragment = fragment;
    }

    @Override
    protected Runnable getIncomingMessageAnalyseRunnable() {

        if (primaryModePresenter != null) {

            primaryModePresenter.showActionFromSecondDevice(incomingMessage.getMessage());

        } else if (secondaryModePresenter != null) {
            if (incomingMessage.getTag().equals(AppConstants.GROUP_CONFIG)) {
                String[] msgSplited = incomingMessage.getMessage().split("/");
                secondaryModePresenter.setPathApp("/" + msgSplited[3]);
                secondaryModePresenter.showDialogChoiceGroup(msgSplited[0], msgSplited[1], msgSplited[2]);
            } else if (incomingMessage.getTag().equals(AppConstants.TO_DOWNLOAD)) {
                String message = incomingMessage.getMessage();
                String[] mediasToDownload = message.split("/");
                Log.e(TAG, mediasToDownload[0]);
                secondaryModePresenter.setMediasToDownload(mediasToDownload);
            }
        } else if (playerFragment != null) {
            if (incomingMessage.getTag().equals(AppConstants.ACTION)) {
                String msg = incomingMessage.getMessage();
                if (msg.contains("rtp://")) { //deve fazer o streaming
                    String[] msgSplited = msg.split(" ");
                    String action = msgSplited[0];
                    String param = msgSplited[1];
                    if (action.equals(AppConstants.START)) {
                        Intent intent = new Intent(mContext, VideoVLCActivity.class);
                        intent.putExtra(AppConstants.STREAMING_URL, param);
                        mContext.startActivity(intent);
                        Log.e(TAG, msg);
                    } else if (action.contains(AppConstants.STOP)) {
                        playerFragment.sendMessageToFinishVideoVLCActivity();
                    }
                    Log.e(TAG, msg);
                } else {
                    Log.e(TAG, msg);
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
