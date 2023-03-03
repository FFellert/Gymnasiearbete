package Entity;

import java.awt.*;

public class GhostEntity extends Entity {
    public int Health;
    public int dy;
    public int dx;
    public int Gold;
    public GhostEntity(Image image, double xPos, double yPos, int speed, int Health, int dy, int dx, int Gold) {
        super(image, xPos, yPos, speed);
        this.Health = Health;
        this.Gold = Gold;
        this.dy = dy;
        this.dx = dx;
        this.setActive(false);
    }
    public int getHealth() {
        return Health;
    }
    public void setHealth(int Health) {
        this.Health = Health;
    }
    public int getGold() {
        return Gold;
    }
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public void setDx(int dx) {
        this.dx = dx;
    }
    public int getDx() {
        return dx;
    }
    public int getDy() {
        return dy;
    }
    public void setDy(int dy) {
        this.dy = dy;
    }


    /**
     * Metod som gör förflyttningen, dvs ändrar xPos och yPos
     * Måste skapas i klasser som ärver entity
     *
     * @param deltaTime nanosekunder sedan förra anropet
     */
    public void move(long deltaTime) {
        xPos += dx*(deltaTime/1000000000.0)*speed;
        yPos += dy*(deltaTime/1000000000.0)*speed;
    }
}
