import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import java.io.File;
import javax.imageio.ImageIO;

import javax.swing.*;

import Entity.*;
import se.egy.graphics.*;

public class GameMain implements KeyListener{
	private HashMap<String, Boolean> keyDown = new HashMap<>();
	private boolean gameRunning = true;
	private PlayerEntity player;
	public int GhostKilled = 0;
	private TabEntity Gold;
	private TabEntity Dmg;
	private TabEntity Amount;
	private GhostEntity[] Ghost1 = new GhostEntity[3];
	private GhostEntity[] Ghost2 = new GhostEntity[3];
	private Image[] GoldR = new Image[3];
	private Image[] GoldN = new Image[3];
	private Image[] DmgR = new Image[3];
	private Image[] DmgN = new Image[3];
	private Image[] AmountR = new Image[3];
	private Image[] AmountN = new Image[3];
	Image Laserimg;
	private GameScreen gameScreen;

	int width = 1600;
	int height = 1200;
	Image[] Playerimg = new Image[5];

	Image Ghostimg;
	private ArrayList<Entity> spriteList = new ArrayList<Entity>();

	public GameMain(){
		gameScreen = new GameScreen("Game", width, height, false); // false vid testkörning
		gameScreen.setKeyListener(this);

		keyDown.put("left", false); 
		keyDown.put("right", false);
		keyDown.put("up", false);
		keyDown.put("down", false);
		keyDown.put("exit", false);
		keyDown.put("space", false);
		loadImages();
		gameLoop();
	}

	public void loadImages(){
		Ghostimg = new ImageIcon(getClass().getResource("resources/ghostImg.png")).getImage();

		GoldR[0] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/x1GoldR.png"))).getImage();
		GoldR[1] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/x10GoldR.png"))).getImage();
		GoldR[2] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/x100GoldR.png"))).getImage();

		GoldN[0] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/x1GoldN.png"))).getImage();
		GoldN[1] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/x10GoldN.png"))).getImage();
		GoldN[2] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/x100GoldN.png"))).getImage();

		DmgR[0] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/x1DmgR.png"))).getImage();
		DmgR[1] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/x10DmgR.png"))).getImage();
		DmgR[2] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/x100DmgR.png"))).getImage();

		DmgN[0] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/x1DmgN.png"))).getImage();
		DmgN[1] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/x10DmgN.png"))).getImage();
		DmgN[2] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/x100DmgN.png"))).getImage();

		AmountR[0] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/x1AmountR.png"))).getImage();
		AmountR[1] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/x10AmountR.png"))).getImage();
		AmountR[2] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/x100AmountR.png"))).getImage();

		AmountN[0] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/x1AmountN.png"))).getImage();
		AmountN[1] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/x10AmountN.png"))).getImage();
		AmountN[2] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/x100AmountN.png"))).getImage();

		Playerimg[0] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImg.png"))).getImage();
		Playerimg[1] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImgRight.png"))).getImage();
		Playerimg[2] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImgLeft.png"))).getImage();
		Playerimg[3] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImgUp.png"))).getImage();
		Playerimg[4] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImgDown.png"))).getImage();
		player = new PlayerEntity(Playerimg[0], (width/2), (height/2), 200);

		Gold = new TabEntity(DmgR[0], 1500, 0, 0 );
		Dmg = new TabEntity(GoldR[0], 1500, 100, 0 );
		Amount = new TabEntity(AmountR[0], 1500, 200, 0 );
		spriteList.add(Gold);
		spriteList.add(Dmg);
		spriteList.add(Amount);
		spriteList.add(player);
			if (player.laser != null && player.laser.getActive()) {
				spriteList.add(player.laser);
		}

		for (int i = 0; i < Ghost1.length; i++) {
			Ghost1[i] = new GhostEntity(Ghostimg,0,(height/100)*i,30, 30);
			spriteList.add(Ghost1[i]);
		}
		for(int i = 0; i < Ghost2.length; i++) {
			Ghost2[i] = new GhostEntity(Ghostimg, (width/100)*i, height, 30, 30);
			spriteList.add(Ghost2[i]);
		}
	}


	public void checkCollisionAndRemove(){
		ArrayList<Entity> removeList = new ArrayList<>();

			if(player.laser != null && player.laser.getActive()){
				for(int i = 0; i < Ghost2.length; i++) {
					if (Ghost2[i].collision(player.laser)) {
						removeList.add(player.laser);
						Ghost2[i].setHealth(Ghost2[i].getHealth() - 20);
						if (Ghost2[i].getHealth() <= 0) {
							GhostKilled++;
							removeList.add(Ghost2[i]);
						}
					}
				}

					for(int i = 0; i < Ghost1.length; i++) {
					if(Ghost1[i].collision(player.laser)) {
						removeList.add(player.laser);
						Ghost1[i].setHealth(Ghost1[i].getHealth() - 20);
						if(Ghost1[i].getHealth() <= 0) {
							GhostKilled++;
							removeList.add(Ghost1[i]);
						}
					}
				}
			}
		spriteList.removeAll(removeList); // Alt namnet på arraylist
	}
	public double calc2Health() {
		double x = 0;
		for (int i = 0; i < Ghost2.length; i++) {
			if (Ghost2[i].getHealth() < 0) {
				Ghost2[i].setHealth(Ghost2[i].getHealth() * -1.25);
			} else {
				Ghost2[i].setHealth(Ghost2[i].getHealth() * 1.25);
			}
			x = Ghost2[i].getHealth();
		}
		return x;
	}
	public double calc1Health() {
		double x = 0;
		for(int i = 0; i < Ghost1.length; i++) {
			if(Ghost1[i].getHealth() < 0) {
				Ghost1[i].setHealth(Ghost1[i].getHealth() * -1.25);
			}
			else{
				Ghost1[i].setHealth(Ghost1[i].getHealth() * 1.25);
			}
			x = Ghost1[i].getHealth();
		}
		return x;
	}

