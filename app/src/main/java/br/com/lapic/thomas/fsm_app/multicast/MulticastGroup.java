package br.com.lapic.thomas.fsm_app.multicast;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import br.com.lapic.thomas.fsm_app.helper.StringHelper;
import br.com.lapic.thomas.fsm_app.player.PlayerFragment;
import br.com.lapic.thomas.fsm_app.ui.primarymode.PrimaryModeActivity;
import br.com.lapic.thomas.fsm_app.ui.primarymode.PrimaryModePresenter;
import br.com.lapic.thomas.fsm_app.ui.secondarymode.SecondaryModeActivity;
import br.com.lapic.thomas.fsm_app.ui.secondarymode.SecondaryModePresenter;
import br.com.lapic.thomas.fsm_app.utils.AppConstants;

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
                secondaryModePresenter.showDialogChoiceGroup(message[0], message[1]);
            }
        } else if (playerFragment != null) {
            if (incomingMessage.getTag().equals(AppConstants.PLAY)) {
                Log.e(TAG, incomingMessage.getMessage());
            }
        }
        return null;
    }
}
