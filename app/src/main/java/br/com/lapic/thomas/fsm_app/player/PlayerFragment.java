package br.com.lapic.thomas.fsm_app.player;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;

import br.com.lapic.thomas.fsm_app.R;
import br.com.lapic.thomas.fsm_app.data.model.Media;
import br.com.lapic.thomas.fsm_app.helper.StringHelper;
import br.com.lapic.thomas.fsm_app.multicast.MulticastGroup;
import br.com.lapic.thomas.fsm_app.utils.AppConstants;

/**
 * Created by thomas on 09/09/17.
 */

public class PlayerFragment extends Fragment {

    private ImageView mImageView;
    private ImageView imageViewAudio;
    private VideoView mVideoView;
    private WebView mWebView;

    private View rootView;
    private MulticastGroup multicastGroup;
    private MediaPlayer mMediaPlayer;
    private int mGroup;
    private String TAG = this.getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_player_layout, parent, false);
        mImageView = rootView.findViewById(R.id.image_view);
        imageViewAudio = rootView.findViewById(R.id.image_audio);
        mMediaPlayer = new MediaPlayer();
        mVideoView = rootView.findViewById(R.id.video_view);
        mWebView = rootView.findViewById(R.id.web_view);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.mGroup = getArguments().getInt(AppConstants.MY_GROUP);
        startMulticastGroup();
    }

    private void startMulticastGroup() {
        String multicastIp = StringHelper.incrementIp(AppConstants.CONFIG_MULTICAST_IP, mGroup);
        int multicastPort = AppConstants.CONFIG_MULTICAST_PORT + mGroup;
        multicastGroup = new MulticastGroup(null,
                rootView.getContext(),
                AppConstants.PLAY,
                StringHelper.incrementIp(AppConstants.CONFIG_MULTICAST_IP, mGroup),
                AppConstants.CONFIG_MULTICAST_PORT + mGroup);
        multicastGroup.setPlayerFragment(this);
        multicastGroup.startMessageReceiver();
    }

    @Override
    public void onDestroy() {
        stopImage();
        stopAudio();
        stopVideo();
        stopWebView();
        super.onDestroy();
    }

    public void playMedia(Media media) {
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
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), AppConstants.PATH_APP + media.getSrc());
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
                    stopImage();
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
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), AppConstants.PATH_APP + media.getSrc());
        if (file.exists()) {
            try {
                mMediaPlayer.setDataSource(file.getAbsolutePath());
                mMediaPlayer.prepare();
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        stopAudio();
                    }
                });
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
            mMediaPlayer.release();
        }
    }

    private void startVideo(Media media) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), AppConstants.PATH_APP + media.getSrc());
        if (file.exists()) {
            mVideoView.setVideoPath(file.getAbsolutePath());
            mVideoView.setMediaController(new MediaController(rootView.getContext()));
            mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stopVideo();
                }
            });
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
                stopWebView();
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
}
