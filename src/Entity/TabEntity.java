package Entity;

import java.awt.*;

public class TabEntity extends  Entity{
    /**
     * Konstruktor
     *
     * @param image
     * @param xPos
     * @param yPos
     * @param speed
     */
    public TabEntity(Image image, double xPos, double yPos, int speed) {
        super(image, xPos, yPos, speed);
    }

    /**
     * Metod som gör förflyttningen, dvs ändrar xPos och yPos
     * Måste skapas i klasser som ärver entity
     *
     * @param deltaTime nanosekunder sedan förra anropet
     */
    public void move(long deltaTime) {
    }
    public void draw(Graphics2D g) {
        super.draw(g);
    }
}
