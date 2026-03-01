import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;

@SuppressWarnings("serial")
public class Game extends JPanel {
	
	public class Block {
		
		int x; //x position on map
		int y; //y position on map
		int width; //width of block
		int height; //height of block
		String name; //block name
		Image image; //image of block
		boolean gravity; //gravity block (ex: sand)
		boolean flowing; //flowing block (ex: water)
		
		Block(Image image, String name, int x, int y, int width, int height){
			this.image = image;
			this.name = name;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			//generic blocks
		}
		Block(Image image, String name, int x, int y, int width, int height, boolean gravity, boolean flowing){
			this.image = image;
			this.name = name;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.gravity = gravity;
			this.flowing = flowing;
			//gravity blocks
		}
		
		
	}	
	
	//world size
	private int rows = 500;
	private int columns = 1000;
	private int tileSize = 16;
	private int worldWidth = columns * tileSize;
	private int worldHeight = rows * tileSize;
	
	// block textures
	private Image dirtImage = new ImageIcon(getClass().getResource("/Textures/dirt.png")).getImage();
	private Image grassImage  = new ImageIcon(getClass().getResource("/Textures/grass.png")).getImage();
	private Image tree_woodImage = new ImageIcon(getClass().getResource("/Textures/tree_wood.png")).getImage();
	private Image leavesImage = new ImageIcon(getClass().getResource("/Textures/leaves.png")).getImage();
	private Image stoneImage = new ImageIcon(getClass().getResource("/Textures/stone.png")).getImage();
	private Image sandImage = new ImageIcon(getClass().getResource("/Textures/sand.png")).getImage();
	private Image cactusImage = new ImageIcon(getClass().getResource("/Textures/cactus.png")).getImage();
	private Image waterImage = new ImageIcon(getClass().getResource("/Textures/water.png")).getImage();
	private Image woodImage = new ImageIcon(getClass().getResource("/Textures/wood.png")).getImage();
	
	//button textures
	private Image play_buttonImage = new ImageIcon(getClass().getResource("/Textures/play_button.png")).getImage();
	private Image controls_buttonImage = new ImageIcon(getClass().getResource("/Textures/controls_button.png")).getImage();
	
	//misc textures
	private Image block_targetedImage = new ImageIcon(getClass().getResource("/Textures/block_targeted.png")).getImage();
	
	//day night stuff
	static int dayStage = 0;
	
	//stores each individual block
	static ArrayList<Block> blockSet;
	
	//declaring block types
	static int numBlocks = 0;
	
	private Camera camera;
	public static String[][] tileMap;
	
	//menu stuff
	//change all this to width / 2 - x and width / 2 + x then make the width in the required functions be width/2-x / width/2+x
	//maybe rename these variables to left, right, top, and bottom
	static int playX = (int) ((Main.worldWidth / 2 - 256) * Main.windowScale);
	static int playY = (int) ((Main.worldHeight / 2 - 192 - 192) * Main.windowScale);
	static int playWidth = (int) ((Main.worldWidth / 2 + 256) * Main.windowScale);
	static int playHeight = (int) ((Main.worldHeight / 2 - 192) * Main.windowScale);
	
	static int controlsX = (int) ((Main.worldWidth / 2 - 256) * Main.windowScale);
	static int controlsY = (int) ((Main.worldHeight / 2 + 192) * Main.windowScale);
	static int controlsWidth = (int) ((Main.worldWidth / 2 + 256) * Main.windowScale);
	static int controlsHeight = (int) ((Main.worldHeight / 2 + 256 + 128) * Main.windowScale);
	
