package com.lapic.thomas.syncplayer.player;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.lapic.thomas.syncplayer.R;
import com.lapic.thomas.syncplayer.data.model.Media;
import com.lapic.thomas.syncplayer.helper.StringHelper;
import com.lapic.thomas.syncplayer.network.multicast.MulticastGroup;
import com.lapic.thomas.syncplayer.sync.Synchronizer;
import com.lapic.thomas.syncplayer.utils.AppConstants;

/**
 * Created by thomas on 28/08/17.
 */

public class Player extends Activity implements MediaPlayer.OnCompletionListener {

    private final String TAG = this.getClass().getSimpleName();
    private int indexCurrentMedia = 0;
    private ArrayList<Media> mMedias;
    private ImageView mImageView;
    private ImageView imageViewAudio;
    private MediaPlayer mMediaPlayer;
    private VideoView mVideoView;
    private WebView mWebView;
    private ArrayList<MulticastGroup> multicastGroups;
    private ArrayList<Thread> syncs;
    private String folderApp;
    private MulticastGroup multicastGroup;
    private int level = -1;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        configStatusBar();
        setContentView(R.layout.player_activity);
        startMulticastGroupAction();
        mMedias = getIntent().getParcelableArrayListExtra(AppConstants.MEDIAS_PARCEL);
        folderApp = getIntent().getStringExtra(AppConstants.PATH_APP);
        if (mMedias != null && folderApp != null) {
            folderApp += "/";
            mImageView = findViewById(R.id.image_view);
            imageViewAudio = findViewById(R.id.image_audio);
            mMediaPlayer = new MediaPlayer();
            mVideoView = findViewById(R.id.video_view);
            mWebView = findViewById(R.id.web_view);
            playMedia(mMedias.get(indexCurrentMedia));
        }

//        Date currentTime = Calendar.getInstance().getTime();
//        Log.e(TAG, (currentTime.getTime() / 1000) + "");
//        long time= System.currentTimeMillis() / 1000;
//        Log.e(TAG, time+"");
    }

    @Override
    protected void onDestroy() {
        stopImage();
        stopAudio();
        stopVideo();
        stopWebView();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (multicastGroups == null) {
            multicastGroups = new ArrayList<>();
            syncs = new ArrayList<>();
            for (int i = 0; i < mMedias.get(0).getGroups().size(); i++) {
                String multicastIp = StringHelper.incrementIp(AppConstants.CONFIG_MULTICAST_IP, i + 1);
                int multicastPort = AppConstants.CONFIG_MULTICAST_PORT + (i + 1);
                MulticastGroup multicastGroup = new MulticastGroup(null, this, AppConstants.ACTION, multicastIp, multicastPort);
                multicastGroups.add(multicastGroup);
                Synchronizer synchronizer = new Synchronizer(this, mMedias.get(0).getGroup(i), multicastGroup, new Handler(), new Handler());
                synchronizer.start();
                syncs.add(synchronizer);
            }
        }
    }

    @Override
    protected void onStop() {
        for (MulticastGroup multicastGroup : multicastGroups) {
            multicastGroup.stopKeepAlive();
            multicastGroup.stopMessageReceiver();
        }
        super.onStop();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        onEndMedia();
    }

    private void configStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.material_green_900));
        }
    }

    private void playMedia(Media media) {
        indexCurrentMedia++;
        multicastGroup.startMessageReceiver();
        switch (media.getType()) {
            case "image":
                startImage(media);
                break;
            case "audio":
                startAudio(media);
                break;
            case "video":
                startVideo(media);
                break;
            case "url":
                startWebView(media);
                break;
        }
    }

    private void startImage(Media media) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), folderApp + media.getSrc());
        if (file.exists()) {
            Bitmap mBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            mImageView.setImageBitmap(mBitmap);
            stopVideo();
            stopAudio();
            stopWebView();
            mImageView.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onEndMedia();
                }
            }, media.getDuration() * 1000);
        }
    }

    private void stopImage() {
        if (mImageView.getVisibility() == View.VISIBLE) {
            mImageView.setImageBitmap(null);
            mImageView.destroyDrawingCache();
            mImageView.setVisibility(View.GONE);
        }
    }

    private void startAudio(Media media) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), folderApp + media.getSrc());
        if (file.exists()) {
            try {
                mMediaPlayer.setDataSource(file.getAbsolutePath());
                mMediaPlayer.prepare();
                mMediaPlayer.setOnCompletionListener(this);
                stopImage();
                stopVideo();
                stopWebView();
                imageViewAudio.setVisibility(View.VISIBLE);
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopAudio() {
        if (mMediaPlayer.isPlaying()) {
            imageViewAudio.setVisibility(View.GONE);
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }
    }

    private void startVideo(Media media) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), folderApp + media.getSrc());
        Log.e(TAG, file.getAbsolutePath());
        if (file.exists()) {
            mVideoView.setVideoPath(file.getAbsolutePath());
            mVideoView.setMediaController(new MediaController(this));
            mVideoView.setOnCompletionListener(this);
            stopImage();
            stopAudio();
            stopWebView();
            mWebView.setVisibility(View.GONE);
            mVideoView.setVisibility(View.VISIBLE);
            mVideoView.start();
        }
    }

    private void stopVideo() {
        if (mVideoView.getVisibility() == View.VISIBLE) {
            mVideoView.stopPlayback();
            mVideoView.setVideoURI(null);
            mVideoView.setVisibility(View.GONE);
        }
    }

    private void startWebView(Media media) {
        stopImage();
        stopAudio();
        stopVideo();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl("http://" + media.getSrc());
        mWebView.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onEndMedia();
            }
        }, media.getDuration() * 1000);
    }

    private void stopWebView() {
        if (mWebView.getVisibility() == View.VISIBLE) {
            mWebView.clearHistory();
            mWebView.clearCache(true);
            mWebView.loadUrl("about:blank");
            mWebView.onPause();
            mWebView.removeAllViews();
            mWebView.destroyDrawingCache();
            mWebView.pauseTimers();
            mWebView.destroy();
            mWebView.setVisibility(View.GONE);
        }
    }

    private void onEndMedia() {
        if (indexCurrentMedia == 1) {
            mVideoView.seekTo(mVideoView.getDuration() - 1);
        } else {
            multicastGroup.stopMessageReceiver();
            multicastGroup.stopKeepAlive();
            finish();
        }
//        if (mMedias.size() > indexCurrentMedia) {
//            playMedia(mMedias.get(indexCurrentMedia));
//        } else {
//            multicastGroup.stopMessageReceiver();
//            multicastGroup.stopKeepAlive();
//            finish();
//        }
    }

    private void startMulticastGroupAction() {
        String tag = "ACTION";
        String multicast_ip = "230.192.0.19";
        int port = 1050;
        multicastGroup = new MulticastGroup(this, tag, multicast_ip, port);
    }

    public void nextVideo(int which) {
        level = which;
        if (mMedias.size() > indexCurrentMedia) {
            //startGroup para iniciar instantApp
            playMedia(mMedias.get(indexCurrentMedia));
        }  else {
            multicastGroup.stopMessageReceiver();
            multicastGroup.stopKeepAlive();
            finish();
        }

//        mMediaPlayer.seekTo();

    }
}
