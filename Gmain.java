import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import javax.swing.*;

import se.egy.graphics.*;

public class GameMain implements KeyListener{
	private HashMap<String, Boolean> keyDown = new HashMap<>();
	private boolean gameRunning = true;
	private PlayerEntity player;
	public int GhostKilled = 0;

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
		Ghostimg = new ImageIcon(getClass().getResource("ghostImg.png")).getImage();


		Playerimg[0] = new ImageIcon(Objects.requireNonNull(getClass().getResource("playerImg.png"))).getImage();
		Playerimg[1] = new ImageIcon(Objects.requireNonNull(getClass().getResource("playerImgRight.png"))).getImage();
		Playerimg[2] = new ImageIcon(Objects.requireNonNull(getClass().getResource("playerImgLeft.png"))).getImage();
		Playerimg[3] = new ImageIcon(Objects.requireNonNull(getClass().getResource("playerImgUp.png"))).getImage();
		Playerimg[4] = new ImageIcon(Objects.requireNonNull(getClass().getResource("playerImgDown.png"))).getImage();
		player = new PlayerEntity(Playerimg[0], (width/2), (height/2), 200);

		spriteList.add(player);

		for (int i = 0; i < 2; i++) {
			spriteList.add(new GhostEntity(Ghostimg,0,(height/100)*i,30, 30,0,1));
		}
		for(int i = 0; i < 2; i++) {
			spriteList.add(new GhostEntity(Ghostimg, (width/100)*i, height, 30, 30,1,0));
		}
	}


	public void checkCollisionAndRemove(){
		ArrayList<Entity> removeList = new ArrayList<>();

		if(player.laser != null && player.laser.getActive()){
			for(int i = 1; i < spriteList.size(); i++) {
				if (spriteList.get(i).collision(player.laser)) {
					player.laser.setActive(false);
					player.laser = null;
					GhostEntity g = (GhostEntity)spriteList.get(i);
					g.setHealth(g.getHealth() - 20);
					if (g.getHealth() <= 0) {
						GhostKilled++;
						removeList.add(g);
					}
				}
			}
		}
		spriteList.removeAll(removeList); // Alt namnet på arraylist
	}
	public double calcHealth() {
		double x = 0;
		for (int i = 0; i < spriteList.size(); i++) {
			GhostEntity g = (GhostEntity)spriteList.get(i);
			if (g.getHealth() < 0) {
				g.setHealth(g.getHealth() * -1.25);
			} else {
				g.setHealth(g.getHealth() * 1.25);
			}
			x = g.getHealth();
		}
		return x;
	}

	public void update(long deltaTime) {
		for(int i = 0; i <spriteList.size(); i++) {

			if(GhostKilled == 4) {
				spriteList.add(new GhostEntity(Ghostimg,0,(height/100)*i,30, calcHealth(),1,0));
				spriteList.add(new GhostEntity(Ghostimg,(width/100)*i,height,30, calcHealth(),0,1));
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


		for (int i = 0; i < spriteList.size(); i++) {

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
