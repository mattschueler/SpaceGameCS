import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;


public class Bullet extends GameComponent {
	private static final long serialVersionUID = 1L;
	private double xVel,yVel;
	private int xPos,yPos;
	private double rot;
	
	private int lifeTime;
	
	public Bullet(int px, int py, double th, double vx, double vy) {
		xPos = px;
		yPos = py;
		rot = th;
		xVel = vx + 25 * Math.cos(rot);
		yVel = vy + 25 * Math.sin(rot);
		lifeTime = 1000; //in msecs
	}
	
	public void moveTick() {
		lifeTime-=Game.TICK_TIME;
		if (lifeTime<=0) {
			getParent().remove(this);
			
		}
		xPos+=xVel;
		yPos+=yVel;
		isOffscreen(Game.xSize,Game.ySize);
	}
	
	public void isOffscreen(int screenX, int screenY) {
		//check if off edge of any side of screen and move to wrap around to other side
		if ((xPos+5)<=0)xPos+=screenX;
		else if ((xPos-5)>=screenX)xPos=0;
		if ((yPos+5)<=0)yPos+=screenY;
		else if ((yPos-5)>=screenY)yPos=0;
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.fillOval(xPos-5, yPos-5, 10, 10);
	}
	
}
