import javax.swing.*;
import java.awt.event.*;

public class Input {

	static double speed = 64.0f;
	static double zoomSpeed = 0.2f;
	static boolean pause = true; //whether or not game is paused
	
	static boolean w = false;
	static boolean a = false;
	static boolean s = false;
	static boolean d = false;
	static boolean m = false;
	static boolean ctrl = false;
	static boolean shift = false;
	
	static int targetedX;
	static int targetedY;
	
	static boolean LEFT_CLICK = false;
	static boolean RIGHT_CLICK = false;
	static boolean zoom = false;
	static int tileX;
	static int tileY;
	static long lastTime = System.nanoTime(); //used for fps independent movement
	static long lastFpsTime = System.nanoTime();
	
	static int frameCounter = 0;
	static int musicCounter = 0;
	static int menuCounter = 0;
	static int controlsCounter = 0;
	
	public static double[][] controlsCoords = new double[36][5];
	
	public Input(Game newGame, Camera camera) {
		
		Main.frame.addKeyListener(new KeyAdapter(){ //camera input
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if(key == KeyEvent.VK_SHIFT) {
					if(Main.camera.zoom < 1) {
						speed = 256.0f * 2.0f/Main.camera.zoom;
					}
					else {
						speed = 256.0f;
					}
				}
				if(key == KeyEvent.VK_ESCAPE) {
					menuCounter++;
					if(Inventory.drawInventory == true) {
						Inventory.invCounter++;
						Inventory.drawInventory = false;
					}
					
					controlsCounter = 0;
					Inventory.invCounter = 0;
					if(menuCounter % 2 == 1) {
						Main.displayMenu = false;	
					}
					else {
						Main.displayMenu = true;			
						w = false; //stops camera panning while paused
						a = false;
						s = false;
						d = false;
					}

				}
				if(key == KeyEvent.VK_W && pause == false) {
					w = true;
				}
				if(key == KeyEvent.VK_A && pause == false) {
					a = true;
				}
				if(key == KeyEvent.VK_S && pause == false) {
					s = true;
				}
				if(key == KeyEvent.VK_D && pause == false) {
					d = true;
				}
				if(key == KeyEvent.VK_Z && pause == false) {
					Main.camera.zoom = 1;
					Main.camera.x = 450 * Main.tileSize;
					Main.camera.y = 0 * Main.tileSize;
				}
				if(key == KeyEvent.VK_M && pause == false) {
					musicCounter++;
					if(musicCounter % 2 == 1) {
						Main.music.stop();	
					}
					else {
						Main.music = new Music();							
					}
				}
				if(key == KeyEvent.VK_CONTROL && pause == false) {
					zoom = true;
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) { //camera input release
				int key = e.getKeyCode();
				
				if(key == KeyEvent.VK_SHIFT && pause == false) {
					speed = 64.0f;
				}
				if(key == KeyEvent.VK_W && pause == false) {
					w = false;
				}
				if(key == KeyEvent.VK_A && pause == false) {
					a = false;
				}
				if(key == KeyEvent.VK_S && pause == false) {
					s = false;
				}
				if(key == KeyEvent.VK_D && pause == false) {
					d = false;
				}
				if(key == KeyEvent.VK_CONTROL && pause == false) {
					zoom = false;
				}
			}
		});
		
		Main.frame.addMouseWheelListener(new MouseWheelListener() { //camera zoom
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int notches = e.getWheelRotation();
				if(zoom && notches < 0 && pause == false) {
					Main.camera.zoom = Math.min(10, Main.camera.zoom + zoomSpeed); //zoom in
				}	
				else if(zoom && notches > 0 && pause == false) {
					Main.camera.zoom = Math.max(0.49f, Main.camera.zoom - zoomSpeed); //zoom out
				}
			}
		});
		Main.frame.addMouseMotionListener(new MouseMotionListener(){
					
					@Override
					public void mouseDragged(MouseEvent e) {	
						if(LEFT_CLICK == true && pause == false) {
							replaceBlockAt(e.getX(), e.getY(), null);					
						}
						else if(RIGHT_CLICK && pause == false) {
							replaceBlockAt(e.getX(), e.getY(), Inventory.blockName);		
						}
		
					}
		
					@Override
					public void mouseMoved(MouseEvent e) {

					}		
				});
		
				
		Main.frame.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getX() * Main.windowScale >= Game.playX && e.getX() * Main.windowScale <= Game.playWidth
						&& e.getY() * Main.windowScale >= Game.playY && e.getY() * Main.windowScale <= Game.playHeight
						&& controlsCounter % 2 == 0 && Inventory.drawInventory == false && pause == true) {
						Main.displayMenu = false;
							menuCounter++;
				}
				if(e.getX() * Main.windowScale >= Game.controlsX && e.getX() * Main.windowScale <= Game.controlsWidth
						&& e.getY() * Main.windowScale >= Game.controlsY && e.getY() * Main.windowScale <= Game.controlsHeight
						&& controlsCounter % 2 == 0 && Inventory.drawInventory == false && pause == true) {
						controlsCounter++;
				}
				if(LEFT_CLICK ^ RIGHT_CLICK && pause == false) {
					return;
				}
				else if(e.getButton() == MouseEvent.BUTTON1 && pause == false) {
					if(pause == false) {
						replaceBlockAt(e.getX(), e.getY(), null);						
					}
						LEFT_CLICK = true;
				}
				else if(e.getButton() == MouseEvent.BUTTON3 && pause == false) {
					if(pause == false) {
						replaceBlockAt(e.getX(), e.getY(), Inventory.blockName);							
					}
					RIGHT_CLICK = true;
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
					if(LEFT_CLICK == true) {
						LEFT_CLICK = false;					
					}
					if(RIGHT_CLICK == true) {
						RIGHT_CLICK = false;					
					}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				return;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				return;
			}
			
		});
		
		Timer gravityTimer = new Timer(50, new ActionListener() { //gravity blocks
			@Override		
	        public void actionPerformed(ActionEvent e) {
				for(int i = 0; i <= 499; i++) {
					for(int j = 0; j <= 999; j++) {
						if(pause == true) {
							continue;
						}
						if(Game.tileMap[i][j] != null && Game.tileMap[i][j].equals("Sand")) { //sand falling
							if(i < 499 && (Game.tileMap[i+1][j] == null || Game.tileMap[i+1][j].equals("Water"))) {
								Main.newGame.replaceBlock(j, i, "Sand"); //refers to the sand block, this is an exception
							}
						}
						else if(Game.tileMap[i][j] != null && Game.tileMap[i][j].equals("Water")) { //water physics							
							if(j > 0 && Game.tileMap[i][j-1] == null) {		
								Main.newGame.replaceBlock(j-1, i, "Water");
								continue;
							}
							if(j < 999 && Game.tileMap[i][j+1] == null) {
								Main.newGame.replaceBlock(j+1, i, "Water");
								j++; //this is to skip the current cycle so water flows cleanly to the right
								continue;
							}
							if(i < 499 && Game.tileMap[i+1][j] == null) {
								Main.newGame.replaceBlock(j, i+1, "Water");
								continue;
							}
						}
					}				
				}
			}
			
		});
		gravityTimer.start();
		        
		Timer timer = new Timer(8, new ActionListener() { //camera redrawing / movement / other
			
			@Override		
            public void actionPerformed(ActionEvent e) {
				
				if(Main.displayMenu == true || Input.controlsCounter % 2 == 1 || Inventory.drawInventory == true) {
					pause = true;
				} else {
					pause = false;
				}	
				
				long currentTime = System.nanoTime();
				double deltaTime = (currentTime - lastTime) / 1_000_000_000.0f;
				lastTime = currentTime;


				frameCounter++;
				if(currentTime - lastFpsTime >= 1_000_000_000.0f) {
					Main.fps = frameCounter;
					frameCounter = 0;
					lastFpsTime  = currentTime;
				}
				
				if(camera.x < -Main.worldWidth/2) camera.x = -Main.worldWidth/2;
				if(camera.y < -Main.worldHeight/2) camera.y = -Main.worldHeight/2;
				if(camera.y > (500 * Main.tileSize - Main.worldHeight/2)) camera.y = 500 * Main.tileSize - Main.worldHeight/2;
				if(camera.x > (1000 * Main.tileSize - Main.worldWidth/2)) camera.x = 1000 * Main.tileSize - Main.worldWidth/2;

				if (w) Main.camera.y -= speed * deltaTime;

                if (a) Main.camera.x -= speed * deltaTime;

                if (s) Main.camera.y += speed * deltaTime;

                if (d) Main.camera.x += speed * deltaTime;

                Main.newGame.repaint(0, 0, Main.worldWidth, Main.worldHeight);
			}
		
		});
		timer.start();
	}
	
	private void replaceBlockAt(int mouseX, int mouseY, String blockName) {
		
        double width = Main.worldWidth;
        double height = Main.worldHeight;

        double worldX = (width / 2) + ((mouseX - width / 2.0) / Main.camera.zoom) + Main.camera.x;
        double worldY = (height / 2) + ((mouseY - height / 2.0) / Main.camera.zoom) + Main.camera.y;

        int tileX = (int) worldX / Main.tileSize;
        int tileY = (int) worldY / Main.tileSize;

        if (tileX >= 0 && tileX < 1000 && tileY >= 0 && tileY < 500) {
            Game.tileMap[tileY][tileX] = blockName;
            Main.newGame.replaceBlock(tileX, tileY, blockName);

        }
    }
}
