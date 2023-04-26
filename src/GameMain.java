import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.swing.*;

import Entity.*;
import se.egy.graphics.*;

public class GameMain implements KeyListener {
    private HashMap<String, Boolean> keyDown = new HashMap<>();
    private boolean gameRunning = true;
    private PlayerEntity player;
    public int GhostKilled = 0; //När mina entities förutom  PlayerEntity dör läggs det till ett värde i GhostKilled
    public int LevelTag = 0;   //Ökar för var 100 Guld man får
//
    int x = 1; //Variabel
    public int Tags = 0; //Kollar om Tags < GhostTag senare i koden


    int width = 1200; //Width
    int m = 1;
    int height = 1200; //Height
    public int dy; //Hastighet i y-led
    public int dx; //Hastighet i x-led
    public int dxG = 3; //Hastigheterna jag ger till mina ghost
    public int dyG = 3;
    public int Health = 50;
    public String direction; //Kollar vilken riktning som min PlayerEntity kollade senast åt
    public int Gold = 5; //Guld per GhostEntity
    Image[] Playerimg = new Image[5]; //Bilderna som finns för min playerEntity
    public Font font = null; //Fonten för mitt Guld-värde när det ökar
    private TxtContainer msg;
    Image GhostImg; //Mina olika bilder när GhostEntity kolliderar med varandra
    Image GhostImgx2;
    private GameScreen gameScreen = new GameScreen("Game", width, height, false); // false vid testkörning
    private ArrayList<Entity> spriteList = new ArrayList<Entity>();
    public GameMain() {
        loadImages();
        gameScreen.setKeyListener(this);
        keyDown.put("left", false);
        keyDown.put("right", false);
        keyDown.put("up", false);
        keyDown.put("down", false);
        keyDown.put("exit", false);
        keyDown.put("space", false);
        gameLoop();

    }

    public void checkCollisionAndRemove() { //Kollar om en GhostEntity kolliderar med PlayerEntity.LaserEntity och tar bort både GhostEntity och LaserEntity
        ArrayList<Entity> removeList = new ArrayList<>();

        if (player.laser != null && player.laser.getActive()) {
            for (int i = 1; i < spriteList.size(); i++) {
                if (spriteList.get(i).collision(player.laser)) {
                    player.laser.setActive(false);
                    player.laser = null;
                    GhostEntity g = (GhostEntity) spriteList.get(i);
                    g.setHealth(g.getHealth() - player.getDamage());
                    if (g.getHealth() <= 0) {
                        GhostKilled++;
                        player.setGold(player.getGold() + g.getGold());
                        removeList.add(g);
                    }
                }
                if (player.laser == null)
                    break;
            }
        }
        spriteList.removeAll(removeList); // Alt namnet på arraylist
    }

    public void loadImages() {
        ImgContainer background = new ImgContainer(0, 0, "resources/Background.png");
        gameScreen.setBackground(background);
        GhostImg = new ImageIcon(getClass().getResource("resources/ghostImg.png")).getImage();
        GhostImgx2 = new ImageIcon(getClass().getResource("resources/ghostImgx2.25.png")).getImage();
        Playerimg[0] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImg.png"))).getImage();
        Playerimg[1] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImgRight.png"))).getImage();
        Playerimg[2] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImgLeft.png"))).getImage();
        Playerimg[3] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImgUp.png"))).getImage();
        Playerimg[4] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImgDown.png"))).getImage();

        player = new PlayerEntity(Playerimg[0], (width / 2), (height / 2), 400, 0, 30, Health);
        spriteList.add(player);
        for (int i = 1; i < 2; i++) {
            spriteList.add(new GhostEntity(GhostImg, width-200, height * Math.random(), 30, 30, dy * i, dx, LevelTag * 2 + Gold));
        }
        for (int i = 1; i < 2; i++) {
            spriteList.add(new GhostEntity(GhostImg, width * Math.random(), height - 200, 30, 30, dy * i, dx, LevelTag * 2 + Gold));
        }
    }

    public void Upgrade() { //Ökar Leveltag vilket ökar skadan som PlayerEntity gör och guld som finns i GhostEntity
        if (player.getGold() == 100 * x) {
            LevelTag++;
            x++;
        }
    }




