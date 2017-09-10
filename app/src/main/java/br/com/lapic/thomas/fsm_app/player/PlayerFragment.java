package br.com.lapic.thomas.fsm_app.player;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
    private TextView message;
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
        message = rootView.findViewById(R.id.tv_message);
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
        multicastGroup = new MulticastGroup(null, rootView.getContext(), AppConstants.ACTION, multicastIp, multicastPort);
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

    @Override
    public void onStop() {
        multicastGroup.stopMessageReceiver();
        super.onStop();
    }

    public void playMedias(ArrayList<Media> medias) {
        for (Media media : medias) {
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
    }

    public void stopMedias(ArrayList<Media> medias) {
        for (Media media : medias) {
            switch (media.getType()) {
                case "image":
                    stopImage();
                    break;
                case "audio":
                    stopAudio();
                    break;
                case "video":
                    stopVideo();
                    break;
                case "url":
                    stopWebView();
                    break;
            }
        }
    }

    private void startImage(Media media) {
        message.setVisibility(View.GONE);
        if (imageViewAudio.getVisibility() == View.VISIBLE)
            imageViewAudio.setVisibility(View.GONE);
        stopVideo();
        stopWebView();
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), AppConstants.PATH_APP + media.getSrc());
        if (file.exists()) {
            stopImage();
            Bitmap mBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            mImageView.setImageBitmap(mBitmap);
            mImageView.setVisibility(View.VISIBLE);
        }
    }

    private void stopImage() {
        if (mImageView.getVisibility() == View.VISIBLE) {
            mImageView.setImageBitmap(null);
            mImageView.destroyDrawingCache();
            mImageView.setVisibility(View.GONE);
            showMessage();
        }
    }

    private void startAudio(Media media) {
        message.setVisibility(View.GONE);
        stopVideo();
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), AppConstants.PATH_APP + media.getSrc());
        if (file.exists()) {
            try {
                stopAudio();
                mMediaPlayer.setDataSource(file.getAbsolutePath());
                mMediaPlayer.prepare();
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        stopAudio();
                    }
                });
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
            showMessage();
        }
    }

    private void startVideo(Media media) {
        message.setVisibility(View.GONE);
        stopAll();
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
            showMessage();
        }
    }

    private void startWebView(Media media) {
        message.setVisibility(View.GONE);
        if (imageViewAudio.getVisibility() == View.VISIBLE)
            imageViewAudio.setVisibility(View.GONE);
        stopImage();
        stopVideo();
        stopWebView();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl("http://" + media.getSrc());
        mWebView.setVisibility(View.VISIBLE);
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
            mWebView.setVisibility(View.GONE);
            showMessage();
        }
    }

    private void stopAll() {
        stopImage();
        stopAudio();
        stopVideo();
        stopWebView();
    }

    private void showMessage() {
        if ((mWebView.getVisibility() == View.GONE) &&
                (mImageView.getVisibility() == View.GONE) &&
                (imageViewAudio.getVisibility() == View.GONE) &&
                (mVideoView.getVisibility() == View.GONE))
            message.setVisibility(View.VISIBLE);
    }

}
