package bouncingballs;

import communications.Channel;
import communications.ClientConnection;
import communications.ConnectionInfo;
import communications.ServerConnection;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BallTask extends JFrame implements ActionListener {
    private static int PROGRAM_WIDTH = 800;
    private static int PROGRAM_HEIGHT = 400;

    public String side;
    private Viewer viewer;
    private ControlPanel controlPanel;
    private Statistics statistics;
    public Channel channel;
    public ClientConnection clientConnection;
    private ServerConnection serverConnection;
    public ConnectionInfo connectionInfo;
    private boolean channelCreated = false;

    private ArrayList<Ball> balls = new ArrayList<>();
    private ArrayList<BlackHole> blackHoles = new ArrayList<>();
    private ArrayList<Ball> ballsToRemove = new ArrayList<>();
    private ArrayList<Ball> ballsToAdd = new ArrayList<>();


    public BallTask() throws IOException {
        this.setTitle("Bouncing Depressed Balls");
        this.setSize(PROGRAM_WIDTH,PROGRAM_HEIGHT);
        this.setIconImage(ImageIO.read(BallTask.class.getResource("../resources/ball.png")));
        this.setVisible(true);
        this.setResizable(false);
        this.setLayout(new GridBagLayout());
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.connectionInfo = new ConnectionInfo();
        this.statistics = new Statistics();
        this.channel = new Channel(this);
        this.serverConnection = new ServerConnection(this.channel, this);
        this.blackHoles.add(new BlackHole(this, this.statistics));
        this.blackHoles.add(new BlackHole(this, this.statistics));
        this.viewer = new Viewer(this, balls, blackHoles);
        GridBagConstraints gCons = new GridBagConstraints();
        this.addViewer(gCons);
        this.addControlPanel(gCons);


        this.pack();
    }

    /**
     * Method to add the viewer (Bouncing balls canvas) to the JFrame
     * @param gCons GridBagConstraints of bouncingballs.ControlPanel
     */
    private void addViewer(GridBagConstraints gCons) {
        this.viewer.loadBackground();
        gCons.fill = GridBagConstraints.BOTH;
        gCons.gridx = 1;
        gCons.gridy = 0;
        gCons.weightx = 2;
        this.add(this.viewer, gCons);
        Thread viewerThread = new Thread(this.viewer);
        viewerThread.start();

    }

    /**
     * Method to add the controlPanel (PlayPauseStop options + bouncingballs.Statistics) to the JFrame
     * @param gCons GridBagConstraints of bouncingballs.ControlPanel
     */
    private void addControlPanel(GridBagConstraints gCons){
        this.controlPanel = new ControlPanel(this, statistics);

        gCons.fill = GridBagConstraints.BOTH;
        gCons.gridx = 0;
        gCons.gridy = 0;
        gCons.weightx = 1;
        this.add(this.controlPanel, gCons);
    }

    /**
     * Check action performed by the user
     * @param e is the click Event on Control Panel
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String event = e.getActionCommand();

        switch(event){
            case ControlPanel.PLAYTEXT:
                controlPanel.stop.setSelected(false);
                controlPanel.pause.setSelected(false);
                playBalls();
                break;
            case ControlPanel.STOPTEXT:
                controlPanel.play.setSelected(false);
                controlPanel.pause.setSelected(false);
                break;
            case ControlPanel.PAUSETEXT:
                controlPanel.play.setSelected(false);
                controlPanel.stop.setSelected(false);
                pauseBalls();
                break;
            case ControlPanel.ADDBALL:
                statistics.addBall();
                break;
            case ControlPanel.CONNECT:
                if(clientConnection==null){
                    connectionInfo.setIp(controlPanel.connectionIP.getText());
                    connectionInfo.setPort(Integer.parseInt(controlPanel.connectionPort.getText()));
                    side = (String) controlPanel.sideBox.getSelectedItem();
                    this.clientConnection = new ClientConnection(this.channel, this.connectionInfo, this);
                }
                break;
        }

    }
    /**
     * Method activated when "PLAY" button is pressed. It sets balls running true.
     */
    private void playBalls() {
        for (Ball ball : this.balls){
            ball.setRunning(true);
        }
    }

    /**
     * Method activated when "PAUSE" button is pressed. It sets balls running false.
     */
    private void pauseBalls() {
        for (Ball ball : this.balls){
            ball.setRunning(false);
        }
    }

    /**
     * Method to constantly update coordX and coordY with the logic movement of the Balls.
     */
    public void move(Ball ball){
        //Move at the given velocity
        ball.coordX += ball.velX;
        ball.coordY += ball.velY;

        //Bounce the ball off the wall if necessary
        if (ball.coordX < 0){
            if(isChannelCreated() && getSide()==ControlPanel.RIGHTSIDE) {
                ball.coordX = ball.rightBound;
                channel.send(ball);
                removeBall(ball);
            } else{
                ball.coordX = 0;
                ball.velX = -ball.velX;
            }
        } else if(ball.coordX > ball.rightBound){
            if(isChannelCreated() && getSide()==ControlPanel.LEFTSIDE) {
                ball.coordX = 0;
                channel.send(ball);
                removeBall(ball);
            } else{
                ball.coordX = ball.rightBound;
                ball.velX = -ball.velX;
            }
        } else if(ball.coordY < 0){
            ball.coordY = 0;
            ball.velY = -ball.velY;
        } else if(ball.coordY > ball.bottomBound){
            ball.coordY = ball.bottomBound;
            ball.velY = -ball.velY;
        }
        ball.rectangle.setBounds(ball.coordX, ball.coordY, ball.DIAMETER, ball.DIAMETER);

    }

    /**
     * Check if a bouncingballs.Ball obj collides with a bouncingballs.BlackHole
     * @param ball bouncingballs.Ball to check
     */
    public void checkCollisionBlackhole(Ball ball) throws InterruptedException {
        for (BlackHole blackHole : this.blackHoles) {
            if (blackHole.getRectangle().intersects(ball.getRectangle()) && ball.isOutside()) {
                blackHole.putBallInBH(ball);
            }
            if (!blackHole.getRectangle().intersects(ball.getRectangle()) && !ball.isOutside()) {
                blackHole.removeBallfromBH(ball);
            }
        }
    }



    /**
     * Method to avoid ConcurrentModificationException
     * while iterating a ArrayList (Balls)
     * (Modify an array while it's iterated)
     */
    public synchronized void checkBalls() {
        if (statistics.getTotalBalls() > balls.size()) {
            for (int i = 0; i < statistics.getTotalBalls() - balls.size(); i++) {
                balls.add(new Ball(this));
            }
        }
        if (ballsToAdd.size() > 0){
            balls.add(ballsToAdd.get(0));
            ballsToAdd.remove(0);
            statistics.addBall();
        }
        if (ballsToRemove.size() > 0){
            balls.remove(ballsToRemove.get(0));
            ballsToRemove.remove(0);
            statistics.removeBall();
        }
    }

    /**
     * Method for Channel to add a new ball that we received
     * @param ball Balls we received and add
     */
    public void addNewBall(Ball ball){
        this.ballsToAdd.add(ball);
    }

    /**
     * Method for Channel to remove a ball that we sent
     * @param ball Ball we sent and remove
     */
    public void removeBall(Ball ball){
        this.ballsToRemove.add(ball);
        ball.setStopped(true);
    }


    public boolean isChannelCreated() {
        return channelCreated;
    }

    public void setChannelCreated(boolean channelCreated) {
        this.channelCreated = channelCreated;
    }

    public String getSide() {
        return side;
    }


}