/**
 * @author Matthew Schueler
 * @version 5.0
 */

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JApplet;
import javax.swing.SwingWorker;

public class Game extends JApplet implements Runnable {

	private static final long serialVersionUID = 1L;
	BufferedImage img = null;
	public Image offScreen;
	public Graphics2D g2d;
	int xSize = 854, ySize = 480;
	public PlayerShip player;
	public HeadsUpDisplay hud;
	private Thread th;
		
	public void init() {
		setSize(xSize,ySize);
		offScreen = createImage(xSize,ySize);
		g2d = (Graphics2D)(offScreen.getGraphics());
		player = new PlayerShip();
		hud = new HeadsUpDisplay(player);
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
		//g2d.clearRect(0,0,854,480);
		super.paint(g);
		for (Component c : getContentPane().getComponents()) {
			c.paint(g);
		}
	}
	
	public void update(Graphics g) {
		paint(g);
	}
	
	class GameWorker extends SwingWorker<Object, Object> {
		@Override
		protected Object doInBackground() throws Exception {
			getContentPane().add(hud,0);
			getContentPane().add(player,1);
			getContentPane().revalidate();
			getContentPane().requestFocus();
			while(true) {
				player.moveTick();
				/*getContentPane().*/repaint();
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
