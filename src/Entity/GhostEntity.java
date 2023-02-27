package Entity;

import java.awt.*;

public class GhostEntity extends Entity {
    public GhostEntity(Image image, double xPos, double yPos, int speed) {
        super(image, xPos, yPos, speed);
        dy = 3;
        this.setActive(false);
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
