import javax.swing.*;
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
	private boolean gameOn, right, left;
	private boolean restart = false;
	private BufferedImage sky, background;
	private Marine marine;

	public GameRunner() {
		frame = new JFrame();
		gameOn = true;

		frame.addKeyListener(this);
		frame.add(this);
		frame.setSize(1024, 750);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

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
				System.out.print(locations[r][c]+" ");
			}
			System.out.println("");
		}

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
				if(right){
					skyX -= Marine.MOVE_SPEED/4;
					bgX -= Marine.MOVE_SPEED/2;
					Block.changeAllX(blocks, -Marine.MOVE_SPEED);
				}
				if(left){
					skyX += Marine.MOVE_SPEED/4;
					bgX += Marine.MOVE_SPEED/2;
					Block.changeAllX(blocks, Marine.MOVE_SPEED);
				}
				if(!right && !left && marine.STATE != Marine.State.JUMP)
					marine.STATE = Marine.State.IDLE;

				if(skyX<-sky.getWidth())
					skyX += sky.getWidth(); 
				if(bgX<-background.getWidth())
					bgX += background.getWidth();

				repaint();
			}
			if(restart)
			{
				restart=false;
				gameOn=true;
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

		//Draw Background Layers
		g2d.drawImage(sky, skyX, 0, null);
		g2d.drawImage(sky, skyX+sky.getWidth(), 0, null);
		g2d.drawImage(sky, skyX-sky.getWidth(), 0, null);
		g2d.drawImage(background, bgX, 0, null);
		g2d.drawImage(background, bgX+background.getWidth(), 0, null);
		g2d.drawImage(background, bgX-background.getWidth(), 0, null);

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
	}
	public void keyPressed(KeyEvent key)
	{
		//37 left, 38 up, 39 right, 40 down
		if(key.getKeyCode()==37 || key.getKeyCode()==65){	//Left || A
			if(!right){
				right = false;
				left = true;
				marine.right = false;
			}
			if(marine.STATE != Marine.State.JUMP)
				marine.STATE = Marine.State.RUN;
		}
		if(key.getKeyCode()==39 || key.getKeyCode()==68){	//Right || D
			marine.right = true;
			if(!left){
				right = true;
				left = false;
				marine.right = true;
			}
			if(marine.STATE != Marine.State.JUMP)
				marine.STATE = Marine.State.RUN;
		}
		if(key.getKeyCode()==32){	//Space
			if(marine.STATE != Marine.State.JUMP)
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
		GameRunner app=new GameRunner();
	}
}