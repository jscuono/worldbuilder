import javax.swing.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Inventory extends JFrame{
	
	static String[] blockList = new String[256];
	public static int hotbarSelected = 1;
	public static int invCounter = 0;
	public static boolean drawInventory = false;
	
	protected static String blockName = null;
	private static int notches = hotbarSelected;
	
	public static boolean getSelection2 = false; //check to see if 1st block to swap has already been chosen
	public static int selection1 = -1; //1st block to swap
	public static int selection2 = -1; //2nd block to swap
	
	public static double[][] invCoords = new double[36][5]; //stores button x, y, width, height, and "i" (referring to blockList[i])
	
	public Inventory() {
		
		blockList[0] = "sky"; //hotbar default
		blockList[1] = "grass";
		blockList[2] = "dirt";
		blockList[3] = "stone";	
		blockList[4] = "sand";	
		blockList[5] = "water";
		blockList[6] = "tree_wood";
		blockList[7] = "leaves";
		blockList[8] = "cactus";
		blockList[9] = "wood";
		blockList[10] = "sky";
		blockList[11] = "sky";
		blockList[12] = "sky";
		blockList[13] = "sky";
		blockList[14] = "sky";
		blockList[15] = "sky";
		blockList[16] = "sky";
		blockList[17] = "sky";
		blockList[18] = "sky";
		blockList[19] = "sky";
		blockList[20] = "sky";
		blockList[21] = "sky";
		blockList[22] = "sky";
		blockList[23] = "sky";
		blockList[24] = "sky";
		blockList[25] = "sky";
		blockList[26] = "sky";
		blockList[27] = "sky";
		blockList[28] = "sky";
		blockList[29] = "sky";
		blockList[30] = "sky";
		blockList[31] = "sky";
		blockList[32] = "sky";
		blockList[33] = "sky";
		blockList[34] = "sky";
		blockList[35] = "sky";

		Main.frame.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(drawInventory == false) {
					if(Input.zoom == false) {

						notches = e.getWheelRotation();
						if(notches < 0) {
							hotbarSelected--;		
						}
						else if(notches > 0) {
							hotbarSelected++;		
						}
						
						if(hotbarSelected > 8) {
							hotbarSelected = 9;
						}
						else if(hotbarSelected < 2) {
							hotbarSelected = 1;
						}
						
						if(hotbarSelected == 1) {	
							blockName = blockList[hotbarSelected-1].substring(0,1).toUpperCase() + blockList[hotbarSelected-1].substring(1);
						}
						if(hotbarSelected == 2) {
							blockName = blockList[hotbarSelected-1].substring(0,1).toUpperCase() + blockList[hotbarSelected-1].substring(1);
						}
						if(hotbarSelected == 3) {
							blockName = blockList[hotbarSelected-1].substring(0,1).toUpperCase() + blockList[hotbarSelected-1].substring(1);
						}
						if(hotbarSelected == 4) {
							blockName = blockList[hotbarSelected-1].substring(0,1).toUpperCase() + blockList[hotbarSelected-1].substring(1);
						}
						if(hotbarSelected == 5) {
							blockName = blockList[hotbarSelected-1].substring(0,1).toUpperCase() + blockList[hotbarSelected-1].substring(1);
						}
						if(hotbarSelected == 6) {
							blockName = blockList[hotbarSelected-1].substring(0,1).toUpperCase() + blockList[hotbarSelected-1].substring(1);
						}
						if(hotbarSelected == 7) {
							blockName = blockList[hotbarSelected-1].substring(0,1).toUpperCase() + blockList[hotbarSelected-1].substring(1);
						}
						if(hotbarSelected == 8) {
							blockName = blockList[hotbarSelected-1].substring(0,1).toUpperCase() + blockList[hotbarSelected-1].substring(1);
						}
						if(hotbarSelected == 9) {
							blockName = blockList[hotbarSelected-1].substring(0,1).toUpperCase() + blockList[hotbarSelected-1].substring(1);
						}
					}					
				}
				

			}
		});
				
		Main.frame.addKeyListener(new KeyAdapter(){ //camera input
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if(key == KeyEvent.VK_E && Main.displayMenu == false && Input.controlsCounter % 2 == 0){
					invCounter++;
					if(invCounter % 2 == 1) {
						Input.w = false;
						Input.a = false;
						Input.s = false;
						Input.d = false;
						Input.zoom = false;
						drawInventory = true;
					}
					else {
						drawInventory = false;
					}
				}	
				if(key == KeyEvent.VK_1 && Input.pause == false) {	
					hotbarSelected = 1;
				}
				if(key == KeyEvent.VK_2 && Input.pause == false) {
					hotbarSelected = 2;
				}
				if(key == KeyEvent.VK_3 && Input.pause == false) {
					hotbarSelected = 3;					
				}
				if(key == KeyEvent.VK_4 && Input.pause == false) {
					hotbarSelected = 4;
				}
				if(key == KeyEvent.VK_5 && Input.pause == false) {
					hotbarSelected = 5;
				}
				if(key == KeyEvent.VK_6 && Input.pause == false) {
					hotbarSelected = 6;
				}
				if(key == KeyEvent.VK_7 && Input.pause == false) {
					hotbarSelected = 7;
				}
				if(key == KeyEvent.VK_8 && Input.pause == false) {
					hotbarSelected = 8;
				}
				if(key == KeyEvent.VK_9 && Input.pause == false) {
					hotbarSelected = 9;
				}
			}
		});
		
		Main.frame.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				//not used for inventory
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON2) {
					//PICK BLOCK ADD LATER
				}
				if(drawInventory == true) { //INVENTORY SWAP FUNCTIONALITY
					if(e.getButton() == MouseEvent.BUTTON1 && getSelection2 == false) { //get first block to swap
						for(int i = 0; i < 36; i++) {
							if(e.getX() * Main.windowScale >= invCoords[i][0] && e.getX() * Main.windowScale <= invCoords[i][2] + invCoords[i][0] &&
							   e.getY() * Main.windowScale >= invCoords[i][1] && e.getY() * Main.windowScale <= invCoords[i][3] + invCoords[i][1]) {								
								selection1 = (int) invCoords[i][4] - 1;
								getSelection2 = true;
							}
						}
					} else if(e.getButton() == MouseEvent.BUTTON1 && getSelection2 == true) { //get second block to swap
						for(int i = 0; i < 36; i++) {
							if(e.getX() * Main.windowScale >= invCoords[i][0] && e.getX() * Main.windowScale <= invCoords[i][2] + invCoords[i][0] &&
							   e.getY() * Main.windowScale >= invCoords[i][1] && e.getY() * Main.windowScale <= invCoords[i][3] + invCoords[i][1]) {
								selection2 = (int) invCoords[i][4] - 1;
								getSelection2 = false;
							}
						}
					} 
					
					if(selection1 != -1 && selection2 != -1) { //swap is finished, just fix blockList and reset selections
						String temp = blockList[selection1];
						blockList[selection1] = blockList[selection2];
						blockList[selection2] = temp;
						selection1 = -1;
						selection2 = -1;
					}
					
				}				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				//not used
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				//not used
			}

			@Override
			public void mouseExited(MouseEvent e) {
				//not used
			}
			
		});
		Timer timer = new Timer(8, new ActionListener() { //camera redrawing / movement / other
			
			@Override		
	        public void actionPerformed(ActionEvent e) {
				
				if(drawInventory == false) {
					getSelection2 = false;
					selection1 = -1;
					selection2 = -1;
					blockName = blockList[hotbarSelected-1].substring(0,1).toUpperCase() + blockList[hotbarSelected-1].substring(1);
				}
				
			}
		
		});
		timer.start();
	}
	
	
}