	public Game(Camera camera){
		this.camera = camera;
		
		setPreferredSize(new Dimension(worldWidth, worldHeight));
		setBackground(new Color(195, 250, 246));
		
	/*	Timer dayNightCycle = new Timer(10, new ActionListener() {

			int R = 195;
			int G = 250;
			int B = 246;
					
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
			
		});
		dayNightCycle.start();*/
			
		tileMap = new String[rows][columns]; //world gen
		Random randomNum = new Random();
		int treeIndex = -3; //trees must be separated by at least 3 spaces. used later in for loops
		int cactusIndex = -5; //cacti must be separated by at least 5 spaces. used later in for loops
		int desertIndex = -100; //deserts must be separated by at least 100 spaces. used later in for loops
		for(int i = 30; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				
				if(i == 30){ //grass
					int randomY = randomNum.nextInt(3) + 3;
					int randomX = randomNum.nextInt(4) + 1;
					for(int k = 0; k < randomY; k++){
						if(j + k < 1000){
							tileMap[i + randomX][j + k] = "Grass";
						}

					}
					
				}
				if(i > 50) { //stone
					int randomY = randomNum.nextInt(5) + 3;
					int randomX = randomNum.nextInt(10) + 1;
					for(int k = 0; k < randomY; k++){
						if(j + k < 1000 && i + randomX < 500){
							tileMap[i + randomX][j + k]  = "Stone";
						}
						if(tileMap[i-1][j] == "Stone") {
							tileMap[i][j] = "Stone";
						}

					}
					
				}
				if(i != 0 && i != 499){ //dirt
					if(tileMap[i][j] != "Stone" && (tileMap[i-1][j] == "Grass" || tileMap[i-1][j] == "Dirt" || tileMap[i-1][j] == "Sand")) {
						tileMap[i][j] = "Dirt";
					}
				}
				if(tileMap[i][j] == "Grass") { //sky
					for(int k = i; k > 0; k--) {
						if(tileMap[k-1][j] == null) {
							tileMap[k-1][j] = null;
						}					
					}

				}
				
				if(i == 41) { //generate after desert
					int randomTree = randomNum.nextInt(10); //tree generation
					int randomCactus = randomNum.nextInt(5);
					if(tileMap[i-10][j] == "Grass" && j - treeIndex > 2 && randomTree == 0) {
						treeIndex = j;
						int randomTreeHeight = randomNum.nextInt(8) + 4;
						for(int k = i - 11; k > i - 11 - randomTreeHeight; k--) {
							tileMap[k][j] = "Tree_wood";
						}
						tileMap[i - 11 - randomTreeHeight][j] = "Leaves";
						tileMap[i - 12 - randomTreeHeight][j] = "Leaves";
						if(j < 999) {
							tileMap[i - 11 - randomTreeHeight][j+1] = "Leaves";
							tileMap[i - 10 - randomTreeHeight][j+1] = "Leaves";
						}
						if(j > 0) {
							tileMap[i - 11 - randomTreeHeight][j-1] = "Leaves";
							tileMap[i - 10 -randomTreeHeight][j-1] = "Leaves";
						}

					}
					if(tileMap[i-10][j] == "Sand" && j - cactusIndex > 5 && randomCactus == 0) {
						cactusIndex = j;
						int randomCactusHeight = randomNum.nextInt(3) + 6;
						for(int k = i - 11; k > i - 11 - randomCactusHeight; k--) {
							tileMap[k][j]  = "Cactus";
						}
						if(j < 998) {
							tileMap[i - 7 - randomCactusHeight][j+1] = "Cactus";
							tileMap[i - 7 - randomCactusHeight][j+2] = "Cactus";
							tileMap[i - 8 - randomCactusHeight][j+2] = "Cactus";
							tileMap[i - 9 - randomCactusHeight][j+2] = "Cactus";
						}
						if(j > 1) {
							tileMap[i - 7 -randomCactusHeight][j-1] = "Cactus";
							tileMap[i - 7 - randomCactusHeight][j-2] = "Cactus";
							tileMap[i - 8 - randomCactusHeight][j-2] = "Cactus";
							tileMap[i - 9 - randomCactusHeight][j-2] = "Cactus";
						}

					}
				}
				if(i == 40) {
					int randomDesert = randomNum.nextInt(100);
					if(randomDesert == 99 && j < 990 && j > 10 && j - desertIndex > 99) {
						desertIndex = j;
						int desertSize = randomNum.nextInt(20) + 20;
						int radius = 3;
						for(int k = 30; k < 50; k++) {
							if(tileMap[k][j] == "Grass") {
								tileMap[k][j] = "Sand";
								for(int l = k - 5; l < 50; l++) { //k - 5 because grass could be located left or right above the chosen grass block
									for(int m = j; m > j - desertSize; m--) {
										if(m >= 0 && (tileMap[l][m] == "Grass" || tileMap[l][m] == "Dirt")) {
											tileMap[l][m] = "Sand";
										}
										else if(m >= 0 && tileMap[l][m] != "Sand" && l > 30) {
											tileMap[l][m] = "Water";
										}
									}
									for(int m = j; m < j + desertSize; m++) {
										if(m < 1000 && (tileMap[l][m] == "Grass" || tileMap[l][m] == "Dirt")) {
											tileMap[l][m] = "Sand";
										}	
										else if(m < 1000 && tileMap[l][m] != "Sand" && l > 30) {
											tileMap[l][m] = "Sand";
										}
									}
									if(radius < -5) {
										desertSize -= 2;
										radius--;
									}
									else if(radius % 1 == 0) {
										radius--;
										desertSize--;
									}
									else {
										radius--;
									}

								}

								break;
							}	
						}
					}
				}
				
				
			}
			
		}
		
