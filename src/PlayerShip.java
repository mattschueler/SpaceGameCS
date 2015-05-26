/**
 * @author Matthew Schueler
 * @version 5.0
 */

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.swing.*;

public class PlayerShip extends GameComponent {

	private static final long serialVersionUID = 1L;
	BufferedImage img;
	public int imgX, imgY;
	private double xVel,yVel,rVel;
	private int xPos,yPos;
	private double rot;
	private double accel;
	
	private int lives;
	
	public ConcurrentHashMap<String,Boolean> keyBinds;
	
	final double MAX_VEL = 75;
	final double MIN_VEL = 0.2;
	
	public PlayerShip() {
		xPos = 300;
		yPos = 300;
		accel = 0.25;
		lives = 3;
		try {
			img = ImageIO.read(new File("spaceshipsmall.jpeg"));
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
			//if (s.equals("SPACE")) {
				System.out.println(s);
				inMap.put(KeyStroke.getKeyStroke("typed " + s), "type " + s);
				acMap.put("typed " + s, new TypeAction(s));
			//}
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
	
	class TypeAction extends AbstractAction {
		//used for the fire button
		private static final long serialVersionUID = 1L;
		private String key;
		public TypeAction(String newKey) {
			System.out.println("init");
			key=newKey;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(e);
			keyBinds.replace(key,false);
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
			
	public double[] getPosition() {
		double[] shipCoordinates = {xPos,yPos};
		return shipCoordinates;
	}
	
	public double[] getVelocity() {
		double[] shipSpeeds = {xVel,yVel};
		return shipSpeeds;
	}
	
	public double getRotation() {
		return rot;
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
			getParent().add(new Bullet(xPos+imgX/2, yPos+imgY/2, rot, xVel, yVel));
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
		isOffscreen(Game.xSize, Game.ySize);
	}
	
	public void isOffscreen(int screenX, int screenY) {
		//check if off edge of any side of screen and move to wrap around to other side
		if ((xPos+imgX/2)<=0)xPos+=screenX;
		else if ((xPos-imgX/2)>=screenX)xPos=0;
		if ((yPos+imgY/2)<=0)yPos+=screenY;
		else if ((yPos-imgY/2)>=screenY)yPos=0;
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		//rotate the image based on theta of ship, and translate to center position of image
		AffineTransform at = AffineTransform.getRotateInstance(rot+(Math.PI/2), xPos+img.getWidth()/2, yPos+img.getHeight()/2);
		at.translate(xPos,yPos);
		g2d.drawImage(img, at, null);
	}
	
	public String toString() {
		return String.format("PlayerShip X:%f Y:%f THETA:%f VX:%f VY:%f", xPos, yPos, rot, xVel, yVel);
	}

}
