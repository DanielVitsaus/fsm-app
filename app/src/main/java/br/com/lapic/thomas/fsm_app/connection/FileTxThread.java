package br.com.lapic.thomas.fsm_app.connection;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by thomas on 08/09/17.
 */

public class FileTxThread extends Thread {

    private String TAG = this.getClass().getSimpleName();
    private Socket socket;
    private BufferedInputStream bufferedInputStream;
    private OutputStream outputStream;

    public FileTxThread(Socket socket) {
        this.socket= socket;
    }

    @Override
    public void run() {
        File file = new File(
                Environment.getExternalStorageDirectory(),
                "teste.jpeg");

        byte[] bytes = new byte[(int) file.length()];
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            bufferedInputStream.read(bytes, 0, bytes.length);
            outputStream = socket.getOutputStream();
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
            socket.close();

            final String sentMsg = "File sent to: " + socket.getInetAddress();
            Log.e(TAG, sentMsg);
//            MainActivity.this.runOnUiThread(new Runnable() {
//
//                @Override
//                public void run() {
//                    Toast.makeText(MainActivity.this,
//                            sentMsg,
//                            Toast.LENGTH_LONG).show();
//                }});

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (bufferedInputStream != null) bufferedInputStream.close();
                if (outputStream != null) outputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

}
