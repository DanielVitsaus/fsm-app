package br.com.lapic.thomas.syncplayer.multicast;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;

import br.com.lapic.thomas.syncplayer.data.model.Media;
import br.com.lapic.thomas.syncplayer.player.PlayerFragment;
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

    public MulticastGroup(MvpBasePresenter basePresenter, Context context, String tag, String multicastIp, int multicastPort) {
        super(context, tag, multicastIp, multicastPort);
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

        } else if (secondaryModePresenter != null) {
            if (incomingMessage.getTag().equals(AppConstants.GROUP_CONFIG)) {
                String[] message = incomingMessage.getMessage().split("/");
                secondaryModePresenter.setPathApp(message[2]);
                secondaryModePresenter.showDialogChoiceGroup(message[0], message[1]);
            } else if (incomingMessage.getTag().equals(AppConstants.TO_DOWNLOAD)) {
                String param = incomingMessage.getMessage();
                String[] mediasToDownload = param.split("/");
                secondaryModePresenter.setMediasToDownload(mediasToDownload);
            }
        } else if (playerFragment != null) {
            if (incomingMessage.getTag().equals(AppConstants.ACTION)) {
                String msg = incomingMessage.getMessage();
                String param = msg.substring(msg.indexOf(":") + 1);
                String[] mediasString = param.split("\\+");
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
        return null;
    }
}
