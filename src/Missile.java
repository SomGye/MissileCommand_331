import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Class for Missile objects; holds velocity, location, and
 * player vs. enemy flags.
 * @author Maxwell Crawford
 *
 */
public class Missile 
{
	private static final int VELOCITY = 6;
	private static final int FAST_VELOCITY = 8; //used for center battery
	private int enemy_velocity = 3; //enemy starts slow, can increase each level!
	public static final int WIDTH = 10;
	public static final int HEIGHT = 17;
	
	private Point startPt = new Point(0, 0);
	private Point endPt = new Point(400, 300); //default center
	private double angle = 90.0; //default is pointing straight up! (90)
	private Point location = new Point(0,0); //track current location
	public boolean isDone = false; //set when location=endPt
	private boolean xDone = false;
	private boolean yDone = false;
	public boolean isFast = false; //set for center battery!
	public boolean isEnemy = false; //check for missile from bomber/satellite

	public BufferedImage missileImg;
	
	/**
	 * Constructor for missile with locations.
	 * @param start Firing position (battery).
	 * @param end Target position.
	 */
	public Missile(Point start, Point end)
	{
		this.startPt = start;
		
		//Check for even end point:
		if (end.x % 2 != 0)
			endPt.x  = (end.x + 1);
		else
			endPt.x = end.x;
		if (end.y % 2 != 0)
			endPt.y = (end.y + 1);
		else
			endPt.y = end.y;
		if ((end.x % 2 == 0) && (end.y % 2 == 0))
			this.endPt = end;
		
		this.location = new Point(start);
		missileImg = getMissileImage();
	}
	
	/**
	 * Constructor for missile with location AND fast flag on.
	 * @param start Firing position (battery).
	 * @param end Target position.
	 */
	public Missile(Point start, Point end, boolean fast)
	{
		this.startPt = start;
		
		//Check for even end point:
		if (end.x % 2 != 0)
			endPt.x  = (end.x + 1);
		else
			endPt.x = end.x;
		if (end.y % 2 != 0)
			endPt.y = (end.y + 1);
		else
			endPt.y = end.y;
		if ((end.x % 2 == 0) && (end.y % 2 == 0))
			this.endPt = end;
		
		this.isFast = fast;
		
		this.location = new Point(start);
		missileImg = getMissileImage();
	}
	
	/**
	 * Constructor for missile with defaults.
	 */
	public Missile()
	{
		missileImg = getMissileImage();
	}
	
	/**
	 * Refresh missile image from file.
	 * @return the new missile image (transparent PNG).
	 */
	public BufferedImage getMissileImage()
	{
		try
		{
			missileImg = ImageIO.read(new File("src/missile4a.png"));
		}
		
		catch (IOException e)
		{
			System.err.println("Image file for missile not found!");
		}
		
		return missileImg;
	}
	
	/**
	 * Manual draw op's for missiles: move, rotate, etc...
	 * @param g the graphics object to be redrawn
	 */
	public void draw(Graphics g)
	{
		//Gather graphics obj:
		Graphics2D g2 = (Graphics2D) g;
		
		//Ready angle rotation transform:
		AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(getNewAngle()), 
				WIDTH/2.0, HEIGHT/2.0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		
		
		//Re-draw the final missile image:
		g2.drawImage(op.filter(missileImg, null), getLocation().x, getLocation().y, null); //using rotation transform!
	}
	
