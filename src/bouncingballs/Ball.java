package bouncingballs;

import communications.Channel;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;


public class Ball implements Runnable, VisualObject, Serializable {
    public final Thread BALLTHREAD;
    public final int DIAMETER = 15;
    public int velX = 1;
    public int velY = 1;
    private int sleepTime = 1000/60;
    private boolean outside = true;
    public int coordX;
    public int coordY;
    public Rectangle rectangle;
    private Color ballColor;
    private Color storeColor;
    private Color borderColor;

    public BallTask ballTask;
    private Random random;

    public int rightBound;
    public int bottomBound;

    private boolean running;
    private boolean stopped = false;
    private boolean blackHoleCheck = true;


    public Ball(BallTask ballTask){
        this.ballTask = ballTask;
        this.random = new Random();
        this.ballColor = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        this.borderColor = new Color(0x000000);
        setBounds(450,400);
        this.coordX = this.random.nextInt(this.rightBound - this.DIAMETER);
        this.coordY = this.random.nextInt(this.bottomBound - this.DIAMETER);
        this.rectangle = new Rectangle(this.DIAMETER, this.DIAMETER);
        setRunning(true);
        this.BALLTHREAD = new Thread(this);
        this.BALLTHREAD.start();
    }

    /**
     * This method is used to fix the bound limit of the balls
     * @param width Canvas width
     * @param height Canvas height
     */
    public void setBounds(int width, int height){
        rightBound = width - DIAMETER;
        bottomBound = height - DIAMETER;
    }

    /**
     * Paint method, used in Canvas(bouncingballs.Viewer.java) to draw the ball
     * @param g Graphics of Canvas
     */
    @Override
    public void paint(Graphics g) {
        g.setColor(this.ballColor);
        g.fillOval(this.coordX, this.coordY, this.DIAMETER, this.DIAMETER);

    }

    /**
     * Thread - Loop of move method, to keep the bouncingballs.Ball object always moving
     */
    @Override
    public void run() {
        while(!stopped){
            if(isRunning()){
                ballTask.move(this);
                try {
                    ballTask.checkCollisionBlackhole(this);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try{
                    this.BALLTHREAD.sleep(this.sleepTime);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            else{
                try {
                    this.BALLTHREAD.sleep(this.sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            }
        }


    public int getVelX() {
        return velX;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public int getVelY() {
        return velY;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public int getCoordX() {
        return coordX;
    }

    public void setCoordX(int coordX) {
        this.coordX = coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public void setCoordY(int coordY) {
        this.coordY = coordY;
    }

    public Color getBallColor() {
        return ballColor;
    }

    public void setBallColor(Color ballColor) {
        this.ballColor = ballColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public boolean isOutside() {
        return outside;
    }
    public void setOutside(boolean outside) {
        this.outside = outside;
    }

    public Rectangle getRectangle() { return rectangle; }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public boolean getRunning() {
        return running;
    }

    public Color getStoreColor() {
        return storeColor;
    }

    public void setStoreColor(Color storeColor) {
        this.storeColor = storeColor;
    }

    public boolean isBlackHoleCheck() {
        return blackHoleCheck;
    }

    public void setBlackHoleCheck(boolean blackHoleCheck) {
        this.blackHoleCheck = blackHoleCheck;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public int getRightBound() {
        return rightBound;
    }

    public void setRightBound(int rightBound) {
        this.rightBound = rightBound;
    }

    public int getBottomBound() {
        return bottomBound;
    }

    public void setBottomBound(int bottomBound) {
        this.bottomBound = bottomBound;
    }
}