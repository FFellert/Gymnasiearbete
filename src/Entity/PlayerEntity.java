package Entity;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class PlayerEntity extends Entity{

    public LaserEntity laser = null;

    public PlayerEntity(Image image, double xPos, double yPos, int speed) {
        super(image, xPos, yPos, speed);
    }

    public void left(){
        dx = -1;
    }
    public void right(){
        dx = 1;
    }
    public void down(){
        dy = 1;
    }
    public void up(){
        dy = -1;
    }

    /**
     * Metod som gör förflyttningen, dvs ändrar xPos och yPos
     * Måste skapas i klasser som ärver entity
     *
     * @param deltaTime nanosekunder sedan förra anropet
     */
    public void move(long deltaTime) {
        yPos += dy*(deltaTime/1000000000.0)*speed;
        xPos += dx*(deltaTime/1000000000.0)*speed;
    }
    public boolean tryToFire() {
        if(laser == null || !laser.getActive()){
            laser = new LaserEntity(new ImageIcon(getClass().getResource("../resources/Laser.png")).getImage(), xPos+13, yPos, 90);
            laser.setActive(true);
            return true;
        }else
            return false;
    }

    public void draw(Graphics2D g) {
        if(laser != null && laser.getActive()){
            laser.draw(g);
        }
        super.draw(g);
    }
}
