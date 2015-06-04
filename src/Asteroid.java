/**
 * @author Matthew Schueler
 * @version 6.0
 */

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Asteroid extends GameComponent {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Generates a new asteroid with an image from a file and a random position, location, 
	 * and rotational velocity
	 */
	public Asteroid(PlayerShip player) {
		try {
			img = ImageIO.read(new File("asteroid.png"));
		    imgX = img.getWidth();
		    imgY = img.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		dead = false;
		xPos = (int)((Game.X_SIZE-100)*Math.random())+50;
		yPos = (int)((Game.Y_SIZE-100)*Math.random())+50;
		while(distanceTo(player.getPosition(), this.getPosition())<25) {
			xPos = (int)((Game.X_SIZE-100)*Math.random())+50;
			yPos = (int)((Game.Y_SIZE-100)*Math.random())+50;
		}
		xVel = (4*Math.random())-2;
		yVel = (4*Math.random())-2;
		rVel = (0.01 * Math.random())+0.01;
	}
	
	/**
	 * moveTick method for this GameComponent, moves the asteroid across the screen based on its
	 * velocities and also checks for Bullet collisions and offScreen coordinates
	 */
	public void moveTick() {
		rot+=rVel;
		xPos+=xVel;
		yPos+=yVel;
		checkBulletCollisions();
		isOffscreen(Game.X_SIZE,Game.Y_SIZE);
	}
	
	/**
	 * Checks if there are any Bullets less than 25 pixels away from the Asteroid, if there is,
	 * that means that there has been a "collision: between the two objects, and this method adjusts
	 * the "dead" boolean variables of the Bullet that is close to it and the Asteroid itself	
	 */
	public void checkBulletCollisions() {
		Component[] comps = getParent().getComponents();
		for(int i=0;i<comps.length;i++) {
			if(comps[i] instanceof Bullet) {
				if(distanceTo(this.getPosition(),((Bullet)comps[i]).getPosition())<25){
					Game.addScore(100);
					((Bullet)comps[i]).dead = true;
					dead = true;
					return;
				}
			}
		}
	}
}
