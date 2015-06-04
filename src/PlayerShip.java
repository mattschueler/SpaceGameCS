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
	
	final double MAX_VEL = 10;
	final double MIN_VEL = 0.2;
	
	public PlayerShip() {
		dead = false;
		xPos = 300;
		yPos = 300;
		accel = 0.25;
		lives = 3;
		try {
			img = ImageIO.read(new File("spaceshipsmall.png"));
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
	
	public int getLives() {
		return lives;
	}
				
	public void moveTick() {
		//make any adjustments to velocity vectors
		//thrusting/braking
		if(keyBinds.get("W")) {
			//thrust
			xVel+=accel*Math.cos(rot);
			yVel+=accel*Math.sin(rot);
		} else if (keyBinds.get("S")) {
			//brake
			if(xVel!=0)xVel-=Math.copySign(1.25*accel, xVel)*Math.cos(Math.atan(Math.abs(yVel/xVel)));
			if(yVel!=0)yVel-=Math.copySign(1.25*accel, yVel)*Math.sin(Math.atan(Math.abs(yVel/xVel)));
		} else {
			//for any cases where not thrusting/braking at all
		}
		//turning
		if (keyBinds.get("A")) rVel=-0.1; //rotate left
		else if (keyBinds.get("D")) rVel=0.1; //rotate right
		else rVel=0; //no rotation
		//fire bullets
		if (keyBinds.get("SPACE")) {
			Component[] comps = getParent().getComponents();
			int fired = 0;
			for(int i=0;i<comps.length;i++) {
				if(comps[i] instanceof Bullet)fired++;
			}
			if(fired<1)getParent().add(new Bullet(xPos+imgX/2, yPos+imgY/2, rot, xVel, yVel));
		}
		//finally make adjustments to position and angle
		xPos+=xVel;
		yPos+=yVel;
		rot+=rVel;
		//If not trying to accelerate and speed less than minimum, set to 0
		if(Math.abs(xVel)<=MIN_VEL && !keyBinds.get("W"))xVel=0;
		if(Math.abs(yVel)<=MIN_VEL && !keyBinds.get("W"))yVel=0;
		//find what max speed is for each direction based off angle of velocity
		double vTheta = Math.atan(yVel/xVel);
		double maxVX = -1 * MAX_VEL * Math.cos(vTheta);
		double maxVY = -1 * MAX_VEL * Math.sin(vTheta);
		//Make sure that total velocity (looking at both components does not exceed max speed
		if(Math.abs(xVel)>Math.abs(maxVX))xVel=Math.copySign(maxVX, xVel);
		if(Math.abs(yVel)>Math.abs(maxVY))yVel=Math.copySign(maxVY, yVel);
		isOffscreen(Game.X_SIZE, Game.Y_SIZE);
	}
			
	public String toString() {
		return String.format("PlayerShip X:%f Y:%f THETA:%f VX:%f VY:%f", xPos, yPos, rot, xVel, yVel);
	}

}
