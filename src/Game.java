/**
 * @author Matthew Schueler
 * @version 6.0
 */

import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

public class Game extends JApplet implements Runnable {
	private static final long serialVersionUID = 1L;
	BufferedImage img = null;
	public Image offScreen;
	public Graphics2D g2d;
	public PlayerShip player;
	private Thread th;
	private int gameTicks = 0;
	private static int gameScore = 0;
	
	public final static int TICK_TIME = 25; //time in msecs of one frame
	public final static int X_SIZE = 854, Y_SIZE = 480;
	public final static int SPAWN_GAP = 10; //seconds between increasing the max num of asteroids
		
	public void init() {
		setSize(X_SIZE,Y_SIZE);
		offScreen = createImage(X_SIZE,Y_SIZE);
		g2d = (Graphics2D)(offScreen.getGraphics());
		player = new PlayerShip();
		th = new Thread(this);
		th.start();
	}
	
	/**
	 * Run method of this JApplet. Creates a GameWorker Thread, a subclass of SwingWorker, for handling 
	 * events on th Evet Dispatch Thread
	 */
	public void run() {
		(new GameWorker(this)).run();
	}
	
	/**
	 * Stops the current thread execution
	 */
	public void stop() {
		th = null;
	}
	
	/**
	 * Paints all the components of the Game that exist in the content pane
	 */
	public void paint(Graphics g) {
		super.paint(g);
		for (Component c : getContentPane().getComponents()) {
			c.paint(g);
		}
	}
	
	/**
	 * Calls the paint method every time update is called (each "tick")
	 */
	public void update(Graphics g) {
		paint(g);
	}
	
	/**
	 * Sets the heading of the JApplet window to show the score and lives for the current game
	 */
	public void setHeading(int score, int lives) {
		JFrame c = (JFrame)this.getParent().getParent().getParent().getParent();
		c.setTitle(String.format("Lives: %d     Score: %d", lives, score));
	}
	
	/**
	 * GameWorker is a subclass of SwingWorker that exists to run the game instance on the EDT
	 * @author Matthew Schueler	 *
	 */
	//class GameWorker extends SwingWorker<Object, Object> {
	class GameWorker extends Thread {
		Game currentGame;
		public GameWorker(Game game) {
			currentGame = game;
		}
		
		public void run() {
			getContentPane().add(player);
			getContentPane().revalidate();
			getContentPane().requestFocus();
			while(true) {
				for (Component c : getContentPane().getComponents()) {
					if (c instanceof GameComponent) {
						((GameComponent)c).moveTick();
						if (((GameComponent)c).dead) getContentPane().remove(c);
					}
										
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
				if(gameTicks%(2000/TICK_TIME)==0)Game.addScore(5);
				currentGame.setHeading(gameScore, player.getLives());
				if(player.dead || Game.getScore()>=5000) {
					currentGame.stop();
					return;
				}
			}		
		}
	}
	
	/**
	 * Gets the current score of the game
	 * @return the score for this game
	 */
	public static int getScore() {
		return gameScore;
	}
	
	/**
	 * Adds a certain number of points to the score
	 * @param points the number points to add to the score
	 */
	public static void addScore(int points) {
		gameScore += points;
	}
	
	/**
	 * Helper method for adding an asteroid to the game. Checks for how many asteroids there are an compares it to 
	 * the max value of asteroids (which increases by 1 every SPAWN_GAP seconds (which is regulated by the game 
	 * constants) and returns an Asteroid or null based on the conditions
	 * @return the Asteroid to be added, or null if no Asteroid should be added
	 */
	private Asteroid addAsteroid() {
		Component[] comps = getContentPane().getComponents();
		int asteroids = 0;
		for(int i=0;i<comps.length;i++) {
			if(comps[i] instanceof Asteroid)asteroids++;
		}
		int maxAsteroids = 3+(gameTicks*TICK_TIME)/(SPAWN_GAP*2000);
		if (asteroids<maxAsteroids) return new Asteroid(player);
		return null;
	}
	
}
