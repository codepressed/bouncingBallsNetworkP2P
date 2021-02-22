package communications;
import bouncingballs.*;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientIdentified implements Runnable {

    private Socket socket;
    private Channel channel;
    private BallTask ballTask;
    private Thread identifiedThread;
    private boolean clientIdentified;

    public Thread getIdentifiedThread() {
        return identifiedThread;
    }

    public ClientIdentified(Socket socket, Channel channel, BallTask ballTask) {
        this.socket = socket;
        this.channel = channel;
        this.ballTask = ballTask;
        identifiedThread = new Thread(this);
    }


    @Override
    public void run() {
        this.clientIdentified = false;
        while (!this.clientIdentified) {
            try {
                DataInputStream input = new DataInputStream(this.socket.getInputStream());
                String header = input.readUTF();

                if (header != null) {
                    System.out.println("ClientIdentified: Setting ");
                    if(header.equals("LEFT")){
                        ballTask.side = "RIGHT";
                    } else if(header.equals("RIGHT")){
                        ballTask.side = "LEFT";
                    }
                    System.out.println("ClientIdentified: The other client has this Side: "+header);
                    this.channel.setSocket(this.socket);
                    DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
                    out.writeUTF("OK");
                    this.clientIdentified = true;

                } else {
                    System.out.println("ClientIdentified: This connection is not a Balltask.");
                    this.clientIdentified = true;

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}