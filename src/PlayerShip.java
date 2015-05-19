/**
 * @author Matthew Schueler
 * @version 4.0
 */

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.swing.*;

public class PlayerShip extends JComponent {

	BufferedImage img;
	public int imgX, imgY;
	private double xVel,yVel,rVel;
	private double xPos,yPos,rot;
	private double accel;
	
	public ConcurrentHashMap<String,Boolean> keyBinds;
	
	public final double MAX_VEL = 100;
	
	public PlayerShip() {
		xPos=300;
		yPos=300;
		accel = 0.25;
		try {
			img = ImageIO.read(new File("spaceshipsmall.jpeg"));
		    imgX = img.getWidth();
		    imgY = img.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		keyBinds = new ConcurrentHashMap<String,Boolean>() {{
			put("W", false);
			put("S", false);
			put("A", false);
			put("D", false);			
		}};
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
		private String key;
		public PressAction(String newKey) {
			key=newKey;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			keyBinds.replace(key,true);
			System.out.println("" + e + true);
		}
	}
	
	class ReleaseAction extends AbstractAction {
		private String key;
		public ReleaseAction(String newKey) {
			key=newKey;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			keyBinds.replace(key,false);
			System.out.println("" + e + false);
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
				
	public void moveTick() {
		//make any adjustments to velocity vectors
		//thrusting/braking
		if(keyBinds.get("W")) {
			//thrust
			xVel+=(accel*Math.cos(rot));
			yVel+=(accel*Math.sin(rot));
		} else if (keyBinds.get("S")) {
			//brake
			if(xVel!=0)xVel-=Math.copySign(1.25*accel, xVel)*Math.cos(Math.atan(Math.abs(yVel/xVel)));
			if(yVel!=0)yVel-=Math.copySign(1.25*accel, yVel)*Math.sin(Math.atan(Math.abs(yVel/xVel)));
		} else {
			//for any cases where not thrusting/braking at all
		}
		//turning
		if(keyBinds.get("A")) {
			//rotate left
			rVel=-0.1;
		} else if (keyBinds.get("D")) {
			//rotate right
			rVel=0.1;
		} else {
			//no rotation
			rVel=0;
		}
		//finally make adjustments to position and angle
		xPos+=xVel;
		yPos+=yVel;
		rot+=rVel;
		if(Math.abs(xVel)<=0.2)xVel=0;
		if(Math.abs(yVel)<=0.2)yVel=0;
		double vTheta = Math.atan(yVel/xVel);
		double maxVX = -1 * MAX_VEL * Math.cos(vTheta);
		double maxVY = -1 * MAX_VEL * Math.sin(vTheta);
		//System.out.println("MAXX: " + maxVX + " MAXY: " + maxVY);
		if(Math.abs(xVel)>Math.abs(maxVX))xVel=Math.copySign(maxVX, xVel);
		if(Math.abs(yVel)>Math.abs(maxVY))yVel=Math.copySign(maxVY, yVel);
		//System.out.println("VX: " + xVel + " VY: " + yVel + " THETA: " + rot);
	}
	
	public void isOffscreen(int screenX, int screenY) {
		if ((xPos+imgX/2)<=0)xPos+=screenX;
		else if ((xPos-imgX/2)>=screenX)xPos=0;
		if ((yPos+imgY/2)<=0)yPos+=screenY;
		else if ((yPos-imgY/2)>=screenY)yPos=0;
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform at = AffineTransform.getRotateInstance(rot+(Math.PI/2), xPos+img.getWidth()/2, yPos+img.getHeight()/2);
		at.translate(xPos,yPos);
		g2d.drawImage(img, at, null);
	}
	
	public String toString() {
		return String.format("PlayerShip X:%f Y:%f THETA:%f VX:%f VY:%f", xPos, yPos, rot, xVel, yVel);
	}

}
