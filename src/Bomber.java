import java.awt.Point;

/**
 * Bomber which is a subclass of Enemy;
 * will drop bombs on player Battery's.
 * @author Nick Ashton
 *
 */
public class Bomber extends Enemy {
	

	/**
	 * Construct Bomber object with current location.
	 * @param location coordinates in Point form.
	 */
	public Bomber(Point location) {
		super(location);
	}
	
	/**
	 * Default constructor for Bomber object.
	 */
	public Bomber()
	{
		super();
	}
	


}
