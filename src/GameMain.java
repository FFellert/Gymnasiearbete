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
	public int GhostKilled = 1;
	public int playerKills = 0;
	private TabEntity GoldTAB;
	private TabEntity DmgTAB;
	private TabEntity AmountTAB;
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
	public int dy;
	public int dx;
	public int Gold = 0;
	Image[] Playerimg = new Image[5];
	public Font font = null;
	private TxtContainer msg;

	Image Ghostimg;
	private ArrayList<Entity> spriteList = new ArrayList<Entity>();

	public GameMain(){
		try {
			String path = getClass().getResource("resources/Droid_Lover/droidlover.ttf").getFile();
			path = URLDecoder.decode(path, "utf-8");

			font = Font.createFont(Font.TRUETYPE_FONT, new File(path));
			font = font.deriveFont(32f); // Typsnittsstorlek
		} catch (IOException | FontFormatException e) {
			System.err.println("Error loading font file: " + e.getMessage());
			e.printStackTrace();
		}
		loadImages();
		msg = new TxtContainer(Integer.toString(Gold), 10, 32, font, Color.GREEN);
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

	public void loadImages(){
		Ghostimg = new ImageIcon(getClass().getResource("resources/ghostImg.png")).getImage();
		/*
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

		 */
		Playerimg[0] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImg.png"))).getImage();
		Playerimg[1] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImgRight.png"))).getImage();
		Playerimg[2] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImgLeft.png"))).getImage();
		Playerimg[3] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImgUp.png"))).getImage();
		Playerimg[4] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImgDown.png"))).getImage();
		player = new PlayerEntity(Playerimg[0], (width/2), (height/2), 200, 0, calcDamage());

		//Gold = new TabEntity(DmgR[0], 1500, 0, 0 );
		//Dmg = new TabEntity(GoldR[0], 1500, 100, 0 );
		//Amount = new TabEntity(AmountR[0], 1500, 200, 0 );
		spriteList.add(player);
		//spriteList.add(Gold);
		//spriteList.add(Dmg);
		//spriteList.add(Amount);

		for (int i = 1; i < 2; i++) {
			spriteList.add(new GhostEntity(Ghostimg,0,(height/10)*i,30, 30, dy, dx, 5));
		}
		for(int i = 1; i < 2; i++) {
			spriteList.add(new GhostEntity(Ghostimg,0,(height/10)*i,30, 30, dy, dx, 5));
		}
	}

	public int calcDamage() {
		int Damage = playerKills*30 + 30;
		return Damage;
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
							Gold += g.getGold();
							removeList.add(g);
						}
					}
					if(player.laser == null)
						break;
				}
			}
			spriteList.removeAll(removeList); // Alt namnet på arraylist
		}
		public int calcHealth() {
			int x = 0;
			for (int i = 1; i < spriteList.size(); i++) {
				GhostEntity g = (GhostEntity)spriteList.get(i);
				if (g.getHealth() < 0) {
					g.setHealth((int) (g.getHealth() * -1.25));
				} else {
					g.setHealth((int) (g.getHealth() * 1.25));
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
				g.setSpeed((int) (g.getSpeed() * 1.5));
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
							g.setDy(2);
							g.setDx(-2);
						}
						if (xPos == 0) {
							g.setDy(2);
							g.setDx(0);
						}
						if (xPos < 0) {
							g.setDy(2);
							g.setDx(2);
						}
					}
					if (yPos > 0) {
						if (xPos > 0) {
							g.setDy(-2);
							g.setDx(-2);
						}
						if (xPos == 0) {
							g.setDy(-2);
							g.setDx(0);
						}
						if (xPos < 0) {
							g.setDy(-2);
							g.setDx(2);
						}
					}
					if (yPos == 0) {
						if (xPos > 0) {
							g.setDy(0);
							g.setDx(-2);
						}
						if (xPos < 0) {
							g.setDy(0);
							g.setDx(2);
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
		calcDamage();
			for(int i = 1; i <spriteList.size(); i++) {
				if(GhostKilled == 4) {
					for(int x = 0; x < 3; x++) {
						spriteList.add(new GhostEntity(Ghostimg,0,(height/10)*x,calcSpeed(), calcHealth(),1,0, 5));
						spriteList.add(new GhostEntity(Ghostimg,(width/10)*x,height,calcSpeed(), calcHealth(),0,1, 5));
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


			for (int i = 1; i < spriteList.size(); i++) {

				if(spriteList.get(i).getxPos() + spriteList.get(i).getWidth() > width) {
					spriteList.get(i).setxPos(0);
				}

				if(spriteList.get(i).getyPos() + spriteList.get(i).getHeight() > height) {
					spriteList.get(i).setyPos(0);
				}

				spriteList.get(i).move(deltaTime);

				// FARLIG
				if (player.collision(spriteList.get(i)))
					spriteList.remove(player);
			}

			checkCollisionAndRemove();
		}

		public void render(){
		gameScreen.render(msg);
/*
			gameScreen.beginRender();
			gameScreen.openRender(msg);
			gameScreen.openRender(spriteList);

			gameScreen.show();

 */
		}

		public void gameLoop() {
			long lastUpdateTime = System.nanoTime();

			while (gameRunning) {
				long deltaTime = System.nanoTime() - lastUpdateTime;

				if (deltaTime > 33333333) {
					lastUpdateTime = System.nanoTime();
					update(deltaTime);
					render();
					CalcTracking();
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
