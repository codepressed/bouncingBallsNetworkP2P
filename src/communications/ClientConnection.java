package communications;

import bouncingballs.BallTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientConnection implements Runnable {

    private String ip;
    private int port;
    private Channel channel;
    private ConnectionInfo connectionInfo;
    private BallTask ballTask;
    private boolean running = true;


    public ClientConnection(Channel channel, ConnectionInfo connectionInfo, BallTask ballTask) {
        this.channel = channel;
        this.connectionInfo = connectionInfo;
        this.ip = this.connectionInfo.getIp();
        this.port = this.connectionInfo.getPort();
        this.ballTask = ballTask;
        Thread clientThread = new Thread(this);
        clientThread.start();
    }

    /**
     * Stablish connection with Server
     */
    private void startConnection() {
        Socket socket;
        try {
            if(!this.channel.isOk()) {
                socket = new Socket(this.ip, this.port);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                String greeting = channel.BALLTASK_STRING;
                if (ballTask.side != null){
                    greeting = ballTask.side;
                }
                out.writeUTF(greeting);
                DataInputStream in = new DataInputStream(socket.getInputStream());
                String response = in.readUTF();
                if (!this.channel.isOk() && response.equals("OK")) {
                    this.channel.setSocket(socket);
                    System.out.println("ClientConnection: Successful connection");
                }
            }
        } catch (Exception e) {
            System.out.println("ClientConnection: Failed to connect to " +this.ip+":"+this.port+". Trying to connect");
            //log.error("failed to connect to server", e);
        }
    }

    @Override
    public void run() {
        while (this.running) {
            try {
                startConnection();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}