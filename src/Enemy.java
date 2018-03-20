import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * 
 */

/**
 * Enemy class which consists of Bomber and Satellite
 * objects, who drop bombs/missiles on player Battery's.
 * @author Nick Ashton
 *
 */
public class Enemy {
	
	protected Point location;
	protected int xAxis;
	protected int yAxis;
	protected double Speed;
	protected int age;
	protected int maxAge;
	protected Image myImage;
	private JLabel enemy;
	private ImageIcon enemyIcon;
	private BufferedImage enemyImg;
	public boolean isDone = false; //flag for completion
	Random ran = new Random();
	protected int startPos = ran.nextInt(2);
	public boolean isBomber = true; //default enemy=bomber, false=satellite!
	
	/**
	 * The constructor takes the following arguments to build an enemy object
	 * of either a bomber or satellite class.
	 * @param l takes the point location for the enemy
	 * @param s 
	 * @param i 
	 * @param x takes starting x value for the enemy (for bomber and satellite, should be 0)
	 * @param y takes the starting y value for the enemy
	 */
	public Enemy(Point l) {
		//super();
		location = l;
		enemyImg = getEnemyImage();
	}
	
	/**
	 * Default constructor for enemy w/o location.
	 */
	public Enemy()
	{
		Random num = new Random();
		int startHeight = num.nextInt(200) + 50;
		if(startPos == 0){
			location = new Point(-5, startHeight);
			enemyImg = getEnemyImage();
		}
		else{
			location = new Point(760, startHeight); //default
			enemyImg = getReverseEnemyImage();
		}
	}
	
	/**
	 * Get default image for enemy, which is the bomber, 
	 * facing left.
	 * @return bomber left image.
	 */
	public BufferedImage getEnemyImage()
	{
		try
		{
			enemyImg = ImageIO.read(new File("src/Bomber2a.png"));
		}
		
		catch (IOException e)
		{
			System.err.println("Image file for enemy bomber not found!");
		}
		
		return enemyImg;
	}
	
	
	/**
	 * Image getter for when the enemy object appears on the right side
	 * @return left facing bomber enemy
	 */
	public BufferedImage getReverseEnemyImage()
	{
		try
		{
			enemyImg = ImageIO.read(new File("src/Bomber2a_mirror.png"));
		}
		
		catch (IOException e)
		{
			System.err.println("Image file for enemy bomber not found!");
		}
		
		return enemyImg;
	}
	
	
	/**
	 * @return the isDone
	 */
	public boolean isDone() {
		if(location.x < -10 || location.x > 800){
			isDone = true;
		}
		else{
			isDone = false;
		}
		return isDone;
	}

	/**
	 * @param isDone the isDone to set
	 */
	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	/**
	 * Method for getting the location of the enemy object
	 * @return location of the object
	 */
	public Point getLocation(){
		return location;
	}
	
	/**
	 * Method for setting the location of the object
	 * @param l is the position of the object
	 */
	public void setLocation(Point l){
		location = l;
	}
	
	
	/**
	 * Method to move the object along the x-axis(bombers and satellites only
	 * move left and right on the x axis).
	 */
	public void move() 
	{
		//location.x += 1;
		
		//Randomly generate movement speed
		Random num = new Random();
		int x = num.nextInt(3) + 1;

		if(startPos == 0){
			location.x += x;
		}
		else{
			location.x -= x;
		}
	}
	
	/**
	 * Method to draw the object with an image
	 * @param g
	 */
	public void draw(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(enemyImg, location.x, location.y, 74, 40, null); //was myImage
		
	}
	
	/**
	 * Method to get the image of the object
	 * @return the image 
	 */
	public JLabel getMyImage() {
		
		try{
			myImage = ImageIO.read(new File("Bomber.jpg"));
			enemyIcon = new ImageIcon(myImage);
		}
		
		catch(IOException e){
			System.err.println("Image file for bomber not found!");
		}
		
		enemy = new JLabel(enemyIcon);
		enemy.setIcon(enemyIcon);
		return enemy;
	}
	
	/**
	 * Method to change the image of the object
	 * @param i
	 */
	/*
	public void setMyImage(Image i){
		myImage = i;
	}*/
	
	
}
