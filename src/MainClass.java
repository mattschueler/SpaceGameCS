import javax.swing.*;

public class MainClass {

	public static void main(String[] args) {
		Game main = new Game();
		JFrame frame = new JFrame();
		frame.pack();
		frame.setSize(Game.X_SIZE,Game.Y_SIZE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.add(main);
	    frame.setVisible(true);
	    main.init();
	    main.run();
	}

}
