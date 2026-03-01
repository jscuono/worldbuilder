import javax.swing.*;
import java.awt.*;

public class Main {
		
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	static final int tileSize = 16; //tileSize
	static final int worldWidth = screenSize.width;
	static final int worldHeight = screenSize.height;
	
	static boolean draw = false; //switches to true when the world needs to be redrawn
	static boolean displayMenu = true;
	
	static Camera camera;
	static JFrame frame;
	static Game newGame;
	static Input input;
	static Inventory inventory;
	static Music music;
	static Menus menus;
	
    static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    static GraphicsDevice gd = ge.getDefaultScreenDevice();
    static GraphicsConfiguration gc = gd.getDefaultConfiguration();
    static double windowScale = gc.getDefaultTransform().getScaleX(); //scale used by windows
    static int fps = 0;
    
	public static void main(String[] args){
		SwingUtilities.invokeLater(() -> {
			frame = new JFrame("Worldbuilder"); //game window
			camera = new Camera(450 * tileSize, 0, 1.0f); //camera starts at world center
			newGame = new Game(camera); //game instantiation
			input = new Input(newGame, camera); //input handler
			inventory = new Inventory(); //inventory 
			menus = new Menus(); //file i/o
			music = new Music(); //change this if music should be played upon loading or not
	
			frame.setSize(worldWidth, worldHeight);
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setFocusable(true);
	        frame.requestFocusInWindow();
	        frame.setUndecorated(true);
	        //probably wont use this:  gd.setFullScreenWindow(frame);
	        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
			frame.setVisible(true);
			frame.add(newGame);	
		});
		
	
	}
	
	
}
