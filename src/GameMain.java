import java.awt.Image;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.swing.ImageIcon;

import Entity.*;
import se.egy.graphics.*;

public class GameMain implements KeyListener{
	private HashMap<String, Boolean> keyDown = new HashMap<>();
	private boolean gameRunning = true;
	private PlayerEntity player;
	private GhostEntity[] Ghost1 = new GhostEntity[1];
	private GhostEntity[] Ghost2 = new GhostEntity[1];
	private GameScreen gameScreen;

	int width = 800;
	int height = 600;
	Image[] Playerimg = new Image[3];

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
		loadImages();
		gameLoop();
	}

	public void loadImages(){
		Ghostimg = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/ghostImg.png"))).getImage();
		Playerimg[0] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImg.png"))).getImage();
		Playerimg[1] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImgRight.png"))).getImage();
		Playerimg[2] = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/playerImgLeft.png"))).getImage();
		player = new PlayerEntity(Playerimg[0], (width/2), (height/2), 200);
		spriteList.add(player);

		for (int i = 0; i < Ghost1.length; i++) {
			Ghost1[i] = new GhostEntity(Ghostimg,0,(height/10)*i,30);
			spriteList.add(Ghost1[i]);
		}
		for(int i = 0; i < Ghost2.length; i++) {
			Ghost2[i] = new GhostEntity(Ghostimg, (width/10)*i, height, 30);
			spriteList.add(Ghost2[i]);
		}
	}




	public void update(long deltaTime) {
		if (keyDown.get("up"))
			player.up(-1);
		else if (keyDown.get("down"))
			player.down(1);
		else player.setDirectionY(0);

		if (keyDown.get("right")) {
			player.setImage(Playerimg[1]);
			player.left(1);
		}

		else if (keyDown.get("left")) {
			player.setImage(Playerimg[2]);
			player.left(-1);
		}

		else {
			player.setImage(Playerimg[0]);
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
		player.move(deltaTime);
		if(player.getxPos() < 0) { player.setxPos(width - player.getWidth());}
		if(player.getyPos() < 0) { player.setyPos(height - player.getHeight());}
		if(player.getyPos() + player.getHeight() > height) {player.setyPos(0);}
		if(player.getxPos() + player.getWidth() > width) {player.setxPos(0);}
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
			Ghost2[i].setxPos((width/10)*i);
		}
		for(int i = 0; i < Ghost1.length; i++) {
			Ghost1[i].move(deltaTime);
			Ghost1[i].setyPos((height/10)*i);
		}
	}

	public void render(){
		gameScreen.render(spriteList);
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
		}
		public static void main (String[]args){
			new GameMain();
		}
	}
