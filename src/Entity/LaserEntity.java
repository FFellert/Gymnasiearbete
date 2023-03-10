package Entity;

import java.awt.*;

public class LaserEntity extends Entity {

    public LaserEntity laser = null;  // ingen missil!
    public int dy;
    public int dx;
    public LaserEntity(Image image, double xPos, double yPos, int speed, int dx, int dy) {
        super(image, xPos, yPos, speed);
        this.dy = dy;
        this.dx = dx;
        this.setActive(false);
    }

    public double getyPos(){
        return yPos;
    }
    public int getSpeed() {
        return speed;
    }

    /**
     * Metod som gör förflyttningen, dvs ändrar xPos och yPos
     * Måste skapas i klasser som ärver entity
     *
     * @param deltaTime nanosekunder sedan förra anropet
     */
    public void move(long deltaTime) {
        if(laser != null && laser.getActive()){
            laser.move(deltaTime);
        }
        yPos += dy*(deltaTime/1000000000.0)*speed;
        xPos += dx*(deltaTime/1000000000.0)*speed;
    }
}
