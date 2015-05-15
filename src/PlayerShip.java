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

import javax.imageio.ImageIO;
import javax.swing.*;

public class PlayerShip extends JComponent {

	BufferedImage img;
	public int imgX, imgY;
	private double xVel,yVel,rVel;
	private double xPos,yPos,rot;
	private double accel;
	public final double MAX_VEL = 100;
	
	public PlayerShip() {
		xPos=300;
		yPos=300;
		accel = 0.25;
		try {
			img = ImageIO.read(new File("spaceshipsmall.jpg"));
		    imgX = img.getWidth();
		    imgY = img.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
		}
		getInputMap().put(KeyStroke.getKeyStroke("pressed W"), "thrust");
		getActionMap().put("thrust", new Thrust());
		getInputMap().put(KeyStroke.getKeyStroke("pressed S"), "brake");
		getActionMap().put("brake", new Brake());
		getInputMap().put(KeyStroke.getKeyStroke("pressed A"), "rotate");
		getActionMap().put("rotate", new Rotate());
		getInputMap().put(KeyStroke.getKeyStroke("pressed D"), "rotate");
		getActionMap().put("rotate", new Rotate());
		getInputMap().put(KeyStroke.getKeyStroke("released A"), "stoprotate");
		getActionMap().put("stoprotate", new StopRotate());
		getInputMap().put(KeyStroke.getKeyStroke("released D"), "stoprotate");
		getActionMap().put("stoprotate", new StopRotate());
	}
	
	public void addActions(String keyStroke, AbstractAction newAction) {
		InputMap inMap = getInputMap();
		ActionMap acMap = getActionMap();
		getInputMap().put(KeyStroke.getKeyStroke("pressed W"), "thrust");
		getActionMap().put("thrust", new Thrust());

		//http://stackoverflow.com/questions/16328946/java-keylistener-stutters
	}
	
	//class 
	
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
	
	class Thrust extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			xVel+=(accel*Math.cos(rot));
			yVel+=(accel*Math.sin(rot));
		}		
	}
	
	class Brake extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(xVel!=0)xVel-=Math.copySign(1.25*accel, xVel)*Math.cos(Math.atan(Math.abs(yVel/xVel)));
			if(yVel!=0)yVel-=Math.copySign(1.25*accel, yVel)*Math.sin(Math.atan(Math.abs(yVel/xVel)));
		}
	}
	
	class Rotate extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("a")) {
				rVel=-0.1;
			} else if(e.getActionCommand().equals("d")){
				rVel=0.1;
			}
		}
	}
	
	class StopRotate extends AbstractAction{
		@Override
		public void actionPerformed(ActionEvent e) {
			rVel=0;
		}
	}
		
	public void moveTick() {
		xPos+=xVel;
		yPos+=yVel;
		rot+=rVel;
		if(Math.abs(xVel)<=0.2)xVel=0;
		if(Math.abs(yVel)<=0.2)yVel=0;
		double maxVX = -1 * MAX_VEL * Math.cos(rot);
		double maxVY = -1 * MAX_VEL * Math.sin(rot);
		System.out.println("MAXX: " + maxVX + " MAXY: " + maxVY);
		if(Math.abs(xVel)>Math.abs(maxVX))xVel=Math.copySign(maxVX, xVel);
		if(Math.abs(yVel)>Math.abs(maxVY))yVel=Math.copySign(maxVY, yVel);
		System.out.println("VX: " + xVel + " VY: " + yVel + " THETA: " + rot);
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
