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

public class GameMain implements KeyListener{
	private HashMap<String, Boolean> keyDown = new HashMap<>();
	private boolean gameRunning = true;
	private PlayerEntity player;
	public int GhostKilled = 0;
	public int playerKills = 0;
	public int LevelTag = 0;
	private GameScreen gameScreen;
	public int Constant = 1;
	int width = 1200;
	int height = 1200;
	public int dy;
	public int dx;
	public int dxG = 3;
	public int dyG = 3;
	public String direction;
	Image[] Playerimg = new Image[5];
	public Font font = null;
	private TxtContainer msg;

	Image Ghostimg;
	private ArrayList<Entity> spriteList = new ArrayList<Entity>();

	public GameMain(){
		loadImages();
		gameScreen = new GameScreen("Game", width, height, false); // false vid testkörning
		gameScreen.setKeyListener(this);
		keyDown.put("left", false);
		keyDown.put("right", false);
		keyDown.put("up", false);
		keyDown.put("down", false);
		keyDown.put("exit", false);
		keyDown.put("space", false);
		gameLoop();

	}
	public void checkCollisionAndRemove(){
		ArrayList<Entity> removeList = new ArrayList<>();

		if(player.laser != null && player.laser.getActive()){
			for(int i = 1; i < spriteList.size(); i++) {
				if (spriteList.get(i).collision(player.laser)) {
					player.laser.setActive(false);
					player.laser = null;
					GhostEntity g = (GhostEntity)spriteList.get(i);
					g.setHealth(g.getHealth() - player.getDamage());
					if (g.getHealth() <= 0) {
						playerKills++;
						GhostKilled++;
						player.setGold(player.getGold() + g.getGold());
						removeList.add(g);
					}
				}
				if(player.laser == null)
					break;
			}
		}
		spriteList.removeAll(removeList); // Alt namnet på arraylist
	}

	public void loadImages(){
		Ghostimg = new ImageIcon(getClass().getResource("resources/ghostImg.png")).getImage();
		Playerimg[0] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImg.png"))).getImage();
		Playerimg[1] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImgRight.png"))).getImage();
		Playerimg[2] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImgLeft.png"))).getImage();
		Playerimg[3] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImgUp.png"))).getImage();
		Playerimg[4] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImgDown.png"))).getImage();
		player = new PlayerEntity(Playerimg[0], (width/2), (height/2), 400, 0, LevelTag*(playerKills*25) +1000);
		spriteList.add(player);
		for (int i = 1; i < 2; i++) {
			spriteList.add(new GhostEntity(Ghostimg,width*Math.random(),height*Math.random(),30, 30, dy*i, dx, 5));
		}
		for(int i = 1; i < 2; i++) {
			spriteList.add(new GhostEntity(Ghostimg,width*Math.random(),height*Math.random(),30, 30, dy*i, dx, 5));
		}
	}


		public void Upgrade() {
		if(player.getGold() >= 100) {
			LevelTag++;
			for(int i = 1; i < spriteList.size(); i++) {
				spriteList.remove(spriteList.get(i));
				player.setGold(0);
			}
		}
	}
		public void GhostLocation() {
			for(int i = 1; i < Constant; i++) {
				System.out.println("Test");
				spriteList.add(new GhostEntity(Ghostimg, width*Math.random(),height*Math.random(),30, 30, dy*i, dx, 5*LevelTag));
				spriteList.add(new GhostEntity(Ghostimg, width*Math.random(),height*Math.random(),30, 30, dy, dx*i, 5*LevelTag));
				double x = player.getxPos() - spriteList.get(i).getxPos();
				double y = player.getyPos() - spriteList.get(i).getyPos();
				double z = Math.sqrt(Math.pow(x, 2)+ Math.pow(y, 2));
				if(z < 200) {
					spriteList.get(i).setxPos(width*Math.random());
					spriteList.get(i).setyPos(height*Math.random());
				}
			}
		}
		public int calcHealth() {
			int x = 0;
			for (int i = 1; i < spriteList.size(); i++) {
				GhostEntity g = (GhostEntity)spriteList.get(i);
				System.out.println(g.getHealth());
				if (g.getHealth() < 0) {
					g.setHealth((int) (g.getHealth() * LevelTag*1.05 + 30));
				} else {
					g.setHealth((int) (g.getHealth() * LevelTag*1.05 + 30));
				}
				x = g.getHealth();
			}
			return x;
		}
		public int calcSpeed() {
		int x = 0;
		for(int i = 1; i < spriteList.size(); i++) {
			GhostEntity g = (GhostEntity) spriteList.get(i);
			if(g.getHealth() <= 0) {
				g.setSpeed((int) (g.getSpeed() * LevelTag*1.25 + 30));
			}
			x = g.getSpeed();
		}
		return x;
	}

