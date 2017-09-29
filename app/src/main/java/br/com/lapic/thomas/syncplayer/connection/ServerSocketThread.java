package br.com.lapic.thomas.syncplayer.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import br.com.lapic.thomas.syncplayer.data.model.Media;

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
            while (true) {
                socket = serverSocket.accept();
                fileTxThread = new FileTxThread(socket, mMedia);
                fileTxThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
