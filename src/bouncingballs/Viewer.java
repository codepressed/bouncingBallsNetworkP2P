package bouncingballs;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Viewer extends Canvas implements Runnable {

    public final int CANVAS_WIDTH = 450;
    public final int CANVAS_HEIGHT = 400;

    private BufferedImage backgroundImg;
    private ArrayList<Ball> balls;
    private ArrayList<BlackHole> blackHoles;
    private BallTask ballTask;
    private Credits credits = new Credits();

    public Viewer(BallTask ballTask, ArrayList<Ball> balls, ArrayList<BlackHole> blackHoles){
        this.ballTask = ballTask;
        this.balls = balls;
        this.blackHoles = blackHoles;
        this.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    }


    /**
     * Load a Background Image for BouncingBalls Viewer
     */
    public void loadBackground() {
        try {
            this.backgroundImg = (ImageIO.read(BallTask.class.getResource("../resources/dom.jpg")));;
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Start a Thread
     * Loop of the paint Method.
     */
    @Override
    public void run(){
        this.createBufferStrategy(4);
        try{
        do {
            paint();

        } while(true);
        } catch(Exception e){
            System.out.println("There was a "+e+" exception on Canvas paint.");
        }
    }

    /**
     * Paint method. It has to draw every ball.
     * It uses paint and move methods from bouncingballs.Ball object
      */
    private void paint() {
        BufferStrategy bs;
        bs = this.getBufferStrategy();
        Graphics g = bs.getDrawGraphics();
        g.drawImage(backgroundImg, 0, 0, null);

        //If there are balls, paint them in the paint loop. But first print blackHoles, so they are on the BG.
        if(this.balls != null){
            for(BlackHole blackHole : this.blackHoles){
                blackHole.paint(g);
            }
            ballTask.checkBalls();
            for (Ball ball : this.balls){
                ball.paint(g);
            }

        }

        //Here I create my Signature-Copyright on Bottom-Right side of the program
        g.setColor(new Color(0, 0, 0, 95));
        g.setFont(new Font("TimesRoman", Font.PLAIN, 10));
        g.fillRect(295, 360, 150,35);
        g.drawString(credits.getName(), 305,370);
        g.drawString(credits.getEmail(), 305, 380);
        g.drawString(credits.getCourse_Info(), 305, 390);
        g.setColor(new Color(255, 255, 255));
        g.drawString(credits.getFps(), 400, 15);

        bs.show();
        g.dispose();
    }
}