	/**
	 * Move/update the missile position.
	 * NOTE: percentages and buffer distances are used to 
	 * fix the diagonal movement relative to target.
	 * Velocity's are chosen depending on flag values.
	 */
	public void move()
	{
		
		//Setup basic properties of movement:
		//NOTE: Buffer distance is crucial to missile removal!
		int endX = (int) endPt.getX();
		int endY = (int) endPt.getY();
		int currentVelocity = VELOCITY;
		if (isFast)
			currentVelocity = FAST_VELOCITY;
		else if (isEnemy)
			currentVelocity = enemy_velocity;
		
		int bufferDistance = currentVelocity; //within tolerance of dx/dy
		
		//Check relative x-y distances and even out
		//NOTE: This is crucial for correct DIAGONAL flight!
		int distx = getDistX();
		int disty = getDistY();
		int coeff = 0;
		double div = 0.0;
		boolean xIsShorter = false;
		boolean yIsShorter = false;
		
		if (distx < disty)
		{
			xIsShorter = true;
			div = ((double)distx / (double)disty);
			coeff = 1 - ((int)div);
			coeff += 1;
		}
		
		else if (distx > disty)
		{
			yIsShorter = true;
			div = ((double)disty / (double)distx);
			coeff = 1 - ((int)div);
			coeff += 1;
		}
		
		//Perform checks / change Missile location:
		if (!xDone) //if x hasn't reached end
		{
			if (location.x < (endX - bufferDistance))
			{
				if (yIsShorter)
					location.x += (currentVelocity * coeff);
				else
					location.x += currentVelocity;
			}
			if (location.x > (endX + bufferDistance))
			{
				if (yIsShorter)
					location.x -= (currentVelocity * coeff);
				else
					location.x -= currentVelocity;
			}
			
		}
		
		if (!yDone) //if y hasn't reached end
		{
			if (location.y < (endY - bufferDistance))
			{
				if (xIsShorter)
					location.y += (currentVelocity * coeff);
				else
					location.y += currentVelocity;
			}
			if (location.y > (endY + bufferDistance))
			{
				if (xIsShorter)
					location.y -= (currentVelocity * coeff);
				else
					location.y -= currentVelocity;
			}
			
		}
		
		if ((location.x >= (endX - bufferDistance)) 
				&& (location.x <= (endX + bufferDistance)))
		{
			xDone = true;
			yIsShorter = false; //reset
		}
		
		if ((location.y >= (endY - bufferDistance)) 
				&& (location.y <= (endY + bufferDistance)))
		{
			yDone = true;
			xIsShorter = false; //reset
		}
		
		if ((xDone) && (yDone)) //both are done, missile has reached endPt
		{
			isDone = true; //flag, Screen class checks and removes
		}
		
	}
	
	// -- Getters/Setters --
	
	private int getDistX()
	{
		int dist = (int) Math.sqrt(Math.pow(this.getLocation().x - this.getEndPt().getX(), 2));
		return dist;
	}
	
	private int getDistY()
	{
		int dist = (int) Math.sqrt(Math.pow(this.getLocation().y - this.getEndPt().getY(), 2));
		return dist;
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
	
	/**
	 * @return the startPt
	 */
	public Point getStartPt() {
		return startPt;
	}
	/**
	 * @param startPt the startPt to set
	 */
	public void setStartPt(Point startPt) {
		this.startPt = startPt;
	}
	/**
	 * @return the endPt
	 */
	public Point getEndPt() {
		return endPt;
	}
	
	/**
	 * @param endPt the endPt to set
	 */
	public void setEndPt(Point end) {
		//Check for even end point:
		if (end.x % 2 != 0)
			endPt.x  = (end.x + 1);
		else
			endPt.x = end.x;
		if (end.y % 2 != 0)
			endPt.y = (end.y + 1);
		else
			endPt.y = end.y;
		if ((end.x % 2 == 0) && (end.y % 2 == 0))
			this.endPt = end;
	}
	
	/**
	 * @return the angle
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * @param angle the angle to set
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}
	
	/**
	 * Produce and return an angle based on endpoint with 
	 * respect to current location.
	 * @return the new angle as a double (in Degrees).
	 */
	public double getNewAngle()
	{
		//Get distance b/w points and determine angle from
		// inverse tangent of x/y:
		int distX = getDistX();
		if (endPt.getX() < getLocation().x)
		{
			distX *= -1;
		}
		int distY = getDistY();
		if (isEnemy) //ENEMY FLIPS Y
			distY *= -1;
		
		this.angle = (double) Math.toDegrees(Math.atan2(distX, distY));
		
		//Keep angle b/w 0-360:
		if (angle < 0)
		{
			angle += 360;
		}
		
		else if (angle > 360)
		{
			angle -= 360;
		}
		
		return angle;
	}

	/**
	 * @return the isEnemy
	 */
	public boolean isEnemy() {
		return isEnemy;
	}

	/**
	 * @param isEnemy the isEnemy to set
	 */
	public void setEnemy(boolean isEnemy) {
		this.isEnemy = isEnemy;
	}
	
	/**
	 * Increase enemy velocity by 2,
	 * used for level progression.
	 */
	public void increaseEnemyVel()
	{
		enemy_velocity += 2;
	}
	
	/**
	 * Reset enemy velocity back to 4.
	 */
	public void resetEnemyVel()
	{
		enemy_velocity = 4;
	}

	
}
