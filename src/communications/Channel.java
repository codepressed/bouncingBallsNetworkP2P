package communications;

import bouncingballs.BallTask;
import bouncingballs.Ball;

import java.awt.*;
import java.io.*;
import java.net.Socket;

public class Channel implements Runnable {

    private final String COMMA = ",";
    public final String BALLTASK_STRING = "BALLTASK";

    private Socket socket;
    private boolean ok;
    private Thread channelThread;
    private BallTask ballTask;


    public Channel(BallTask ballTask) {
        this.ballTask = ballTask;
    }

    /**
     * Method to assign a Socket to channel
     * @param socket Socket to set
     */
    public synchronized void setSocket(Socket socket) {
        this.ok = true;
        this.socket = socket;
        this.channelThread = new Thread(this);
        this.channelThread.start();
    }

    /**
     * Method to create new ball based of information received.
     * @param ballInfo information to reCreate the ball
     * @return the ball we received
     */
    private Ball createBall(String ballInfo) {
        String[] info = ballInfo.split(COMMA);
        Ball ball = new Ball(ballTask);
        ball.setCoordX(Integer.parseInt(info[1]));
        ball.setCoordY(Integer.parseInt(info[2]));
        ball.setVelX(Integer.parseInt(info[3]));
        ball.setVelY(Integer.parseInt(info[4]));
        ball.setBallColor(new Color(Integer.parseInt(info[5]), Integer.parseInt(info[6]), Integer.parseInt(info[7])));
        return ball;
    }

    /**
     * Method to send important ball information with DataOutputStream before being deleted
     * @param ball Object sent
     */
    public void send(Ball ball) {
        try {
            DataOutputStream writer = new DataOutputStream((this.socket.getOutputStream()));
            String ballInfo = BALLTASK_STRING + COMMA +
                    ball.getCoordX() + COMMA +
                    ball.getCoordY() + COMMA +
                    ball.getVelX() + COMMA +
                    ball.getVelY() + COMMA +
                    ball.getBallColor().getRed() + COMMA +
                    ball.getBallColor().getGreen() + COMMA +
                    ball.getBallColor().getBlue();

            writer.writeUTF(ballInfo);
        } catch (IOException e) {
            System.out.println("Channel: Connection reset");
            this.ok = false;
        }
    }

    /**
     * Method to receive important ball information with DataInputStream and create it
     */
    public void receiveInfo() {
        DataInputStream reader = null;
        try {
            reader = new DataInputStream(this.socket.getInputStream());
            String received;
            received = reader.readUTF();

            if (received == null) {
                System.out.println("Channel: Content is null");
                this.ok = false;
            } else if (received.split(",")[0].equals((BALLTASK_STRING))){
                Ball ball = createBall(received);
                this.ballTask.addNewBall(ball);
            } else if (received.equals("ok")){
            }

        } catch (IOException e) {
            System.out.println("Channel: Connection reset");
            this.ok = false;
        }
    }

    public void run() {
        while (this.isOk()) {
            try {
                receiveInfo();
            } catch (Exception e) {
                System.out.println("Channel: Waiting for information: +" + e);
            }
        }
    }

    public synchronized boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Thread getChannelThread() {
        return channelThread;
    }

    public void setChannelThread(Thread channelThread) {
        this.channelThread = channelThread;
    }

    public boolean isStatus() {
        return ok;
    }

    public void setStatus(boolean status) {
        this.ok = status;
    }

    public Socket getSocket() {
        return socket;
    }
}