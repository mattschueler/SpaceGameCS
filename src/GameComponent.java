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
	protected double xVel, yVel, rVel;
	protected int xPos, yPos;
	protected double rot;
	protected boolean dead;
	
	/**
	 * Allows for each child to define its own movement method, this method is empty and 
	 * does nothing
	 */
	public void moveTick() {}
	
	/**
	 * Checks to see if the component is currently off the side of the screen and 
	 * adjusts the coordinates as necessary to wrap the image around
	 * @param screenX the width of the window
	 * @param screenY the height of the window
	 */
	public void isOffscreen(int screenX, int screenY) {
		if (getPosition()[0]<=0)xPos+=screenX;
		else if (getPosition()[0]>=screenX)xPos=0;
		if (getPosition()[1]<=0)yPos+=screenY;
		else if (getPosition()[1]>=screenY)yPos=0;
	}
	
	/**
	 * Overrides JComponent.paint() to render the image with a rotation
	 */
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform at = AffineTransform.getRotateInstance(rot, xPos+img.getWidth()/2, yPos+img.getHeight()/2);
		at.translate(xPos,yPos);
		g2d.drawImage(img, at, null);
	}

	/**
	 * Gives the x and y coordinates of the object
	 * @return the position of the center of the image of the object as a "vector"
	 */
	public int[] getPosition() {
		return new int[]{xPos+imgX/2,yPos+imgY/2};
	}
	
	/**
	 * Gets the x and y velocity of the object
	 * @return the velocity of the object as a "vector"
	 */
	public double[] getVelocity() {
		return new double[]{xVel,yVel};
	}
	
	/**
	 * Gets the rotation of the object in radians
	 * @return the rotation of the object
	 */
	public double getRotation() {
		return rot;
	}
	
	/**
	 * Gets the rotational velocity of the object
	 * @return the rotational velocity of the object
	 */
	public double getRotVelocity() {
		return rVel;
	}

	/**
	 * Finds the distance between two sets of coordinates
	 * @param aCoords the coordinates of the first object
	 * @param bCoords the coordinates of the second object
	 * @return the distance between the two objects
	 */
	public double distanceTo(int[] aCoords, int[] bCoords) {
		return Math.sqrt(Math.pow(aCoords[0]-bCoords[0], 2)+Math.pow(aCoords[1]-bCoords[1], 2));
	}
	
}
