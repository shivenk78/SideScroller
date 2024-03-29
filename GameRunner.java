import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.lang.*;
import java.math.*;
import java.awt.image.*;
import java.applet.*;
import javax.swing.border.*;

import javax.imageio.ImageIO;

public class GameRunner extends JPanel implements KeyListener, Runnable {
	private float angle;
	private int skyX, bgX;
	private int[][] locations;
	private Block[][] blocks;
	private JFrame frame;
	private Thread t;
	private boolean gameOn, right, left, rightLock, leftLock, blockUnder;
	private boolean restart = false;
	private BufferedImage sky, background;
	private Marine marine;
	private ArrayList<Block> blockList;
	private ArrayList<Enemy> enemies;
	private String endMessage;
	private static GameRunner gameRunner;

	public GameRunner() {
		frame = new JFrame();
		gameOn = true;

		frame.addKeyListener(this);
		frame.add(this);
		frame.setSize(1024, 750);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		enemies = new ArrayList<>();
		blockList = new ArrayList<>();
		skyX = bgX = 0;
		marine = new Marine(128, 704-192);
		try {
			sky = ImageIO.read(new File("Environments/another-world/PNG/layered/sky.png"));
			background = ImageIO.read(new File("Environments/another-world/PNG/layered/back-towers.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		locations = Block.readMap("map.txt");
		blocks = new Block[locations.length][locations[0].length];
		for(int r=0; r<locations.length; r++){
			for(int c=0; c<locations[r].length; c++){
				blocks[r][c] = new Block(128*c, 128*r, (locations[r][c] == 1));
				if(blocks[r][c].isVisible())
					blockList.add(blocks[r][c]);
			}
		}

		enemies.add(new Enemy(marine.getX()+5*128, 10));
		enemies.add(new Enemy(marine.getX()+6*128, 10));
		enemies.add(new Enemy(marine.getX()+17*128, 10));
		enemies.add(new Enemy(marine.getX()+18*128, 10));
		enemies.add(new Enemy(marine.getX()+19*128, 10));

		t=new Thread(this);
		t.start();
	}

	public void run()
	{
		while(true)
		{
			if(gameOn)
			{
				//Sprite Movement
				if(right && !rightLock){
					skyX -= Marine.MOVE_SPEED/4;
					bgX -= Marine.MOVE_SPEED/2;
					Block.changeAllX(blocks, -Marine.MOVE_SPEED);
					Enemy.changeAllX(enemies, -Marine.MOVE_SPEED);
				}
				if(left && !leftLock){
					skyX += Marine.MOVE_SPEED/4;
					bgX += Marine.MOVE_SPEED/2;
					Block.changeAllX(blocks, Marine.MOVE_SPEED);
					Enemy.changeAllX(enemies, Marine.MOVE_SPEED);    
				}
				if(!right && !left && marine.STATE != Marine.State.JUMP)
					marine.STATE = Marine.State.IDLE;

				if(skyX<-sky.getWidth())
					skyX += sky.getWidth(); 
				if(skyX>2*sky.getWidth())
					skyX -= sky.getWidth();
				if(bgX<-background.getWidth())
					bgX += background.getWidth();
				if(bgX>2*sky.getWidth())
					bgX -= sky.getWidth();

				//Move Enemies 
				for(Enemy e : enemies){
					e.changeY(Marine.MOVE_SPEED/2);
					if(e.getY() > 710)
						e.setY(10);

					if(marine.getCollider().intersects(e.getCollider())){
						endMessage = "GAME OVER";
						gameOn = false;
					}
						
				}

				//Player 'Gravity'
				marine.changeY(marine.getYVel());
				if(marine.getY() <= marine.getYBeforeJump()-(Marine.JUMP_HEIGHT*Marine.JUMP_SPEED)){
					marine.setYVel(Marine.JUMP_SPEED);
					marine.STATE = marine.lastState;
				}
				if(marine.getY()>704){
					endMessage = "GAME OVER";
					gameOn = false;
				}

				//Block Collisions
				blockUnder = rightLock = leftLock = false;
				for(Block b : blockList){
					if(marine.getFutureCollider(0, Marine.JUMP_SPEED).intersects(b.getCollider())){
						marine.setYVel(0);
						System.out.println("Down collision!"+blockList.size());
						blockUnder = true;
					} 
					if(marine.getFutureCollider(Marine.MOVE_SPEED, 0).intersects(b.getCollider())){
						rightLock = true;
					}
					if(marine.getFutureCollider(-Marine.MOVE_SPEED, 0).intersects(b.getCollider())){
						leftLock = true;
					}
				}

				if(!blockUnder && marine.STATE != Marine.State.JUMP){
					marine.setYVel(Marine.JUMP_SPEED);
				}

				//Win Condition
				if(blocks[0][blocks[0].length-1].getCollider().intersects(marine.getCollider())){
					endMessage = "YOU WIN";
					gameOn = false;
				}
				repaint();
			}else{
				
			}
			try
			{
				t.sleep(20);
			}catch(InterruptedException e)
			{
			}
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;

		if(gameOn){
			//Draw Background Layers
			g2d.drawImage(sky, skyX, 0, null);
			g2d.drawImage(sky, skyX+sky.getWidth(), 0, null);
			g2d.drawImage(sky, skyX-sky.getWidth(), 0, null);
			g2d.drawImage(background, bgX, 0, null);
			g2d.drawImage(background, bgX+background.getWidth(), 0, null);
			g2d.drawImage(background, bgX-background.getWidth(), 0, null);

			g2d.setColor(Color.GREEN);
			g2d.fill(blocks[0][blocks[0].length-1].getCollider());

			//Draw Map Blocks
			for(int r=0; r<locations.length; r++){
				for(int c=0; c<locations[r].length; c++){
					Block b = blocks[r][c];
					if(b.isVisible())
						g2d.drawImage(b.getImage().getScaledInstance(128, 128, Image.SCALE_DEFAULT), b.getX(), b.getY(), 128, 128, null);
				}
			}

			//Draw Sprites
			if(marine.right){
				g2d.drawImage(marine.getImage().getScaledInstance(-128, 128, Image.SCALE_DEFAULT), marine.getX(),marine.getY(), marine.getImage().getScaledInstance(-128, 128, Image.SCALE_DEFAULT).getWidth(null), 128, null);
			}else{
				g2d.drawImage(marine.getImage().getScaledInstance(-128, 128, Image.SCALE_DEFAULT), marine.getX()+128,marine.getY(), -marine.getImage().getScaledInstance(-128, 128, Image.SCALE_DEFAULT).getWidth(null), 128, null);
			}

			//Draw Enemies
			for(Enemy e : enemies){
				g2d.drawImage(e.getImage().getScaledInstance(128, 128, Image.SCALE_DEFAULT), e.getX(), e.getY(), -128, 128, null);
			}
		}else{
			g2d.setColor(Color.RED);
			g2d.setFont(new Font("Times New Roman", 10, 100));
			g2d.drawString(endMessage, 200, 200);
		}
	}
	public void keyPressed(KeyEvent key)
	{
		//37 left, 38 up, 39 right, 40 down
		if(key.getKeyCode()==37 || key.getKeyCode()==65){	//Left || A
			if(!leftLock && !right){
				right = false;
				left = true;
				marine.right = false;
			}
			if(marine.STATE != Marine.State.JUMP)
				marine.STATE = Marine.State.RUN;
		}
		if(key.getKeyCode()==39 || key.getKeyCode()==68){	//Right || D
			marine.right = true;
			if(!rightLock && !left){
				right = true;
				left = false;
				marine.right = true;
			}
			if(marine.STATE != Marine.State.JUMP)
				marine.STATE = Marine.State.RUN;
		}
		if(key.getKeyCode()==32){	//Space
			if(marine.STATE != Marine.State.JUMP && marine.getYVel() == 0)
				marine.jump(marine.STATE);
		}
		if(key.getKeyCode()==82)
			restart=true;
	}
	public void keyReleased(KeyEvent key)
	{
		if(key.getKeyCode()==37 || key.getKeyCode()==65){	//Left || A
			left = false;
		}
		if(key.getKeyCode()==39 || key.getKeyCode()==68){	//Right || D
			right = false;
		}
	}
	public void keyTyped(KeyEvent key)
	{
	}
	public static void main(String args[])
	{
		gameRunner=new GameRunner();
	}
}