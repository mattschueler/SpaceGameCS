/**
 * @author Matthew Schueler
 * @version 6.0
 */

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.swing.*;

public class PlayerShip extends GameComponent {
	private static final long serialVersionUID = 1L;
	private double accel;
	private int lives;
	
	public ConcurrentHashMap<String,Boolean> keyBinds;
	
	final double MAX_VEL = 5;
	final double MIN_VEL = 0.1;
	
	/**
	 * Creates a new PlayerShip with a starting Position and sets the acceleration value of the ship.
	 * Also initializes the lives and gets the image for the ship. Creates the key bindings for playe input
	 * with ActionMap and InputMap for the directions and the fire button.
	 */
	public PlayerShip() {
		dead = false;
		xPos = 300;
		yPos = 300;
		accel = 0.125;
		lives = 3;
		try {
			img = ImageIO.read(new File("src/spaceshipsmall.png"));
		    imgX = img.getWidth();
		    imgY = img.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		keyBinds = new ConcurrentHashMap<String,Boolean>() {
			private static final long serialVersionUID = 1L;
			{
				put("W", false);
				put("S", false);
				put("A", false);
				put("D", false);
				put("SPACE", false);
			}
		};
		for(String s : keyBinds.keySet()) {
			InputMap inMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
			ActionMap acMap = getActionMap();
			inMap.put(KeyStroke.getKeyStroke("pressed " + s), "press " + s);
			acMap.put("press " + s, new PressAction(s));
			inMap.put(KeyStroke.getKeyStroke("released " + s), "release " + s);
			acMap.put("release " + s, new ReleaseAction(s));
		}
	}
	
	/**
	 * PressAction class to handle a key press event and modify the keys ArrayList
	 * @author Matthew Schueler
	 *
	 */
	class PressAction extends AbstractAction {
		//general key press action class
		private static final long serialVersionUID = 1L;
		private String key;
		public PressAction(String newKey) {
			key=newKey;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			keyBinds.replace(key,true);
		}
	}
	
	/**
	 * ReleaseAction class to handle a key release event and modify the keys ArrayList
	 * @author Matthew Schueler
	 *
	 */
	class ReleaseAction extends AbstractAction {
		//general key release action class
		private static final long serialVersionUID = 1L;
		private String key;
		public ReleaseAction(String newKey) {
			key=newKey;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			keyBinds.replace(key,false);
		}
	}
	
	/**
	 * Gets the number of lives that the player has left
	 * @return the current lives left of the PlayerShip
	 */
	public int getLives() {
		return lives;
	}
	
	/**
	 * moveTick method for the PlayerShip. First checks for thrust/braking key binings and adjusts velocity components 
	 * accordingly. Then checks for rotation key bindings and adjusts rotational velocity accordingly. The checks to 
	 * see if the ship should fire and makes sure that there are no Bullets left to ensure only one is fired at a time. 
	 * Then modifies the position and rotation of the ship. Then, makes sure that if the ship's velocity is below a 
	 * threshold it is set to zero, otherwise the ship would jitter around as it's velocity would never become zero. 
	 * Then using the sin/cos of the angle of the velocity vector, makes sure that each component of the velocity 
	 * is no more than MAX_VEL times the sin/cos of the angle of the velocity vector so that the net Velocity is never 
	 * greater than MAX_VEL. Finally, it checks if the PlayerShip is offscreen, has collided with Asteroids, and has 
	 * run out of lives.
	 */
	public void moveTick() {
		//Thrust/braking
		if(keyBinds.get("W")) {
			xVel+=accel*Math.cos(rot);
			yVel+=accel*Math.sin(rot);
		} else if (keyBinds.get("S")) {
			if(xVel!=0)xVel-=Math.copySign(1.25*accel, xVel)*Math.cos(Math.atan(Math.abs(yVel/xVel)));
			if(yVel!=0)yVel-=Math.copySign(1.25*accel, yVel)*Math.sin(Math.atan(Math.abs(yVel/xVel)));
		} else {}
		//Turning
		if (keyBinds.get("A")) rVel=-0.1; //left
		else if (keyBinds.get("D")) rVel=0.1; //right
		else rVel=0;
		//Firing
		if (keyBinds.get("SPACE")) {
			Component[] comps = getParent().getComponents();
			int fired = 0;
			for(int i=0;i<comps.length;i++) {
				if(comps[i] instanceof Bullet)fired++;
			}
			if(fired<1)getParent().add(new Bullet(getPosition(), getVelocity(), rot));
		}
		//Position/rotation modify
		xPos+=xVel;
		yPos+=yVel;
		rot+=rVel;
		//Below a threshold, velocity is zero
		if(Math.abs(xVel)<=MIN_VEL && !keyBinds.get("W"))xVel=0;
		if(Math.abs(yVel)<=MIN_VEL && !keyBinds.get("W"))yVel=0;
		//Make sure not going too fast
		double vTheta = Math.atan(yVel/xVel);
		double maxVX = -1 * MAX_VEL * Math.cos(vTheta);
		double maxVY = -1 * MAX_VEL * Math.sin(vTheta);
		if(Math.abs(xVel)>Math.abs(maxVX))xVel=Math.copySign(maxVX, xVel);
		if(Math.abs(yVel)>Math.abs(maxVY))yVel=Math.copySign(maxVY, yVel);
		
		isOffscreen(Game.X_SIZE, Game.Y_SIZE);
		checkAsteroidCollisions();
		if(getLives()<=0)dead=true;
	}
	
	/**
	 * Checks if the PlayerShip has come too close to any Asteroids and decrements the lives if this is true
	 */
	public void checkAsteroidCollisions() {
		Component[] comps = getParent().getComponents();
		for(int i=0;i<comps.length;i++) {
			if(comps[i] instanceof Asteroid) {
				if(distanceTo(this.getPosition(),((Asteroid)comps[i]).getPosition())<25){
					lives--;
					((Asteroid)comps[i]).dead = true;
					return;
				}
			}
		}
	}
			
	/**
	 * Returns info about the PlayerShip for debugging
	 * @return the object data as a string
	 */
	public String toString() {
		return String.format("PlayerShip X:%f Y:%f THETA:%f VX:%f VY:%f", xPos, yPos, rot, xVel, yVel);
	}

}
