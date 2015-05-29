/**
 * @author Matthew Schueler
 * @version 6.0
 */

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Asteroid extends GameComponent {
	private static final long serialVersionUID = 1L;
	//private int size;
	
	public Asteroid() {
		try {
			img = ImageIO.read(new File("asteroid.png"));
		    imgX = img.getWidth();
		    imgY = img.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		xPos = (int)((Game.X_SIZE-200)*Math.random())+100;
		yPos = (int)((Game.Y_SIZE-200)*Math.random())+100;
		xVel = (4*Math.random())-2;
		yVel = (4*Math.random())-2;
		rVel = (0.01 * Math.random())+0.01;
		//size = (int)(3*Math.random())+1;
	}
	
	public void moveTick() {
		rot+=rVel;
		xPos+=xVel;
		yPos+=yVel;
		checkBulletCollisions();
		/*if(size<=0){
			getParent().remove(this);
		}*/
		isOffscreen(Game.X_SIZE,Game.Y_SIZE);
	}
	
	public void isOffscreen(int screenX, int screenY) {
		//check if off edge of any side of screen and move to wrap around to other side
		if ((xPos+5)<=0)xPos+=screenX;
		else if ((xPos+5)>=screenX)xPos=0;
		if ((yPos+5)<=0)yPos+=screenY;
		else if ((yPos+5)>=screenY)yPos=0;
	}
	
	public void checkBulletCollisions() {
		Component[] comps = getParent().getComponents();
		for(int i=0;i<comps.length;i++) {
			if(comps[i] instanceof Bullet) {
				if(distanceTo(this.getPosition(),((Bullet)comps[i]).getPosition())<50/*"*size)*/){
					//size--;
					Game.addScore(100);
					getParent().remove(comps[i]);
					getParent().remove(this);
					return;
				}
			}
		}
	}
	
	public double[] getPosition() {
		return new double[]{xPos+imgX/*"*size*/,yPos+imgY/*"*size*/};
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform at = AffineTransform.getRotateInstance(rot, xPos+img.getWidth()/2, yPos+img.getHeight()/2);
		//at.scale(size,size);
		at.translate(xPos,yPos);
		g2d.drawImage(img, at, null);
		//g2d.drawImage(img, xPos, yPos, null);
	}
		
}
