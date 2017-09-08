package br.com.lapic.thomas.fsm_app.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by thomas on 08/09/17.
 */

public class ServerSocketThread extends Thread {

    private String TAG = this.getClass().getSimpleName();
    private ServerSocket serverSocket;
    private FileTxThread fileTxThread;
    private static final int SocketServerPORT = 8080;
    private Socket socket;

    @Override
    public void run() {
        socket = null;
        try {
            serverSocket = new ServerSocket(SocketServerPORT);
//            MainActivity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.e(TAG, "socketPort: " + serverSocket.getLocalPort());
//                    infoPort.setText(String.valueOf(serverSocket.getLocalPort()));
//                }});
            while (true) {
                socket = serverSocket.accept();
                fileTxThread = new FileTxThread(socket);
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
