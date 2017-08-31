package br.com.lapic.thomas.fsm_app.player;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import br.com.lapic.thomas.fsm_app.R;
import br.com.lapic.thomas.fsm_app.data.model.Media;
import br.com.lapic.thomas.fsm_app.utils.AppConstants;

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

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        configStatusBar();
        setContentView(R.layout.player_activity);
        mMedias = getIntent().getParcelableArrayListExtra(AppConstants.MEDIAS_PARCEL);
        if (mMedias != null) {
            mImageView = findViewById(R.id.image_view);
            imageViewAudio = findViewById(R.id.image_audio);
            mMediaPlayer = new MediaPlayer();
            mVideoView = findViewById(R.id.video_view);
            mWebView = findViewById(R.id.web_view);
            playMedia(mMedias.get(indexCurrentMedia));
        }
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
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (mMedias.size() > indexCurrentMedia)
            playMedia(mMedias.get(indexCurrentMedia));
        else
            finish();
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
                    if (mMedias.size() > indexCurrentMedia)
                        playMedia(mMedias.get(indexCurrentMedia));
                    else
                        finish();
                }
            }, 10000);
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
            mMediaPlayer.release();
        }
    }

    private void startVideo(Media media) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), AppConstants.PATH_APP + media.getSrc());
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
        mWebView.loadUrl("http://" + media.getSrc());
        mWebView.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mMedias.size() > indexCurrentMedia)
                    playMedia(mMedias.get(indexCurrentMedia));
                else
                    finish();
            }
        }, 10000);
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
