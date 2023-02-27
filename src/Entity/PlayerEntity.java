package Entity;

import java.awt.*;

public class PlayerEntity extends Entity{

    public PlayerEntity(Image image, double xPos, double yPos, int speed) {
        super(image, xPos, yPos, speed);
    }

    public void left(int dx){
        this.dx = dx;
    }
    public void right(int dx){
        this.dx = dx;
    }
    public void down(int dy){
        this.dy = dy;
    }
    public void up(int dy){
        this.dy = dy;
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
    public void draw(Graphics2D g) {
        super.draw(g);
    }
}
