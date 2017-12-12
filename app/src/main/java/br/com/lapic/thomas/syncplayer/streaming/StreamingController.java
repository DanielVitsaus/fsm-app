package br.com.lapic.thomas.syncplayer.streaming;

import android.content.Context;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.util.ArrayList;

import br.com.lapic.thomas.syncplayer.data.model.Media;

/**
 * Created by thomasmarquesbrandaoreis on 11/12/2017.
 */

public class StreamingController {

    private Context mContext;
    private FFmpeg ffmpeg;
    private String TAG = this.getClass().getSimpleName();

    public StreamingController(Context context) {
        this.mContext = context;
        initFFmpeg();
    }

    private void initFFmpeg() {
        ffmpeg = FFmpeg.getInstance(mContext);
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onStart() {
                    Log.e(TAG, "ffmpeg started");
                }
                @Override
                public void onFailure() {
                    Log.e(TAG, "ffmpeg failure");
                }
                @Override
                public void onSuccess() {
                    Log.e(TAG, "ffmpeg Success");
                }
                @Override
                public void onFinish() {
                    Log.e(TAG, "ffmpeg onFinish");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public String startStreaming(ArrayList<String> medias) {
        String media[] = medias.get(0).split(":");
        String typeMedia = media[0];
        String mediaSrc = media[1];
        String ipPort = null;
        switch (typeMedia) {
            case "video":
                ipPort = "230.192.0.11:7005";
                executeCommand(mediaSrc, ipPort);
                break;
            default:
                Log.e(TAG, "Mídia não suportada");
                break;
        }
        return ipPort;
    }

    private void executeCommand(String mediaSrc, String ipPort) {
        String command = "-re -i /storage/emulated/0/Download/" + mediaSrc + " -acodec copy -vcodec copy -f rtp_mpegts rtp://" + ipPort;
        Log.e(TAG, "COMMAND: ffmpeg " + command);
        final String cmd[] = command.split(" ");
        try {
            ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    Log.e(TAG, "FAILED with output : "+s);
                }
                @Override
                public void onSuccess(String s) {
                    Log.e(TAG, "SUCCESS with output : "+s);
                }
                @Override
                public void onProgress(String s) {
                    Log.d(TAG, "Started command : ffmpeg "+ cmd);
                }
                @Override
                public void onStart() {
                    Log.d(TAG, "Started command : ffmpeg " + cmd);
                }
                @Override
                public void onFinish() {
                    Log.d(TAG, "Finished command : ffmpeg "+cmd);
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }


    public void stopStreaming() {
        if (ffmpeg.isFFmpegCommandRunning())
            ffmpeg.killRunningProcesses();
    }
}
