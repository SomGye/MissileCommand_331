import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Weapon class which handles the crosshair used
 * to aim the player missiles.
 * NOTE: Uses JLabel to hold and display the image.
 * @author Maxwell Crawford
 *
 */
public class Weapon extends JLabel
{
	private JLabel crosshair;
	private ImageIcon hairIcon;
	private BufferedImage hairImg;
	
	public boolean mouseInWindow = false; //when mouse is in window, play game, move crosshair
	private int width = 800;
	//private int height = 600;
	
	/**
	 * Constructor for Weapon which initializes the visibility.
	 */
	public Weapon()
	{
		setVisible(true);
	}
	
	/**
	 * Grabs the image for the weapon crosshairs.
	 * @return the crosshair as a JLabel, with img pre-set.
	 */
	public JLabel getImage()
	{
		try
		{
			hairImg = ImageIO.read(new File("src/crosshair_4a.png"));
			hairIcon = new ImageIcon(hairImg);
		}
		
		catch (IOException e)
		{
			System.err.println("Image file for crosshair not found!");
		}
		
		crosshair = new JLabel(hairIcon);
		crosshair.setIcon(hairIcon);
		crosshair.setVisible(false); //set to false later, make visible when mouseover
		return crosshair;
	}
	
	
	/**
	 * Get condition of whether mouse is in window.
	 * @return the boolean value.
	 */
	public boolean isMouseInWindow()
	{
		return mouseInWindow;
	}
	
	/**
	 * Set condition of whether mouse is in window.
	 * @param mousebool the new true/false value.
	 */
	public void setMouseInWindow(boolean mousebool)
	{
		mouseInWindow = mousebool;
	}
}
