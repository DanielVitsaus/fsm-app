package com.lmd.thomas.syncplayer.sync;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import com.lmd.thomas.syncplayer.data.model.Anchor;
import com.lmd.thomas.syncplayer.data.model.Group;
import com.lmd.thomas.syncplayer.data.model.Media;
import com.lmd.thomas.syncplayer.network.multicast.MulticastGroup;
import com.lmd.thomas.syncplayer.network.streaming.StreamingController;
import com.lmd.thomas.syncplayer.player.Player;
import com.lmd.thomas.syncplayer.utils.AppConstants;

/**
 * Created by thomas on 09/09/17.
 */

public class Synchronizer extends Thread {

    private final Context mContext;
    private Group mGroup;
    private MulticastGroup mMulticastGroup;
    private StreamingController streamingController;
    private final String TAG = this.getClass().getSimpleName();
    private Handler handler1;
    private Handler handler2;
    private String ipPort;

    public Synchronizer(Context context, Group group, MulticastGroup multicastGroup, Handler h1, Handler h2) {
        this.mContext = context;
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
                    if (mGroup.getMode().equals(AppConstants.MODE_PASSIVE)) {
                        ipPort = streamingController.startStreaming(anchor.getMedias());
                        if (ipPort != null)
                            sendMessage(AppConstants.START, ipPort);
                        else
                            Log.e(TAG, "ERRO, ipPort is null");
                    } else {
                        sendMessage(AppConstants.START, anchor.getMedias());
                    }
                }
            }, anchor.getBeginInt() * 1000);

            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
    //                    streamingController.stopStreaming();
                    if (mGroup.getMode().equals(AppConstants.MODE_PASSIVE))
                        sendMessage(AppConstants.STOP, ipPort);
                    else
                        sendMessage(AppConstants.STOP, anchor.getMedias());
                }
            }, anchor.getEndInt() * 1000);
            Log.e(TAG, anchor.getMedia(0) + " " +  anchor.getBegin() + " " +anchor.getEnd());
        }

    }

    public void sendMessage(String action, String ipPort) {
        String message = action + " rtp://" + ipPort;
        try {
            Log.e(TAG, message);
            mMulticastGroup.sendMessage(false, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String action, ArrayList<String> medias) {
        String str = action + ":";
        for (String media : medias) {
            if (media.contains(AppConstants.APP)) {
                str += media;
            } else if (media.contains("text:")) {
                Player player = (Player) mContext;
                if (player != null && player.level != -1)
                str += media + "_" + player.level;
            }else {
                String mediaName = media.substring(media.lastIndexOf("/") + 1, media.lastIndexOf("."));
                Media mMedia = mGroup.getMedia(mediaName);
                str += mMedia.getId() + "," + mMedia.getType() + "," + mMedia.getDuration() + "," + mMedia.getSrc() + "+";
            }
        }
        try {
            Log.e(TAG, str);
            mMulticastGroup.sendMessage(false, str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
