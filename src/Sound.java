import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Sound object class that can play, loop, 
 * or stop the sound file (in WAV format).
 * @author Maxwell Crawford
 * NOTE: Credit to Tyler Thomas (TdotThomas on StackOverflow) for
 * steering in the right direction!
 * Link: http://stackoverflow.com/questions/11919009/using-javax-sound-sampled-clip-to-play-loop-and-stop-mutiple-sounds-in-a-game
 *
 */
public class Sound 
{
    private Clip clip;
    
    /**
     * Constructor which receives filename of WAV.
     * @param fileName the WAV file to use.
     */
    public Sound(String fileName) 
    {
        try 
        {
            File file = new File(fileName);
            if (file.exists()) 
            {
                AudioInputStream sound = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip(); //get sound as input stream
                clip.open(sound);
            }
            else 
            {
                throw new RuntimeException("Sound file not found at: " + fileName);
            }
        }
        catch (MalformedURLException e)
        {
            System.out.println("Sound URL is not valid! ");
        }
        catch (UnsupportedAudioFileException e) 
        {
        	System.out.println("Sound format is unsupported.");
        }
        catch (IOException e) 
        {
        	System.out.println("Sound Input/Output Error: " + e);
        }
        catch (LineUnavailableException e) 
        {
        	System.out.println("Sound Line Unavailable Exception Error: " + e);
        }

    }
    
    //Sound Functions: 

    /**
     * Play the sound clip.
     */
    public void play()
    {
    	clip.setFramePosition(0);  // Must always rewind!
    	clip.start();
    }

    /**
     * Loop the sound clip continuously,
     * until stop() is called.
     */
    public void loop()
    {
    	clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Stop the sound clip immediately.
     */
    public void stop()
    {
    	clip.stop();
    }
    
}