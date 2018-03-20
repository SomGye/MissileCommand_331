import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author Maxwell Crawford
 *
 */
public class City 
{
	public int width = 45;
	
	public BufferedImage cityImg;
	public BufferedImage destroyedImg;
	
	public boolean isDestroyed = false;
	public Point location = new Point(0,0);
	
	public City(Point loc)
	{
		this.location = loc;
		cityImg = getCityImage();
	}
	
	public BufferedImage getCityImage()
	{
		try
		{
			cityImg = ImageIO.read(new File("src/newcity2.png"));
		}
		
		catch (IOException e)
		{
			System.err.println("Image file for city not found!");
		}
		
		return cityImg;
	}
	
	public BufferedImage getDestroyedImage()
	{
		try
		{
			destroyedImg = ImageIO.read(new File("src/newcity2d.png"));
		}
		
		catch (IOException e)
		{
			System.err.println("Image file for destroyed city not found!");
		}
		
		return destroyedImg;
	}
	
	/**
	 * Manual draw op's for cities at location, 
	 * depending on whether it is destroyed.
	 * @param g the graphics object to be redrawn
	 */
	public void draw(Graphics g)
	{
		//Gather graphics obj:
		Graphics2D g2 = (Graphics2D) g;
		
		if (!isDestroyed)
			g2.drawImage(cityImg, location.x, location.y, 45, 45, null);
		else
			g2.drawImage(destroyedImg, location.x, location.y, 45, 45, null);
	}

	
	// -- Getters/Setters --
	/**
	 * @return the isDestroyed
	 */
	public boolean isDestroyed() {
		return isDestroyed;
	}

	/**
	 * @param isDestroyed the isDestroyed to set
	 */
	public void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
		getDestroyedImage();
	}

	/**
	 * @return the location
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(Point location) {
		this.location = location;
	}
	
	
	
	

}
