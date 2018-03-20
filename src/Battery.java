
import java.awt.image.BufferedImage;

/**
 * @author Maxwell Crawford
 *
 */
public class Battery 
{
	private int currentAmmo = 30; //start with 30 missiles total, 10 per battery
	private int leftAmmo = 10;
	private int centerAmmo = 10;
	private int rightAmmo = 10;
	private boolean isLeft = false; //check if left,center, or right battery
	private boolean isCenter = false;
	private boolean isRight = false;
	public boolean leftIsLow = false; //checks for battery low on ammo
	public boolean centerIsLow = false;
	public boolean rightIsLow = false;
	public boolean leftEmpty = false;
	public boolean centerEmpty = false;
	public boolean rightEmpty = false;
	public boolean isLeftDead = false; //check for destroyed batteries; draw alt. versions
	public boolean isCenterDead = false;
	public boolean isRightDead = false;
	private boolean isRefilled = false; //check for refill at end of level
	
	public BufferedImage batteryImg;
	
	/**
	 * Construct a battery object and refresh the defaults.
	 */
	public Battery()
	{
		currentAmmo = 30; //start with 30 missiles total, 10 per battery
		leftAmmo = 10;
		centerAmmo = 10;
		rightAmmo = 10;
	}
	
	/**
	 * Fire the left battery.
	 */
	public void fireLeft()
	{
		if (currentAmmo > 0)
			currentAmmo -= 1;
		if (leftAmmo < 6)
			leftIsLow = true;
		if (leftAmmo > 1)
			leftAmmo -= 1;
		else
			leftEmpty = true;
	}
	
	/**
	 * Fire the center battery.
	 */
	public void fireCenter()
	{
		if (currentAmmo > 0)
			currentAmmo -= 1;
		if (centerAmmo < 6)
			centerIsLow = true;
		if (centerAmmo > 1)
			centerAmmo -= 1;
		else
			centerEmpty = true;
	}
	
	/**
	 * Fire the right battery.
	 */
	public void fireRight()
	{
		if (currentAmmo > 0)
			currentAmmo -= 1;
		if (rightAmmo < 6)
			rightIsLow = true;
		if (rightAmmo > 1)
			rightAmmo -= 1;
		else
			rightEmpty = true;
	}
	
	/**
	 * Refill ammo counters to defaults and reset ammo flags.
	 */
	public void refillAmmo()
	{
		currentAmmo = 30; //back to full
		leftAmmo = 10;
		centerAmmo = 10;
		rightAmmo = 10;
		
		leftEmpty = false;
		centerEmpty = false;
		rightEmpty = false;
		
		//Perform animations of filling left, center, and right batteries:
	}
	
	
	/**
	 * @return the isLeft
	 */
	public boolean isLeft() 
	{
		return isLeft;
	}

	/**
	 * @param isLeft the isLeft to set
	 */
	public void setLeft(boolean isLeft) 
	{
		this.isLeft = isLeft;
	}

	/**
	 * @return the isCenter
	 */
	public boolean isCenter() 
	{
		return isCenter;
	}

	/**
	 * @param isCenter the isCenter to set
	 */
	public void setCenter(boolean isCenter) 
	{
		this.isCenter = isCenter;
	}

	/**
	 * @return the isRight
	 */
	public boolean isRight() 
	{
		return isRight;
	}

	/**
	 * @param isRight the isRight to set
	 */
	public void setRight(boolean isRight) 
	{
		this.isRight = isRight;
	}

	/**
	 * @return the isLeftDead
	 */
	public boolean isLeftDead() {
		return isLeftDead;
	}

	/**
	 * @param isLeftDead the isLeftDead to set
	 */
	public void setLeftDead(boolean isLeftDead) {
		this.isLeftDead = isLeftDead;
	}

	/**
	 * @return the isCenterDead
	 */
	public boolean isCenterDead() {
		return isCenterDead;
	}

	/**
	 * @param isCenterDead the isCenterDead to set
	 */
	public void setCenterDead(boolean isCenterDead) {
		this.isCenterDead = isCenterDead;
	}

	/**
	 * @return the isRightDead
	 */
	public boolean isRightDead() {
		return isRightDead;
	}

	/**
	 * @param isRightDead the isRightDead to set
	 */
	public void setRightDead(boolean isRightDead) {
		this.isRightDead = isRightDead;
	}
	
	
}
