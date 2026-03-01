import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.Timer;
import javax.swing.UIManager;

public class Menus{
	
	public static boolean worldSaved = false;
	public static boolean worldLoaded = false;
	
	public Menus() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			//Try to create a worlds folder if it doesn't exist
			String folderPath = "Worlds";
		    File newFolder = new File(folderPath);
	    	boolean created = newFolder.mkdir();
		    if(created == true) {
		    	System.out.println("Worlds folder created");
		    } else {
		    	System.out.println("Worlds folder already exists or error");
		    }
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		Main.frame.addKeyListener(new KeyAdapter() {
			
		    
			@Override
			public void keyPressed(KeyEvent e) {	    	
				if(e.getKeyCode() == KeyEvent.VK_R) {
					try {
						
					    
					    JFileChooser files = new JFileChooser();
						files.setCurrentDirectory(new File("Worlds"));
						files.showSaveDialog(Main.frame);
						File selectedFile = files.getSelectedFile();
					    
						File world = new File("Worlds/" + selectedFile.getName());
						world.createNewFile();
						BufferedWriter writer = new BufferedWriter(new FileWriter(world));
						for(int i = 0; i < 500; i++) {
							for(int j = 0; j < 1000; j++) {
								if(Game.tileMap[i][j] == null) {
									writer.write("null" + " ");
									continue;
								}
								writer.write(Game.tileMap[i][j] + " ");
							}
						}
						//Save prompt in top left
						worldSaved = true;
						Timer timer = new Timer(5000, new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								worldSaved = false;
								
							}
						});
						timer.setRepeats(false);
						timer.start();
						writer.close();

					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
				if(e.getKeyCode() == KeyEvent.VK_L) {
					try {
						JFileChooser files = new JFileChooser();
						files.setCurrentDirectory(new File("Worlds"));
						files.showOpenDialog(Main.frame);
						File selectedFile = files.getSelectedFile();
						
						Scanner scanner = new Scanner(selectedFile);
						for(int i = 0; i < 500; i++) {
							for(int j = 0; j < 1000; j++) {
								Game.tileMap[i][j] = scanner.next();
								if(Game.tileMap[i][j].equals("null")) {
									Game.tileMap[i][j] = null;
								}
							}
						}
						Main.newGame.loadMap(Game.tileMap);
						//Load prompt in top left
						worldLoaded = true;
						Timer timer = new Timer(5000, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								worldLoaded = false;
							}
						});
						timer.setRepeats(false);
						timer.start();
						scanner.close();
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
	}

		
	
}
