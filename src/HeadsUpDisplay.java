/**
 * @author Matthew Schueler
 * @version 5.0
 */

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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
			life = ImageIO.read(new File("spaceshiptiny.jpeg"));
		    lifeImgX = life.getWidth();
		    lifeImgY = life.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void paint (Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		for (int i=0;i<playerShip.getLives();i++) {
			g2d.drawImage(life, 10+20*i, 20, lifeImgX, lifeImgY, null);
		}
	}
	
}
