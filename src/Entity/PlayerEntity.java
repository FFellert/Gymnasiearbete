package Entity;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class PlayerEntity extends Entity{

    public LaserEntity laser = null;
    int Gold;
    int Damage;
    int Health;


    public PlayerEntity(Image image, double xPos, double yPos, int speed, int Gold, int Damage, int Health) {
        super(image, xPos, yPos, speed);    //Ärver från Entity
        this.Gold = Gold;                   //Mitt guld-värde
        this.Damage = Damage;               //Damage
        this.Health = Health;
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
    public int getGold() {
        return Gold;
    }
    public void setGold(int Gold) {
        this.Gold = Gold;
    }
    public int getDamage() {
        return Damage;
    }
    public void setDamage(int Damage) {
        this.Damage = Damage;
    }
    public boolean tryToFire(String direction) {
        if(laser == null || !laser.getActive()){
            if(direction == "up") { //Sätter dy och dx baserat på i vilken riktning som min PlayerEntity kollar
                laser = new LaserEntity(new ImageIcon(getClass().getResource("../resources/Laserup.png")).getImage(), xPos+13, yPos, 800, 0, -1);
                laser.setActive(true);
            }
            if(direction == "down") {
                laser = new LaserEntity(new ImageIcon(getClass().getResource("../resources/Laserup.png")).getImage(), xPos+13, yPos, 800, 0, 1);
                laser.setActive(true);
            }
            if(direction == "left") {
                laser = new LaserEntity(new ImageIcon(getClass().getResource("../resources/LaserRight.png")).getImage(), xPos+13, yPos, 800, -1, 0);
                laser.setActive(true);
            }
            if(direction == "right") {
                laser = new LaserEntity(new ImageIcon(getClass().getResource("../resources/LaserRight.png")).getImage(), xPos+13, yPos, 800, 1, 0);
                laser.setActive(true);
            }

            return true;
        }else
            return false;
    }
    public int getHealth() {
        return Health;
    }
    public void setHealth(int Health) {
        this.Health = Health;
    }
    public void draw(Graphics2D g) {
        if(laser != null && laser.getActive()){
            laser.draw(g);
        }
        super.draw(g);
    }
}
