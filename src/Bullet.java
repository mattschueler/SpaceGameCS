/**
 * @author Matthew Schueler
 * @version 6.0
 */

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bullet extends GameComponent {
	private static final long serialVersionUID = 1L;
	private int lifeTime;
	
	/**
	 * Creates a new Bullet with an image from a file with a given position, velocity, and rotation
	 * @param pos the position vector of the PlayerShip that fired the bullet
	 * @param vel the velocity vector of the PlayerShip that fired the bullet
	 * @param th the rotation of the PlayerShip that fired the bullet
	 */
	public Bullet(int pos[], double vel[], double th) {
		dead = false;
		xPos = pos[0];
		yPos = pos[1];
		rot = th;
		xVel = vel[0] + 20 * Math.cos(rot);
		yVel = vel[1] + 20 * Math.sin(rot);
		lifeTime = 500; //in msecs
		try {
			img = ImageIO.read(new File("bullet.png"));
	    	imgX = img.getWidth();
	    	imgY = img.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * moveTick method of the Bullet class, decreases lifeTime left by a Game.TICK_TIME, checks if the Bullet is 
	 * "dead", changes the position and checks if the Bullet has gone offscreen
	 */
	public void moveTick() {
		lifeTime-=Game.TICK_TIME;
		if (lifeTime<=0) {
			dead = true;
		}
		xPos+=xVel;
		yPos+=yVel;
		isOffscreen(Game.X_SIZE,Game.Y_SIZE);
	}
		
}
