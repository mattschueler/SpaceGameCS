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
		dead = false;
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
			dead = true;
		}
		xPos+=xVel;
		yPos+=yVel;
		isOffscreen(Game.X_SIZE,Game.Y_SIZE);
	}
		
}