		loadMap(tileMap);
	}

	public void loadMap(String[][] tileMap) {
		blockSet = new ArrayList<Block>();
		
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < columns; c++) {
				String tileMapString = tileMap[r][c];
				
				int x = c * tileSize;
				int y = r * tileSize;
				if(tileMapString == null) {
					continue;
				}
				Image image;
				switch (tileMapString) {
	                case "Grass":
	                    image = grassImage;
	                    break;
	                case "Dirt":
	                    image = dirtImage;
	                    break;
	                case "Tree_wood":
	                    image = tree_woodImage;
	                    break;
	                case "Leaves":
	                    image = leavesImage;
	                    break;
	                case "Stone":
	                    image = stoneImage;
	                    break;
	                case "Sand":
	                    image = sandImage;
	                    blockSet.add(new Block(image, tileMapString, x, y, tileSize, tileSize, true, false));
	                    break;
	                case "Water":
	                    image = waterImage;
	                    blockSet.add(new Block(image, tileMapString, x, y, tileSize, tileSize, true, true));
	                    break;
	                case "Cactus":
	                    image = cactusImage;
	                    break;
	                case "Wood":
	                    image = woodImage;
	                    break;
	                default:
	                    continue;
				}
			
				blockSet.add(new Block(image, tileMapString, x, y, tileSize, tileSize));
			}
		}
	}
	
	static int sandCounter = 1;//how many blocks sand needs to fall
	public void replaceBlock(int tileX, int tileY, String blockName) { //takes the tile form of the targeted block
		
		int worldX = tileX * tileSize; //converts to world coordinates
        int worldY = tileY * tileSize;
        if(blockName != null && blockName.equals("Sky")) {
        	blockName = null;
        	tileMap[tileY][tileX] = null;
			blockSet.removeIf(block -> block.x == worldX && block.y == worldY);	   
		}
		if(blockName != null && blockName.equals("Water") && tileMap[tileY][tileX] == null) {
			tileMap[tileY][tileX] = "Water";
			blockSet.add(new Block(new ImageIcon(getClass().getResource("/Textures/water.png")).getImage(), "Water", worldX, worldY, tileSize, tileSize, true, true));
			//since it is guaranteed a tile is null, no need to attempt to remove anything.
		}
		else if(blockName != null && blockName.equals("Sand")) {
			while(tileY+sandCounter < 500 && ((tileMap[tileY+sandCounter][tileX] != null && tileMap[tileY+sandCounter][tileX].equals("Water")) || tileMap[tileY+sandCounter][tileX] == null)) {
				sandCounter++;
			}
			tileMap[tileY][tileX] = null;
			tileMap[tileY+sandCounter-1][tileX] = "Sand";
			blockSet.removeIf(block -> block.x == worldX && block.y == worldY);	    
			blockSet.removeIf(block -> block.x == worldX && block.y == worldY + tileSize*(sandCounter - 1));	
			blockSet.add(new Block(new ImageIcon(getClass().getResource("/Textures/sand.png")).getImage(), "Sand", worldX, worldY + tileSize*(sandCounter-1), tileSize, tileSize, true, false));
			sandCounter = 1;
		}
		else {
			blockSet.removeIf(block -> block.x == worldX && block.y == worldY);	   
	        if(blockName != null) {
		        blockSet.add(new Block(new ImageIcon(getClass().getResource("/Textures/" + blockName.toLowerCase() + ".png")).getImage(), blockName, worldX, worldY, tileSize, tileSize));	    
		        return;
	        }
		}
    }
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
        double width = Main.worldWidth;
        double height = Main.worldHeight;
        double xScale = width/1706.67 * Main.windowScale / 1.5; //scale for graphics on all resolutions
        double yScale = height/1066.67 * Main.windowScale / 1.5;
        
        super.paintComponent(g);
        g2d.translate(width / 2.0, height / 2.0); //zoom
        g2d.scale(camera.zoom, camera.zoom);
        g2d.translate(-width / 2.0, -height / 2.0);
        
        
        g2d.translate(-camera.x, -camera.y);
        draw(g);
        
       /* if(Input.pause == false) {
			g2d.drawImage(block_targetedImage, Input.targetedX, Input.targetedY, tileSize, tileSize, null);
        }*/
        
        g2d.setTransform(new AffineTransform());
        g2d.setColor(Color.RED); //FPS Counter
        g2d.setFont(new Font("Arial", Font.BOLD, (int) (16 * Main.windowScale)));
        g2d.drawString("FPS: " + Main.fps, (int) ((width-72) * Main.windowScale), (int) (20 * Main.windowScale));
        g2d.drawString("X: " + (int)((2 * camera.x + width) / 2 / tileSize) + " " + "Y: " + (int)((2 * camera.y + height) / 2 / tileSize), 
        (int) ((width-110) * Main.windowScale), (int) (40 * Main.windowScale));
      //  g2d.drawString("+", (int)(Main.windowScale * width/2), (int)( Main.windowScale * height/2)); //crosshair if wanted
            
        if(Menus.worldLoaded == true) { //world loaded IO
			g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, (int) (16 * Main.windowScale)));
            g2d.drawString("World Loaded Successfully ", (int) (16 * Main.windowScale), (int) (20 * Main.windowScale));

        }
        if(Menus.worldSaved == true) { //world saved IO
			g2d.setColor(Color.RED);
	        g2d.setFont(new Font("Arial", Font.BOLD, (int) (16 * Main.windowScale)));
	        g2d.drawString("World Saved Successfully ", (int) (16 * Main.windowScale), (int) (20 * Main.windowScale));
        }
        if(Input.controlsCounter % 2 == 0 && Main.displayMenu == true) { //main menu drawing
			g2d.setColor(new Color(130, 130, 130, 200)); //rgb 130, alpha 200
			g2d.fillRect(0, 0, (int)(Main.worldWidth * Main.windowScale), (int)(Main.worldHeight * Main.windowScale));
			
			g2d.setColor(Color.DARK_GRAY); //play button
	        g2d.fillRect(playX, playY, playWidth - playX, playHeight - playY);
	        g2d.setColor(Color.GRAY);
	        g2d.fillRect(playX + 8, playY + 8, playWidth - playX - 16, playHeight - playY - 16);
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.setFont(new Font("Monospaced", Font.BOLD, (int) (128 / Main.windowScale)));
			g2d.drawImage(play_buttonImage, playX, playY, playWidth - playX, playHeight - playY, null);
	        
	        g2d.setColor(Color.DARK_GRAY); //options button
	        g2d.fillRect(controlsX, controlsY, controlsWidth - controlsX, controlsHeight - controlsY);
	        g2d.setColor(Color.GRAY);
	        g2d.fillRect(controlsX + 8, controlsY + 8, controlsWidth - controlsX - 16, controlsHeight - controlsY - 16);
	        g2d.setColor(Color.LIGHT_GRAY);
	        g2d.setFont(new Font("Monospaced", Font.BOLD, (int) (128 / Main.windowScale)));
	        g2d.drawImage(controls_buttonImage, controlsX, controlsY, controlsWidth - controlsX, controlsHeight - controlsY, null);
	        
		}
        else if(Input.controlsCounter % 2 == 1) { //controls menu
        	g2d.setColor(new Color(130, 130, 130, 200)); //rgb 130, alpha 200
			g2d.fillRect(0, 0, (int)(Main.worldWidth * Main.windowScale), (int)(Main.worldHeight * Main.windowScale));
			for(int j = 1; j < 5; j++) {
				for(int i = 1; i < 10; i++) {
					g2d.setColor(Color.GRAY);
	                g2d.fillRect((int)((256 * i - 80) * xScale), (int)(-300 + 400 * j * yScale), (int)((tileSize*8 + 10) * xScale), (int)((tileSize*8 + 10) * yScale)); 
	                Input.controlsCoords[i-1][0] = (256 * i - 80) * xScale; //for button input
	                Input.controlsCoords[i-1][1] = (100 * j) * yScale;
	                Input.controlsCoords[i-1][2] = (tileSize*8 + 10) * xScale;
	                Input.controlsCoords[i-1][3] = (tileSize*8 + 10) * yScale;
	                Input.controlsCoords[i-1][4] = i; //to store which block corresponds to which button
	            }  				
			}
        }
        else if(Main.displayMenu == false && Inventory.invCounter % 2 == 0) { //inventory hotbar drawing
        	for(int i = 1; i < 10; i++) {
        		if(Inventory.hotbarSelected == i) {
                	g2d.setColor(Color.GREEN);
        		}
        		else {
                	g2d.setColor(Color.GRAY);
        		}
                g2d.fillRect((int)((100 * i - 55) * xScale), (int)(45 * yScale), (int)((tileSize*4 + 10) * xScale), (int)((tileSize*4 + 10) * yScale));
                g2d.drawImage(new ImageIcon(getClass().getResource("/Textures/" + Inventory.blockList[i-1] + ".png")).getImage(), (int)((100 * i - 50) * xScale ), (int)(50 * yScale), (int)(tileSize * 4 * xScale), (int)(tileSize * 4 * yScale), null);             
            }  
        }
        else if(Inventory.drawInventory == true && Inventory.invCounter % 2 == 1) { //full inventory drawing
        	g2d.setColor(new Color(130, 130, 130, 200)); //rgb 130, alpha 200
			g2d.fillRect(0, 0, (int)(Main.worldWidth * Main.windowScale), (int)(Main.worldHeight * Main.windowScale));
			for(int j = 1; j < 5; j++) {
				for(int i = 1; i < 10; i++) { //hotbar row
					if(j == 1) {
						if(Inventory.selection1 == i-1) {
		                	g2d.setColor(Color.GREEN);
		        		} else {
							g2d.setColor(Color.GRAY);	        			
		        		}

		                g2d.fillRect((int)((256 * i - 80) * xScale), (int)(100 * j * yScale), (int)((tileSize*8 + 10) * xScale), (int)((tileSize*8 + 10) * yScale));
		                g2d.drawImage(new ImageIcon(getClass().getResource("/Textures/" + Inventory.blockList[i-1] + ".png")).getImage(), (int)((256 * i - 75) * xScale), (int)((100 * j + 5) * yScale), (int)(tileSize* 8 * xScale), (int)(tileSize* 8 * yScale), null);
		                Inventory.invCoords[i-1][0] = (256 * i - 80) * xScale; //for button input
		                Inventory.invCoords[i-1][1] = (100 * j) * yScale;
		                Inventory.invCoords[i-1][2] = (tileSize*8 + 10) * xScale;
		                Inventory.invCoords[i-1][3] = (tileSize*8 + 10) * yScale;
		                Inventory.invCoords[i-1][4] = i; //to store which block corresponds to which button
		                
					}else {
						if(Inventory.selection1 == (i-1) + (j-1) * 9) { //inventory rows
		                	g2d.setColor(Color.GREEN);
		        		} else {
							g2d.setColor(Color.GRAY);	        			
		        		}
		                g2d.fillRect((int)((256 * i - 80) * xScale), (int)(300 * j * yScale), (int)((tileSize*8 + 10) * xScale), (int)((tileSize*8 + 10) * yScale));
		                g2d.drawImage(new ImageIcon(getClass().getResource("/Textures/" + Inventory.blockList[(i-1) + (j-1) * 9] + ".png")).getImage(), (int)((256 * i - 75) * xScale), (int)((300 * j + 5) * yScale), (int)(tileSize* 8 * xScale), (int)(tileSize* 8 * yScale), null);
		                Inventory.invCoords[(i-1) + (j-1) * 9][0] = (256 * i - 80) * xScale; //for button input
		                Inventory.invCoords[(i-1) + (j-1) * 9][1] = (300 * j) * yScale;
		                Inventory.invCoords[(i-1) + (j-1) * 9][2] = (tileSize*8 + 10) * xScale;
		                Inventory.invCoords[(i-1) + (j-1) * 9][3] = (tileSize*8 + 10) * yScale;
		                Inventory.invCoords[(i-1) + (j-1) * 9][4] = (i-1) + (j-1) * 9 + 1; //to store which block corresponds to which button
					}
	            }  				
			}
        	
        }
        
        
        
	}

	public void draw(Graphics g) {

        int minX = g.getClipBounds(getVisibleRect()).x - tileSize;
        int maxX = g.getClipBounds(getVisibleRect()).x + g.getClipBounds(getVisibleRect()).width + tileSize;
        int minY = g.getClipBounds(getVisibleRect()).y - tileSize;
        int maxY = g.getClipBounds(getVisibleRect()).y + g.getClipBounds(getVisibleRect()).height + tileSize;
        for (Block block : blockSet) {
        	if (block.x >= minX  && block.x <= maxX &&
        		block.y >= minY && block.y <= maxY) {
        		g.drawImage(block.image, block.x, block.y, block.width, block.height, null);
            }
        	
            
        }
       
    }
}

	