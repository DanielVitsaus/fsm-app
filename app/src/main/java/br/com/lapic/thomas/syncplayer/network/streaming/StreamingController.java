package br.com.lapic.thomas.syncplayer.network.streaming;

import android.content.Context;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.util.ArrayList;

import br.com.lapic.thomas.syncplayer.utils.AppConstants;

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
        if (ffmpeg == null) {
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
    }

    public String startStreaming(ArrayList<String> medias) {
        String media[] = medias.get(0).split(":");
        String typeMedia = media[0];
        String mediaSrc = media[1];
        String ipPort = null;
        String command;
        switch (typeMedia) {
            case "video":
                ipPort = AppConstants.STREAMING_MULTICAST_IP_BASE + ":7005";
//                command = "-re -i /storage/emulated/0/Download/" + mediaSrc + " -acodec copy -vcodec copy -f rtp_mpegts rtp://" + ipPort;
//                command = "-re -i /storage/emulated/0/Download/" + mediaSrc + " -acodec copy -vcodec copy -an -f rtp rtp://" + ipPort;
                command = "-re -i /storage/emulated/0/Download/" + mediaSrc + " -acodec copy -vcodec copy -f rtp_mpegts rtp://" + ipPort;
                executeCommand(command);
                break;
            case "image":
                ipPort = AppConstants.STREAMING_MULTICAST_IP_BASE + ":7006";
//                command = "-loop 1 -i /storage/emulated/0/Download/" + mediaSrc + " -f lavfi -i anullsrc=channel_layout=5.1:sample_rate=48000 -t 5 -c:v libx264 -t 20 -pix_fmt yuv420p -vf scale=1280:720 -y -f rtp_mpegts rtp://" + ipPort;
                command = "-loop 1 -i /storage/emulated/0/Download/"+ mediaSrc + " -g 10 -c:v libx264 -t 20 -pix_fmt yuv420p -vf scale=854:480 -f rtp_mpegts rtp://" + ipPort;
                executeCommand(command);
                break;
//            case "audio":
//                ipPort = AppConstants.STREAMING_MULTICAST_IP_BASE + ":7007";
//                command = "-re -i /storage/emulated/0/Download/" + mediaSrc + " -acodec libmp3lame -ab 128k -ac 2 -ar 44100 -f rtp rtp://" + ipPort;
//                executeCommand(command);
//                break;
            default:
                Log.e(TAG, "Mídia não suportada");
                break;
        }
        return ipPort;
    }

    private void executeCommand(String command) {
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
//                    Log.d(TAG, "Started command : ffmpeg "+ cmd);
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
