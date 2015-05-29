/**
 * @author Matthew Schueler
 * @version 6.0
 */

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class HeadsUpDisplay extends JComponent {
	private static final long serialVersionUID = 1L;
	private PlayerShip playerShip;
	private BufferedImage life;
	public int lifeImgX, lifeImgY;
	
	public HeadsUpDisplay(PlayerShip player) {
		playerShip = player;
		try {
			life = ImageIO.read(new File("spaceshiptiny.png"));
		    lifeImgX = life.getWidth();
		    lifeImgY = life.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void paint (Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		/*for (int i=0;i<playerShip.getLives();i++) {
			g2d.drawImage(life, 10+20*i, 20, lifeImgX, lifeImgY, null);
		}*/
		g2d.drawString("Lives: " + playerShip.getLives(), 10, 20);
		g2d.drawString("Score: " + Game.getScore(), 10, 40);
	}
	
	public void showEnd (int status, Graphics2D g2d) {
		if (status==-1) {
			g2d.drawString("RATS!!! YOU LOST!!", 100, 100);
		} else if (status==-2) {
			g2d.drawString("CONGRATS!!! YOU WIN!!!", 100, 100);
		}
	}
	
}
