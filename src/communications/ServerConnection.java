package communications;
import bouncingballs.*;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerConnection implements Runnable {

    private int port;
    private BallTask ballTask;
    private Thread serverThread;
    private Channel channel;
    private Socket socket;
    private ServerSocket serverSocket;
    private boolean running = true;

    public ServerConnection(Channel channel, BallTask ballTask) {
        this.port = 11001;
        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.channel = channel;
        this.ballTask = ballTask;
        this.serverThread = new Thread(this);
        this.serverThread.start();
    }

    /**
     * Stablish connection Server - Client
     */
    private void startConnection() {
        try {
            if(!this.channel.isOk()) {
                System.out.println("ServerConnection: SERVER UP");
                System.out.println("ServerConnection: My Port is: "+this.port);
                this.socket = serverSocket.accept();
                this.ballTask.setChannelCreated(true);
                if(this.ballTask.clientConnection == null) {
                    ballTask.connectionInfo.setIp(this.socket.getInetAddress().getHostAddress());
                    ballTask.connectionInfo.setPort(this.port);
                    ballTask.clientConnection = new ClientConnection(this.channel, ballTask.connectionInfo, this.ballTask);
                }
                String clientAddress = this.socket.getInetAddress().getHostAddress();
                System.out.println("ServerConnection: New connection from: " + clientAddress);
                ClientIdentified clientIdentified = new ClientIdentified(this.socket, this.channel, this.ballTask);
                clientIdentified.getIdentifiedThread().start();
            }
            //  }
        } catch (IOException e) {
            e.printStackTrace();
            }
        }



    public void run() {
        while (this.running) {
            try {
                startConnection();
            } catch (Exception e) {
                System.out.println("ServerConnection: Error initial connection" + e);
            }
        }


    }
}