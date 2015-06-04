/**
 * @author Matthew Schueler
 * @version 6.0
 */

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Asteroid extends GameComponent {
	private static final long serialVersionUID = 1L;
	
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
	}
	
	public void moveTick() {
		rot+=rVel;
		xPos+=xVel;
		yPos+=yVel;
		checkBulletCollisions();
		isOffscreen(Game.X_SIZE,Game.Y_SIZE);
	}
		
	public void checkBulletCollisions() {
		Component[] comps = getParent().getComponents();
		for(int i=0;i<comps.length;i++) {
			if(comps[i] instanceof Bullet) {
				if(distanceTo(this.getPosition(),((Bullet)comps[i]).getPosition())<15){
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
}
