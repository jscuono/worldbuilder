import javax.sound.sampled.*;
//import java.io.File;

public class Music {
	
	private Clip clip;
	public Music(){
		
		try {	
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(getClass().getResource("/Tracks/track1.wav"));
			clip = AudioSystem.getClip();
			clip.open(audioInput);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			
			FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(-35);
		}
		catch (Exception e) {
			stop();
			e.printStackTrace();
		}		
	}
	
	public void stop() {
		clip.close();
	}

}
