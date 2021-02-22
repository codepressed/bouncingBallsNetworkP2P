package bouncingballs;

public class Statistics{

    private int totalBalls = 0;
    private int pausedBalls = 0;
    private int insideBH = 0;

    public void addBall(){
        this.totalBalls += 1;
        System.out.println("A ball was added");
    }

    public void removeBall(){
        this.totalBalls -= 1;
    }

    public void modInsideBH(int num){
        this.insideBH += num;
    }

    public void modPausedBalls(int num){
        this.pausedBalls += num;
    }

    public void removeInsideBH(){
        this.insideBH -= 1;
    }

    public int getTotalBalls() {
        return totalBalls;
    }

    public int getPausedBalls() {
        return pausedBalls;
    }

    public int getInsideBH() {
        return insideBH;
    }
}