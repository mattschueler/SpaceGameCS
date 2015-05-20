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
import javax.swing.SwingWorker;

public class Game extends JApplet implements Runnable {

	private static final long serialVersionUID = 1L;
	BufferedImage img = null;
	public Image offScreen;
	public Graphics2D g2d;
	int xSize = 854, ySize = 480;
	public PlayerShip player;//= new PlayerShip();
	//public HeadsUpDisplay hud;//= new HeadsUpDisplay(xSize, ySize, player);
	private Thread th;
		
	public void init() {
		setSize(xSize,ySize);
		offScreen = createImage(xSize,ySize);
		g2d = (Graphics2D)(offScreen.getGraphics());
		player = new PlayerShip();
		//hud = new HeadsUpDisplay(xSize, ySize, player);
		th = new Thread(this);
		th.start();
	}
	
	@Override
	public void run() {
		(new GameWorker()).execute();
	}
	
	public void stop() {
		th = null;
	}
	
	public void paint(Graphics g) {
		g2d.clearRect(0,0,854,480);
		getContentPane().paint(g);
	}
	
	public void update(Graphics g) {
		paint(g);
	}
	
	class GameWorker extends SwingWorker<Object, Object> {

		@Override
		protected Object doInBackground() throws Exception {
			player = new PlayerShip();
			//hud = new HeadsUpDisplay(xSize, ySize, player);
			//getContentPane().add(hud);
			getContentPane().add(player);
			getContentPane().revalidate();
			getContentPane().requestFocus();
			getContentPane().setVisible(true);
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
		
		
		
	}
	
}
