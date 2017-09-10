package br.com.lapic.thomas.fsm_app.connection;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import br.com.lapic.thomas.fsm_app.utils.AppConstants;

/**
 * Created by thomas on 08/09/17.
 */

public class ClientRxThread extends Thread {

    private final String mMediaName;
    private String TAG = this.getClass().getSimpleName();
    private String dstAddress;
    private int dstPort;
    private int current = 0;
    private int FILE_SIZE = 30000000;
    private InputStream inputStream;
    private FileOutputStream fileOutputStream;
    private BufferedOutputStream bufferedOutputStream;
    private Socket socket;

    public ClientRxThread(String address, int port, String mediaName) {
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
            Log.e(TAG, "Finished");
//            MainActivity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(MainActivity.this,
//                            "Finished",
//                            Toast.LENGTH_LONG).show();
//                }});

        } catch (IOException e) {
            e.printStackTrace();
            final String eMsg = "Something wrong: " + e.getCause().getMessage();
            Log.e(TAG, eMsg);
//            MainActivity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(MainActivity.this,
//                            eMsg,
//                            Toast.LENGTH_LONG).show();
//                }});

        } finally {
            try {
                if (fileOutputStream != null) fileOutputStream.close();
                if (bufferedOutputStream != null) bufferedOutputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
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
