package Entity;

import se.egy.graphics.Drawable;

import java.awt.*;

public abstract class Entity implements Drawable {
    private Image image;
    private Rectangle rec = null;
    /* Anledningen till att jag har mina xPos och yPos i double är för att om hastigheten vid något tillfälle blir mindre än 0,5 kommer värdet
    att automatiskt bli noll enligt int och detta kommer ge en speed på 0 vilket vi inte vill ha så därför gör jag om det till en double
    för att kunna flytta på föremålet som då är aliens i detta fall. */
    protected double xPos, yPos;   // Positionen
    public int width;
    public int height;

    protected int speed;           // Hastighet i px/sekund

    protected int dx = 0, dy = 0;  // Rörelseriktning

    private boolean active = true; // Gör alla nya Image image, double xPos, double yPos, int speedobjekt aktiva.

    /**
     * Konstruktor
     */
    public Entity (Image image, double xPos, double yPos, int speed){
        this.image = image;
        this.xPos = xPos;
        this.yPos = yPos;
        this.speed = speed;
        rec = new Rectangle((int)xPos, (int)yPos, image.getWidth(null),
                image.getHeight(null));
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
    }
    public Rectangle getRectangle(){
        rec.setLocation((int)xPos, (int)yPos);
        return rec;
    }

    public boolean collision(Entity entity){
        getRectangle(); // Uppdaterar positionen på den egna rektangeln
        return rec.intersects(entity.getRectangle());
    }
    public double getyPos () {
        return yPos;
    }
    public double getxPos () {
        return xPos;
    }

    public int getDx() {
        return dx;
    }

    public void setxPos(double xPos) {this.xPos = xPos; }

    public void setyPos(double yPos) {this.yPos = yPos; }
    /**
     * Ritar bilden på ytan g
     */
    public void draw(Graphics2D g) {
        g.drawImage(image,(int)xPos,(int)yPos,null);
    }

    /**
     * Vilken riktning i x-led
     * @param dx 0 = stilla, 1 = höger, -1 = vänster´
     */
    public void setDirectionX(int dx){
        this.dx = dx;
    }

    /**
     * Vilken riktning i y-led
     * @param dy 0 = stilla, 1 = höger, -1 = vänster
     */
    public void setDirectionY(int dy){
        this.dy = dy;
    }
    public void setActive(boolean active){
        this.active = active;
    }
    public boolean getActive() {
        return active;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Metod som gör förflyttningen, dvs ändrar xPos och yPos
     * Måste skapas i klasser som ärver entity
     * @param deltaTime nanosekunder sedan förra anropet
     */
    public abstract void move(long deltaTime);
}
