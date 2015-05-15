/**
 * @author Matthew Schueler
 * @version 4.0
 */

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.JApplet;

public class Game extends JApplet implements Runnable {

	private static final long serialVersionUID = 1L;
	BufferedImage img = null;
	public Image offScreen;
	public Graphics2D g2d;
	int xSize = 854, ySize = 480;
	public PlayerShip player;//= new PlayerShip();
	//public HeadsUpDisplay hud;//= new HeadsUpDisplay(xSize, ySize, player);
		
	public void init() {
		setSize(xSize,ySize);
		offScreen = createImage(xSize,ySize);
		g2d = (Graphics2D)(offScreen.getGraphics());
		player = new PlayerShip();
		//hud = new HeadsUpDisplay(xSize, ySize, player);
		Thread th = new Thread(this);
		th.start();
	}
	
	@Override
	public void run() {
		player = new PlayerShip();
		getContentPane().add(player);
		add(player);
		//hud = new HeadsUpDisplay(xSize, ySize, player);
		//getContentPane().add(hud);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		getContentPane().revalidate();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println(player);
		while(true) {
			player.moveTick();
			repaint();
			player.isOffscreen(xSize, ySize);
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void paint(Graphics g) {
		g2d.clearRect(0,0,854,480);
		getContentPane().paint(g);
	}
	
	public void update(Graphics g) {
		paint(g);
	}
	
}
