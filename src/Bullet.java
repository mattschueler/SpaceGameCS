/**
 * @author Matthew Schueler
 * @version 6.0
 */

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bullet extends GameComponent {
	private static final long serialVersionUID = 1L;
	private int lifeTime;
		
	public Bullet(int px, int py, double th, double vx, double vy) {
		xPos = px;
		yPos = py;
		rot = th;
		xVel = vx + 20 * Math.cos(rot);
		yVel = vy + 20 * Math.sin(rot);
		lifeTime = 500; //in msecs
		try {
			img = ImageIO.read(new File("bullet.png"));
	    	imgX = img.getWidth();
	    	imgY = img.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void moveTick() {
		lifeTime-=Game.TICK_TIME;
		if (lifeTime<=0) {
			getParent().remove(this);			
		}
		xPos+=xVel;
		yPos+=yVel;
		isOffscreen(Game.X_SIZE,Game.Y_SIZE);
	}
	
	/*public void isOffscreen(int screenX, int screenY) {
		//check if off edge of any side of screen and move to wrap around to other side
		if ((xPos+5)<=0)xPos+=screenX;
		else if ((xPos-5)>=screenX)xPos=0;
		if ((yPos+5)<=0)yPos+=screenY;
		else if ((yPos-5)>=screenY)yPos=0;
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		setBackground(Color.YELLOW);
		setOpaque(true);
		g2d.fillOval(xPos-RADIUS, yPos-RADIUS, 2*RADIUS, 2*RADIUS);
	}*/
	
}
