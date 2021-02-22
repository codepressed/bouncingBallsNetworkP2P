package bouncingballs;

import java.awt.*;
import java.util.Random;

public class BlackHole implements VisualObject{
    private int coordX;
    private int coordY;
    private int width;
    private int height;
    private Ball ball;
    private Random random;
    private Color color = new Color(0x000000);
    private BallTask ballTask;
    private Viewer viewer;
    private Rectangle rectangle;
    private Statistics statistics;
    private boolean busy = false;

    public BlackHole(BallTask ballTask, Statistics statistics){
        this.ballTask = ballTask;
        this.statistics = statistics;
        this.random = new Random();
        this.width = this.random.nextInt((80-50)+0)+50;
        this.height = this.random.nextInt((80-50)+0)+50;
        System.out.println("Blackhole... Ancho: "+ this.width + " Altura: " + this.height);
        this.coordX = this.random.nextInt(350 - this.width);
        this.coordY = this.random.nextInt(300 - this.height);
        this.rectangle = new Rectangle(width, height);
        this.rectangle.setBounds(this.coordX, this.coordY, width, height);
    }

    /**
     * Method used to put balls in BlackHole
     * @param ball Ball that will get in
     * It's syncronized and waits until the ball in is released to get another in
     */
    public synchronized void putBallInBH(Ball ball){
        statistics.modPausedBalls(1);
        while (this.ball != null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.ball = ball;
        ball.setStoreColor(ball.getBallColor());
        ball.setBallColor(new Color(255,0,0));
        ball.setSleepTime(ball.getSleepTime()*2);
        ball.setOutside(false);
        statistics.modPausedBalls(-1);
        statistics.modInsideBH(1);
        notifyAll();
    }


    /**
     * Method used to return original status to balls in BH
     * It's applied once ball leaves the Blackhole
     * @param ball  Ball that has left the Blackhole
     */
    public synchronized void removeBallfromBH(Ball ball){
        if (this.ball != null) {
            if (ball.equals(this.ball)) {
                this.ball = null;
                ball.setBallColor(ball.getStoreColor());
                ball.setOutside(true);
                ball.setSleepTime(ball.getSleepTime()/2);
                statistics.modInsideBH(-1);
                notifyAll();
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.coordX, this.coordY, width, height);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

}