	public void update(long deltaTime) {
		for(int i = 0; i < Ghost2.length; i++) {
			System.out.println(Ghost1[i].getHealth());
			if(GhostKilled == Ghost1.length + Ghost2.length) {
				Ghost2[i] = new GhostEntity(Ghostimg,0,(height/100)*i,30, calc2Health());
				Ghost1[i] = new GhostEntity(Ghostimg,(width/100)*i,height,30, calc1Health());
				spriteList.add(Ghost2[i]);
				spriteList.add(Ghost1[i]);
				GhostKilled = 0;
			}
		}
		if(player.laser != null && player.laser.getActive()) {
			Rectangle rec = player.laser.getRectangle();
			if(rec.y < 0 | rec.y > height) {
				player.laser.setActive(false);
				player.laser = null;
			}
		}
		if (keyDown.get("up")) {
			player.setImage(Playerimg[3]);
			player.up();
		}
		else if (keyDown.get("down")) {
			player.setImage(Playerimg[4]);
			player.down();
		}

		else {
			player.setDirectionY(0);
		}

		if (keyDown.get("right")) {
			player.setImage(Playerimg[1]);
			player.right();
		}

		else if (keyDown.get("left")) {
			player.setImage(Playerimg[2]);
			player.left();
		}

		else {
			player.setDirectionX(0);
		}

		if (keyDown.get("exit")) {
			gameRunning = false;
			try {
				gameScreen.close();
			} catch (GameCloseException e) {
				throw new RuntimeException(e);
			}
		}
		if(keyDown.get("space")) {
			player.tryToFire();
		}
		player.move(deltaTime);
		if(player.getxPos() < 0) { player.setxPos(width - player.getWidth());}
		if(player.getyPos() < 0) { player.setyPos(height - player.getHeight());}
		if(player.getyPos() + player.getHeight() > height) {player.setyPos(0);}
		if(player.getxPos() + player.getWidth() > width) {player.setxPos(0);}

		if(player.laser != null && player.laser.getActive()) {
			player.laser.move(deltaTime);
		}


		for (int i = 0; i < Ghost1.length; i++) {
				if (Ghost1[i].getxPos() < width) {
					Ghost1[i].setDirectionX(1);
				}
				if(Ghost1[i].getxPos() + Ghost1[i].getWidth() > width) {
					Ghost1[i].setxPos(0);
				}
		}
		for(int i = 0; i < Ghost2.length; i++) {
			if(Ghost2[i].getyPos() < height) {
				Ghost2[i].setDirectionY(1);
			}
			if(Ghost2[i].getyPos() + Ghost2[i].getHeight() > height) {
				Ghost2[i].setyPos(0);
			}
		}
		for (int i = 0; i < Ghost1.length; i++) {
			if (player.collision(Ghost1[i]) | player.collision(Ghost2[i]))
				spriteList.remove(player);
		}


		for(int i = 0; i < Ghost2.length; i++) {
			Ghost2[i].move(deltaTime);
			Ghost2[i].setxPos((width/5)*i);
		}
		for(int i = 0; i < Ghost1.length; i++) {
			Ghost1[i].move(deltaTime);
			Ghost1[i].setyPos((height/5)*i);
		}
		checkCollisionAndRemove();
	}

	public void render(){


		gameScreen.beginRender();

		gameScreen.openRender(spriteList);

		gameScreen.show();
	}

	public void gameLoop() {
			long lastUpdateTime = System.nanoTime();

			while (gameRunning) {
				long deltaTime = System.nanoTime() - lastUpdateTime;

				if (deltaTime > 33333333) {
					lastUpdateTime = System.nanoTime();
					update(deltaTime);
					render();
				}
			}
		}

		/** Spelets tangentbordslyssnare */
		public void keyTyped (KeyEvent e){
		}

		public void keyPressed (KeyEvent e){
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_A)
				keyDown.put("left", true);
			else if (key == KeyEvent.VK_D)
				keyDown.put("right", true);
			if (key == KeyEvent.VK_W)
				keyDown.put("up", true);
			else if (key == KeyEvent.VK_S)
				keyDown.put("down", true);
			if(key == KeyEvent.VK_ESCAPE)
				keyDown.put("exit", true);
			if(key == KeyEvent.VK_SPACE)
				keyDown.put("space", true);
		}

		public void keyReleased (KeyEvent e){
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_A)
				keyDown.put("left", false);
			else if (key == KeyEvent.VK_D)
				keyDown.put("right", false);
			if (key == KeyEvent.VK_W)
				keyDown.put("up", false);
			else if (key == KeyEvent.VK_S)
				keyDown.put("down", false);
			if(key == KeyEvent.VK_ESCAPE)
				keyDown.put("exit", false);
			if(key == KeyEvent.VK_SPACE)
				keyDown.put("space", false);
		}
		public static void main (String[]args){
			new GameMain();
		}
	}