    public int CalcDamage() { //Ökar skadan som PlayerEntity gör baserat på Leveltag och GhostKilled med ett start-värde på 30.
        return (LevelTag * (GhostKilled * 12) + 30);
    }

    public int calcHealth() { //Ökar Health för GhostEntity baserat på LevelTag och deras tidigare Health med en Konstant = 30.
        int x = 0;

        if (Tags < LevelTag) {
            for (int i = 1; i < spriteList.size(); i++) {
                GhostEntity g = (GhostEntity) spriteList.get(i);
                if (g.getHealth() < 0) {
                    g.setHealth((int) (g.getHealth() * LevelTag * 1.05 + 30));
                } else {
                    g.setHealth((int) (g.getHealth() * LevelTag * 1.05 + 30));
                }
                x = g.getHealth();
                Tags = LevelTag;
            }
        }
        return x;
    }

    public void CalcTracking() {
        PlayerEntity player = (PlayerEntity) spriteList.get(0);
        for (int i = 1; i < spriteList.size(); i++) {
            GhostEntity ghost = (GhostEntity) spriteList.get(i);
            double xPos = ghost.getxPos() - player.getxPos();
            double yPos = ghost.getyPos() - player.getyPos();
            int dx = xPos == 0 ? 0 : xPos > 0 ? -dxG : dxG;
            int dy = yPos == 0 ? 0 : yPos > 0 ? -dyG : dyG;
            ghost.setDx(dx);
            ghost.setDy(dy);
            if (player.collision(ghost)) {
                player.setHealth(player.getHealth()-20);
                if(player.getHealth() <= 0) {
                    gameRunning = false;
                    System.exit(0);
                }
            }
        }
    }