		public void CalcTracking() {
		try {
			PlayerEntity p = (PlayerEntity) spriteList.get(0);
			for (int i = 1; i < spriteList.size(); i++) {
				GhostEntity g = (GhostEntity) spriteList.get(i);
				if (g.getxPos() != p.getxPos() && g.getyPos() != p.getyPos()) {
					double xPos = g.getxPos() - p.getxPos();
					double yPos = g.getyPos() - p.getyPos();
					if (yPos < 0) {
						if (xPos > 0) {
							g.setDy(dyG);
							g.setDx(-dxG);
						}
						if (xPos == 0) {
							g.setDy(dyG);
							g.setDx(0);
						}
						if (xPos < 0) {
							g.setDy(dyG);
							g.setDx(dxG);
						}
					}
					if (yPos > 0) {
						if (xPos > 0) {
							g.setDy(-dyG);
							g.setDx(-dxG);
						}
						if (xPos == 0) {
							g.setDy(-dyG);
							g.setDx(0);
						}
						if (xPos < 0) {
							g.setDy(-dyG);
							g.setDx(dxG);
						}
					}
					if (yPos == 0) {
						if (xPos > 0) {
							g.setDy(0);
							g.setDx(-dxG);
						}
						if (xPos < 0) {
							g.setDy(0);
							g.setDx(dxG);
						}
					}
				} else {
					gameRunning = false;
					try {
						gameScreen.close();
					} catch (GameCloseException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}catch (ClassCastException e) {
			throw new RuntimeException(e);
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
			msg = new TxtContainer("Gold: " + player.getGold(), 10, 32, font, Color.GREEN);
			for(int i = 1; i <spriteList.size(); i++) {
				if(GhostKilled == 1) {
					for(int x = 0; x < 2; x++) {
						spriteList.add(new GhostEntity(Ghostimg,width*Math.random(),height*Math.random(),calcSpeed(), calcHealth(),dy,dx*x, 5));
						spriteList.add(new GhostEntity(Ghostimg,width*Math.random(),height*Math.random(),calcSpeed(), calcHealth(),dy,dx*x, 5));
						GhostKilled = 0;
					}
				}

			}
			if(player.laser != null && player.laser.getActive()) {
				Rectangle rec = player.laser.getRectangle();
				if(rec.y < 0 | rec.y > height) {
					player.laser.setActive(false);
					player.laser = null;
				}
				if(rec.x < 0 | rec.x > width) {
					player.laser.setActive(false);
					player.laser = null;
				}
			}
			if (keyDown.get("up")) {
				player.setImage(Playerimg[3]);
				direction = "up";
				player.up();
			}
			else if (keyDown.get("down")) {
				player.setImage(Playerimg[4]);
				direction = "down";
				player.down();
			}

			else {
				player.setDirectionY(0);
			}

			if (keyDown.get("right")) {
				player.setImage(Playerimg[1]);
				direction = "right";
				player.right();
			}

			else if (keyDown.get("left")) {
				player.setImage(Playerimg[2]);
				direction = "left";
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
				player.tryToFire(direction);
			}
			player.move(deltaTime);
			if(player.getxPos() < 0) { player.setxPos(0);}
			if(player.getyPos() < 0) { player.setyPos(0);}
			if(player.getyPos() + player.getHeight() > height) {player.setyPos(height- player.getHeight());}
			if(player.getxPos() + player.getWidth() > width) {player.setxPos(width- player.getWidth());}

			if(player.laser != null && player.laser.getActive()) {
				player.laser.move(deltaTime);
			}


			for (int i = 1; i < spriteList.size(); i++) {

				if(spriteList.get(i).getxPos() + spriteList.get(i).getWidth() > width) {
					spriteList.get(i).setxPos(0);
				}

				if(spriteList.get(i).getyPos() + spriteList.get(i).getHeight() > height) {
					spriteList.get(i).setyPos(0);
				}

				spriteList.get(i).move(deltaTime);

				// FARLIG
				if (player.collision(spriteList.get(i))) {
					spriteList.remove(player);
					gameRunning = false;
					try {
						gameScreen.close();
					} catch (GameCloseException e) {
						throw new RuntimeException(e);
					}
				}
			}

			checkCollisionAndRemove();
			Upgrade();
			GhostLocation();
		}

		public void render(){
			gameScreen.beginRender();
			gameScreen.openRender(msg);
			gameScreen.openRender(spriteList);

			gameScreen.show();

		}

		public void gameLoop() {
			long lastUpdateTime = System.nanoTime();

			while (gameRunning) {
				long deltaTime = System.nanoTime() - lastUpdateTime;

				if (deltaTime > 8333333) {
					lastUpdateTime = System.nanoTime();
					update(deltaTime);
					render();
					CalcTracking();
					for(int i = 1; i < spriteList.size(); i++) {
						GhostEntity g = (GhostEntity) spriteList.get(i);
						if(spriteList.size() >= 10*LevelTag+ 10) {
							spriteList.remove(g);
						}
					}
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
