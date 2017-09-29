package br.com.lapic.thomas.syncplayer.connection;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import br.com.lapic.thomas.syncplayer.ui.secondarymode.SecondaryModePresenter;
import br.com.lapic.thomas.syncplayer.utils.AppConstants;

/**
 * Created by thomas on 08/09/17.
 */

public class ClientRxThread extends Thread {

    private final String mMediaName;
    private final SecondaryModePresenter presenter;
    private String TAG = this.getClass().getSimpleName();
    private String dstAddress;
    private int dstPort;
    private int current = 0;
    private int FILE_SIZE = 30000000;
    private InputStream inputStream;
    private FileOutputStream fileOutputStream;
    private BufferedOutputStream bufferedOutputStream;
    private Socket socket;

    public ClientRxThread(SecondaryModePresenter secondaryModePresenter, String address, int port, String mediaName) {
        this.presenter = secondaryModePresenter;
        Log.e(TAG, address);
        dstAddress = address;
        dstPort = port;
        this.mMediaName = mediaName;
    }

    @Override
    public void run() {
        createFolders();
        try {
            socket = new Socket(dstAddress, dstPort);
            File file = new File(AppConstants.FILE_PATH_DOWNLOADS, AppConstants.PATH_APP + AppConstants.MEDIAS + "/" + mMediaName);
            if (!file.exists()) {
                byte[] bytes = new byte[FILE_SIZE];
                inputStream = socket.getInputStream();
                fileOutputStream = new FileOutputStream(file);
                bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                int bytesRead = inputStream.read(bytes, 0, bytes.length);
                current = bytesRead;
                do {
                    bytesRead = inputStream.read(bytes, current, (bytes.length - current));
                    if (bytesRead >= 0) current += bytesRead;
                } while (bytesRead > -1);

                bufferedOutputStream.write(bytes, 0, current);
                bufferedOutputStream.flush();
            }
            presenter.onMediaDownloadFinished();
            Log.d(TAG, "Download media " + mMediaName + "Finished");
        } catch (IOException e) {
            e.printStackTrace();
            final String eMsg = "Something wrong: " + e.getCause().getMessage();
            Log.d(TAG, eMsg);
        } finally {
            try {
                if (fileOutputStream != null) fileOutputStream.close();
                if (bufferedOutputStream != null) bufferedOutputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createFolders() {
        File folder = new File(AppConstants.FILE_PATH_DOWNLOADS, AppConstants.PATH_APP + AppConstants.MEDIAS);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

}
