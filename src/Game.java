/**
 * @author Matthew Schueler
 * @version 6.0
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
	public PlayerShip player;
	public HeadsUpDisplay hud;
	private Thread th;
	
	public final static int TICK_TIME = 20;
	public final static int X_SIZE = 854, Y_SIZE = 480;
		
	public void init() {
		setSize(X_SIZE,Y_SIZE);
		offScreen = createImage(X_SIZE,Y_SIZE);
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
			getContentPane().add(hud);
			getContentPane().add(player);
			getContentPane().revalidate();
			getContentPane().requestFocus();
			while(true) {
				for (Component c : getContentPane().getComponents()) {
					if (c instanceof GameComponent)
						((GameComponent)c).moveTick();
				}
				repaint();				
				revalidate();
				try {
					Thread.sleep(TICK_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}		
	}	
}
