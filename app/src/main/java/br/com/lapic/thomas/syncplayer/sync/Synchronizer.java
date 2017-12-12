package br.com.lapic.thomas.syncplayer.sync;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import br.com.lapic.thomas.syncplayer.data.model.Anchor;
import br.com.lapic.thomas.syncplayer.data.model.Group;
import br.com.lapic.thomas.syncplayer.data.model.Media;
import br.com.lapic.thomas.syncplayer.multicast.MulticastGroup;
import br.com.lapic.thomas.syncplayer.streaming.StreamingController;
import br.com.lapic.thomas.syncplayer.utils.AppConstants;

/**
 * Created by thomas on 09/09/17.
 */

public class Synchronizer extends Thread {

    private Group mGroup;
    private MulticastGroup mMulticastGroup;
    private StreamingController streamingController;
    private final String TAG = this.getClass().getSimpleName();
    private Handler handler1;
    private Handler handler2;
    private String ipPort;

    public Synchronizer(Context context, Group group, MulticastGroup multicastGroup, Handler h1, Handler h2) {
        this.mGroup = group;
        this.mMulticastGroup = multicastGroup;
        this.handler1 = h1;
        this.handler2 = h2;
        this.streamingController = new StreamingController(context);
    }

    @Override
    public void run() {
        for (final Anchor anchor : mGroup.getAnchors()) {
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ipPort = streamingController.startStreaming(anchor.getMedias());
                    if (ipPort != null)
                        sendMessage(AppConstants.START, ipPort);
                    else
                        Log.e(TAG, "ERRO, ipPort is null");
                }
            }, anchor.getBeginInt() * 1000);

            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    streamingController.stopStreaming();
                    sendMessage(AppConstants.STOP, ipPort);
                }
            }, anchor.getEndInt() * 1000);
            Log.e(TAG, anchor.getMedia(0) + " " +  anchor.getBegin() + " " +anchor.getEnd());
        }

    }

    public void sendMessage(String action, String ipPort) {
//        String str = action + ":";
//        for (String media : medias) {
//            Media mMedia = mGroup.getMedia(media);
//            str += mMedia.getId() + "," + mMedia.getType() + "," + mMedia.getDuration() + "," + mMedia.getSrc() + "+";
//        }
//        try {
//            Log.e(TAG, str);
//            mMulticastGroup.sendMessage(false, str);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String message = action + ":" + ipPort;
        try {
            Log.e(TAG, message);
            mMulticastGroup.sendMessage(false, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
