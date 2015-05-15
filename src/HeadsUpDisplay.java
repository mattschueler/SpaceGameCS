/**
 * @author Matthew Schueler
 * @version 4.0
 */

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class HeadsUpDisplay extends JComponent {
	
	private int xSize, ySize;
	private PlayerShip playerShip;
	
	public HeadsUpDisplay(int xPixels, int yPixels, PlayerShip player) {
		xSize = xPixels;
		ySize = yPixels;
		playerShip = player;
	}
	
	public void paint (Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawString(String.format("Xpos: %5.2f", playerShip.getPosition()[0]), 10, 10);
		g2d.drawString(String.format("Ypos: %5.2f", playerShip.getPosition()[1]), 90, 10);
		g2d.drawString(String.format("Xvel: %5.2f", playerShip.getVelocity()[0]), 10, 30);
		g2d.drawString(String.format("Yvel: %5.2f", playerShip.getVelocity()[1]), 90, 30);
		g2d.drawString(String.format("Angle(rad): %5.2f", playerShip.getRotation()), 10, 50);
	}
	
}
