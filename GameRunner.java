import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.*;
import java.math.*;
import java.awt.image.*;
import java.applet.*;
import javax.swing.border.*;
import javax.imageio.ImageIO;
public class GameRunner extends JPanel implements KeyListener,Runnable
{
	private float angle;
	private int x;
	private int y;
	private JFrame frame;
	Thread t;
	private boolean gameOn;
	boolean restart=false;
	int imgCount=0;
	Polygon poly;
	Polygon poly2;

	public GameRunner()
	{
		frame=new JFrame();
		x=100;
		y=100;
        gameOn=true;
        
		//int[] x={1,2,3};
		//int[] y={4,5,6};

		poly=new Polygon(x,y, x.length);

		poly2=new Polygon();
		poly2.addPoint(1,4);
		poly2.addPoint(2,5);
		poly2.addPoint(3,6);

		frame.addKeyListener(this);
		frame.add(this);
		frame.setSize(800,500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		t=new Thread(this);
		t.start();
	}

	public void run()
	{
		while(true)
		{
			if(gameOn)
			{
				//Math happens here!


				repaint();
			}
			if(restart)
			{
				restart=false;
				gameOn=true;
			}
			try
			{
				t.sleep(10);
			}catch(InterruptedException e)
			{
			}
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;

		g2d.setColor(Color.BLACK);
		g2d.drawImage(marines[imgCount].getScaledInstance(200, 200, Image.SCALE_DEFAULT),x,y,null);
		g2d.setColor(Color.MAGENTA);
		GradientPaint gp = new GradientPaint((float)0.0, (float)0.0, Color.BLUE, (float)500.0, (float)500, Color.WHITE, true);
		g2d.setPaint(gp);
		g2d.drawLine(100,100,500,500);

	}
	public void keyPressed(KeyEvent key)
	{
		System.out.println(key.getKeyCode());
		if(key.getKeyCode()==39)
		{
			x+=5;
			imgCount++;
			if(imgCount>10)
				imgCount=0;
		}
		if(key.getKeyCode()==82)
			restart=true;
	}
	public void keyReleased(KeyEvent key)
	{
	}
	public void keyTyped(KeyEvent key)
	{
	}
	public static void main(String args[])
	{
		GameRunner app=new GameRunner();
	}
}