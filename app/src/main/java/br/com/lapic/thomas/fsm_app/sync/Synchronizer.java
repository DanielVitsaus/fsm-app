package br.com.lapic.thomas.fsm_app.sync;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import br.com.lapic.thomas.fsm_app.data.model.Anchor;
import br.com.lapic.thomas.fsm_app.data.model.Group;
import br.com.lapic.thomas.fsm_app.data.model.Media;
import br.com.lapic.thomas.fsm_app.multicast.MulticastGroup;
import br.com.lapic.thomas.fsm_app.utils.AppConstants;

/**
 * Created by thomas on 09/09/17.
 */

public class Synchronizer extends Thread {

    private Group mGroup;
    private MulticastGroup mMulticastGroup;
    private final String TAG = this.getClass().getSimpleName();
    private Handler handler1;
    private Handler handler2;

    public Synchronizer(Group group, MulticastGroup multicastGroup, Handler h1, Handler h2) {
        this.mGroup = group;
        this.mMulticastGroup = multicastGroup;
        this.handler1 = h1;
        this.handler2 = h2;
    }

    @Override
    public void run() {
        for (final Anchor anchor : mGroup.getAnchors()) {
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendMessage(AppConstants.START, anchor.getMedias());
                }
            }, anchor.getBegin() * 1000);

            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendMessage(AppConstants.STOP, anchor.getMedias());
                }
            }, anchor.getEnd() * 1000);
            Log.e(TAG, anchor.getMedia(0) + " " +  anchor.getBegin() + " " +anchor.getEnd());
        }

    }

    public void sendMessage(String action, ArrayList<String> medias) {
        String str = action + ":";
        for (String media : medias) {
            Media mMedia = mGroup.getMedia(media);
            str += mMedia.getId() + "," + mMedia.getType() + "," + mMedia.getDuration() + "," + mMedia.getSrc() + "+";
        }
        try {
            mMulticastGroup.sendMessage(false, str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
