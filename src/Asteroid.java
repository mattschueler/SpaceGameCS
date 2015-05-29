/**
 * @author Matthew Schueler
 * @version 6.0
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Asteroid extends GameComponent {

	private static final long serialVersionUID = 1L;
	private int size;
	
	public Asteroid() {
		try {
			img = ImageIO.read(new File("asteroid.png"));
		    imgX = img.getWidth();
		    imgY = img.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void moveTick() {
		
		xPos+=xVel;
		yPos+=yVel;
		isOffscreen(Game.X_SIZE,Game.Y_SIZE);
	}
	
	public void isOffscreen(int screenX, int screenY) {
		//check if off edge of any side of screen and move to wrap around to other side
		if ((xPos+5)<=0)xPos+=screenX;
		else if ((xPos+5)>=screenX)xPos=0;
		if ((yPos+5)<=0)yPos+=screenY;
		else if ((yPos+5)>=screenY)yPos=0;
	}
		
}
