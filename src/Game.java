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
	private int gameTicks = 0;
	private static int gameScore = 0;
	
	public final static int TICK_TIME = 20; //time in msecs of one frame
	public final static int X_SIZE = 854, Y_SIZE = 480;
	public final static int SPAWN_GAP = 10; //seconds between increasing the max num of asteroids
		
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
				Asteroid a = addAsteroid();
				if(a!=null) getContentPane().add(a);
				repaint();				
				revalidate();
				try {
					Thread.sleep(TICK_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//keep track of number of frames the game has been played for
				gameTicks++;
				if(gameTicks%(1000/TICK_TIME)==0)Game.addScore(5);
				if(player.getLives()<=0) {
					stop();
				}
				else if(Game.getScore()>=5000) {
					stop();
				}
			}		
		}
	}
	
	public static int getScore() {
		return gameScore;
	}
	
	public static void addScore(int points) {
		gameScore += points;
	}
	
	private Asteroid addAsteroid() {
		Component[] comps = getContentPane().getComponents();
		int asteroids = 0;
		for(int i=0;i<comps.length;i++) {
			if(comps[i] instanceof Asteroid)asteroids++;
		}
		int maxAsteroids = 3+(gameTicks*TICK_TIME)/(SPAWN_GAP*1000);
		if (asteroids<maxAsteroids) return new Asteroid();
		//this means that the max starts at 3, and increases by 1 every SPAWN_GAP seconds
		return null;
	}
	
}
