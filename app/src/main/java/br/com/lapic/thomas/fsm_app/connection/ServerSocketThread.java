package br.com.lapic.thomas.fsm_app.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import br.com.lapic.thomas.fsm_app.data.model.Media;

/**
 * Created by thomas on 08/09/17.
 */

public class ServerSocketThread extends Thread {

    private final Media mMedia;
    private String TAG = this.getClass().getSimpleName();
    private ServerSocket serverSocket;
    private FileTxThread fileTxThread;
    private final int socketServerPORT;
    private Socket socket;

    public ServerSocketThread(int downloadSocketPort, Media media) {
        this.socketServerPORT = downloadSocketPort;
        this.mMedia = media;
    }

    @Override
    public void run() {
        socket = null;
        try {
            serverSocket = new ServerSocket(socketServerPORT);
//            MainActivity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.e(TAG, "socketPort: " + serverSocket.getLocalPort());
//                    infoPort.setText(String.valueOf(serverSocket.getLocalPort()));
//                }});
            while (true) {
                socket = serverSocket.accept();
                fileTxThread = new FileTxThread(socket, mMedia);
                fileTxThread.start();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
