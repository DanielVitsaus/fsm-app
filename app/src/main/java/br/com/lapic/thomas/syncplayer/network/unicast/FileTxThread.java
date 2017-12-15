package br.com.lapic.thomas.syncplayer.network.unicast;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import br.com.lapic.thomas.syncplayer.data.model.Media;
import br.com.lapic.thomas.syncplayer.utils.AppConstants;

/**
 * Created by thomas on 08/09/17.
 */

public class FileTxThread extends Thread {

    private final Media mMedia;
    private final String pathApp;
    private String TAG = this.getClass().getSimpleName();
    private Socket socket;
    private BufferedInputStream bufferedInputStream;
    private OutputStream outputStream;

    public FileTxThread(Socket socket, String pathApp, Media media) {
        this.socket= socket;
        this.pathApp = pathApp;
        this.mMedia = media;
    }

    @Override
    public void run() {
        File file = new File(AppConstants.FILE_PATH_DOWNLOADS, pathApp + mMedia.getSrc());
        byte[] bytes = new byte[(int) file.length()];
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            bufferedInputStream.read(bytes, 0, bytes.length);
            outputStream = socket.getOutputStream();
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
            socket.close();
            final String sentMsg = "Media sended: " + mMedia.getId();
            Log.d(TAG, sentMsg);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedInputStream != null) bufferedInputStream.close();
                if (outputStream != null) outputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
