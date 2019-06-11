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
	private int skyX, bgX, jumpTimer;
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
		frame.setSize(1024, 704);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		marine = new Marine(100, 100);
		try {
			sky = ImageIO.read(new File("Environments/another-world/PNG/layered/sky.png"));
			background = ImageIO.read(new File("Environments/another-world/PNG/layered/back-towers.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		jumpTimer = 0;

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
					marine.changeX(Marine.MOVE_SPEED);
					skyX -= Marine.MOVE_SPEED/4;
					bgX -= Marine.MOVE_SPEED/2;
				}
				if(left){
					marine.changeX(-Marine.MOVE_SPEED);
					skyX += Marine.MOVE_SPEED/4;
					bgX += Marine.MOVE_SPEED/2;
				}
				if(!right && !left && marine.STATE != Marine.State.JUMP)
					marine.STATE = Marine.State.IDLE;
				if(Marine.STATE == Marine.State.JUMP){
					//TODO jump mechanic
				}

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
		g2d.drawImage(background, bgX, 0, null);
		g2d.drawImage(background, bgX+background.getWidth(), 0, null);

		//Draw Sprites
		if(marine.right){
			g2d.drawImage(marine.getImage().getScaledInstance(-100, 100, Image.SCALE_DEFAULT), marine.getX(),marine.getY(), 100, 100, null);
		}else{
			g2d.drawImage(marine.getImage().getScaledInstance(-100, 100, Image.SCALE_DEFAULT), marine.getX()+100,marine.getY(), -100, 100, null);
		}
	}
	public void keyPressed(KeyEvent key)
	{
		//37 left, 38 up, 39 right, 40 down
		if(key.getKeyCode()==37){	//Left
			if(!right){
				right = false;
				left = true;
				marine.right = false;
			}
			marine.STATE = Marine.State.RUN;
		}
		if(key.getKeyCode()==39){	//Right
			marine.right = true;
			if(!left){
				right = true;
				left = false;
				marine.right = true;
			}
			marine.STATE = Marine.State.RUN;
		}
		if(key.getKeyCode()==32){	//Space
			if(marine.STATE != Marine.State.JUMP){
				jumpTimer = 0;
				marine.STATE = Marine.State.JUMP;
			}
		}
		if(key.getKeyCode()==82)
			restart=true;
	}
	public void keyReleased(KeyEvent key)
	{
		if(key.getKeyCode()==37){	//Left
			left = false;
		}
		if(key.getKeyCode()==39){	//Right
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