    public void update(long deltaTime) {
        try {
            String path = getClass().getResource("resources/Droid_Lover/droidlover.ttf").getFile();
            path = URLDecoder.decode(path, "utf-8");

            font = Font.createFont(Font.TRUETYPE_FONT, new File(path));
            font = font.deriveFont(32f); // Typsnittsstorlek
        } catch (IOException | FontFormatException e) {
            System.err.println("Error loading font file: " + e.getMessage());
            e.printStackTrace();
        }
        msg = new TxtContainer("Gold: " + player.getGold() + " PlayerKills: " +GhostKilled, 10, 32, font, Color.GREEN);

        if (GhostKilled >= 2*m) { //Om det har dött mer eller lika med två GhostEntities skapas det fyra till i deras ställe
            if(player.getxPos() < width/2 - 100) { //Kollar var PlayerEntity xPos är och placerar GhostEntity på en viss plats för att det inte ska bli en instakill.
                for (int i = 0; i < 2; i++) {
                    spriteList.add(new GhostEntity(GhostImg, width + player.getxPos(), height * Math.random(), 30, calcHealth(), dy, dx * i, Gold * LevelTag + Gold));
                    spriteList.add(new GhostEntity(GhostImg, width + player.getWidth(), height * Math.random(), 30, calcHealth(), dy, dx * i, Gold * LevelTag + Gold));
                    m++;
                }

            }
            if(player.getxPos() > width/2 + 100) {
                for (int i = 0; i < 2; i++) {
                    spriteList.add(new GhostEntity(GhostImg, width -player.getxPos(), height * Math.random(), 30, calcHealth(), dy, dx * i, Gold * LevelTag + Gold));
                    spriteList.add(new GhostEntity(GhostImg, width -player.getxPos(), height * Math.random(), 30, calcHealth(), dy, dx * i, Gold * LevelTag + Gold));
                    m++;
                }
            }
            else{
                for (int i = 0; i < 2; i++) {
                    spriteList.add(new GhostEntity(GhostImg, width, height * Math.random(), 30, calcHealth(), dy, dx * i, Gold * LevelTag + Gold));
                    spriteList.add(new GhostEntity(GhostImg, width, height * Math.random(), 30, calcHealth(), dy, dx * i, Gold * LevelTag + Gold));
                    m++;
                }
            }
        }
        if (player.laser != null && player.laser.getActive()) {
            Rectangle rec = player.laser.getRectangle();
            if (rec.y < 0 | rec.y > height) {
                player.laser.setActive(false);
                player.laser = null;
            }
            if (rec.x < 0 | rec.x > width) { //Kollar om LaserEntity är utanför skärmen på något sätt och tar bort LaserEntity.
                player.laser.setActive(false);
                player.laser = null;
            }
        }
        if (keyDown.get("up")) { //Ändrar bilden på PlayerEntity baserat på vilken riktning som senast fanns.
            player.setImage(Playerimg[3]);
            direction = "up";
            player.up();
        } else if (keyDown.get("down")) {
            player.setImage(Playerimg[4]);
            direction = "down";
            player.down();
        } else {
            player.setDirectionY(0);
        }

        if (keyDown.get("right")) {
            player.setImage(Playerimg[1]);
            direction = "right";
            player.right();
        } else if (keyDown.get("left")) {
            player.setImage(Playerimg[2]);
            direction = "left";
            player.left();
        } else {
            player.setDirectionX(0);
        }

        if (keyDown.get("exit")) {
            gameRunning = false;
            System.exit(0);
        }
        if (keyDown.get("space")) {
            player.tryToFire(direction);
        }
        player.move(deltaTime);
        if (player.getxPos() < 0) {
            player.setxPos(0);
        }
        if (player.getyPos() < 0) {
            player.setyPos(0);
        }
        if (player.getyPos() + player.getHeight() > height) {
            player.setyPos(height - player.getHeight());
        }
        if (player.getxPos() + player.getWidth() > width) {
            player.setxPos(width - player.getWidth());
        }

        if (player.laser != null && player.laser.getActive()) {
            player.laser.move(deltaTime);
        }


        for (int i = 1; i < spriteList.size(); i++) { //Placerar GhostEntity på andra sidan map för att det går genom skärmen och setter xPos eller yPos baserat på detta.

            if (spriteList.get(i).getxPos() + spriteList.get(i).getWidth() > width) {
                spriteList.get(i).setxPos(0);
            }

            if (spriteList.get(i).getyPos() + spriteList.get(i).getHeight() > height) {
                spriteList.get(i).setyPos(0);
            }

            spriteList.get(i).move(deltaTime);


            if (player.collision(spriteList.get(i))) {
                spriteList.remove(spriteList.get(i));
                GhostKilled++;
                player.setGold((player.getGold()/2));
                player.setHealth(player.getHealth() - 20);
                if(player.getHealth() <= 0) {
                    System.out.println("Congratulations of the score of " +player.getGold() + " Gold! And the score of " +GhostKilled +" Kills!");
                    spriteList.remove(player);
                    gameRunning = false;
                    System.exit(0);
                }
            }
        }
        player.setDamage(CalcDamage()); //Sätter PlayerEntity skada baserat på som sagts tidigare GhostKilled och LevelTag
        checkCollisionAndRemove();
        Upgrade();
    }

    public void render() { //Min render-funktion
        gameScreen.beginRender();
        gameScreen.openRender(msg);
        gameScreen.openRender(spriteList);

        gameScreen.show();

    }

    public void gameLoop() {
        long lastUpdateTime = System.nanoTime();

        while (gameRunning) {
            long deltaTime = System.nanoTime() - lastUpdateTime;

            if (deltaTime > 4166666) {
                lastUpdateTime = System.nanoTime();
                update(deltaTime);
                render();
                CalcTracking();
            }
        }
    }


    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A)
            keyDown.put("left", true);
        else if (key == KeyEvent.VK_D)
            keyDown.put("right", true);
        if (key == KeyEvent.VK_W)
            keyDown.put("up", true);
        else if (key == KeyEvent.VK_S)
            keyDown.put("down", true);
        if (key == KeyEvent.VK_ESCAPE)
            keyDown.put("exit", true);
        if (key == KeyEvent.VK_SPACE)
            keyDown.put("space", true);
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A)
            keyDown.put("left", false);
        else if (key == KeyEvent.VK_D)
            keyDown.put("right", false);
        if (key == KeyEvent.VK_W)
            keyDown.put("up", false);
        else if (key == KeyEvent.VK_S)
            keyDown.put("down", false);
        if (key == KeyEvent.VK_ESCAPE)
            keyDown.put("exit", false);
        if (key == KeyEvent.VK_SPACE)
            keyDown.put("space", false);
    }

    public static void main(String[] args) {
        new GameMain();
    }
}
