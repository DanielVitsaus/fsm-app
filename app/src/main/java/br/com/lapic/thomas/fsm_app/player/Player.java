package br.com.lapic.thomas.fsm_app.player;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;

import br.com.lapic.thomas.fsm_app.R;
import br.com.lapic.thomas.fsm_app.data.model.Media;
import br.com.lapic.thomas.fsm_app.utils.AppConstants;

/**
 * Created by thomas on 28/08/17.
 */

public class Player extends Activity implements MediaPlayer.OnCompletionListener {

    private final String TAG = this.getClass().getSimpleName();
    private ArrayList<Media> mMedias;
    private VideoView mVideoView;
    private ImageView mImageView;
    private WebView mWebView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        configStatusBar();
        setContentView(R.layout.player_activity);
        mMedias = getIntent().getParcelableArrayListExtra(AppConstants.MEDIAS_PARCEL);
        if (mMedias != null) {
            mVideoView = findViewById(R.id.video_view);
            mImageView = findViewById(R.id.image_view);
            mWebView = findViewById(R.id.web_view);
            startVideo();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
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

    private void startVideo() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), AppConstants.PATH_APP + "medias/media1.mp4");
        mVideoView.setVideoPath(file.getAbsolutePath());
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.setOnCompletionListener(this);
        mImageView.setVisibility(View.GONE);
        mWebView.setVisibility(View.GONE);
        mVideoView.setVisibility(View.VISIBLE);
        mVideoView.start();
    }

}
