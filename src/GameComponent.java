/**
 * @author Matthew Schueler
 * @version 6.0
 */

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class GameComponent extends JComponent {

	private static final long serialVersionUID = 1L;
	BufferedImage img;
	public int imgX, imgY;
	protected double xVel,yVel;
	protected int xPos, yPos;
	protected double rot;
	
	public void moveTick() {
		//this class leaves the movement to be defined by its children
	}
	
	public void isOffscreen(int screenX, int screenY) {
		//check if off edge of any side of screen and move to wrap around to other side
		if ((xPos+imgX/2)<=0)xPos+=screenX;
		else if ((xPos+imgX/2)>=screenX)xPos=0;
		if ((yPos+imgY/2)<=0)yPos+=screenY;
		else if ((yPos+imgY/2)>=screenY)yPos=0;
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform at = AffineTransform.getRotateInstance(rot, xPos+img.getWidth()/2, yPos+img.getHeight()/2);
		at.translate(xPos,yPos);
		g2d.drawImage(img, at, null);
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
	
}
