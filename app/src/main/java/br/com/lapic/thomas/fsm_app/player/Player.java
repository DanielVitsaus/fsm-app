package br.com.lapic.thomas.fsm_app.player;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import java.io.File;

import br.com.lapic.thomas.fsm_app.R;
import br.com.lapic.thomas.fsm_app.data.model.Media;
import br.com.lapic.thomas.fsm_app.ui.primarymode.PrimaryModePresenter;
import br.com.lapic.thomas.fsm_app.utils.AppConstants;

/**
 * Created by thomas on 26/08/17.
 */

public class Player implements MediaPlayer.OnCompletionListener{

    private final String TAG = this.getClass().getSimpleName();
    private PrimaryModePresenter mPrimaryModePresenter;
    private Context mContext;
    private Media mMedia;
    private VideoView mVideoView;
    private Button mButtonStart;

    public Player(PrimaryModePresenter primaryModePresenter, Context context) {
        mPrimaryModePresenter = primaryModePresenter;
        mContext = context;
        Activity activity = (Activity) mContext;
        mVideoView = activity.findViewById(R.id.video_view);
        mButtonStart = activity.findViewById(R.id.start_button);
    }

    public void setMedia(Media media) {
        mMedia = media;
    }

    public void play(boolean withController) {
        switch (mMedia.getType()) {
            case "image":

                break;
            case "audio":

                break;
            case "video":
                playVideo(withController);
                break;
            case "url":

                break;
            default:
                Log.e(TAG, mContext.getString(R.string.media_type_invalid));
                break;
        }
    }

    private void playVideo(boolean withController) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), AppConstants.PATH_APP + mMedia.getSrc());
        mVideoView.setVideoPath(file.getAbsolutePath());
        mVideoView.setMediaController(new MediaController(mContext));
        mVideoView.setOnCompletionListener(this);
        mVideoView.setVisibility(View.VISIBLE);
        mButtonStart.setVisibility(View.GONE);
        mVideoView.start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mPrimaryModePresenter.onEndMedia();
    }

}
