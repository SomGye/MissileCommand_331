import java.awt.Point;

/**
 * Satellite object which is a subclass of Enemy.
 * Drops bombs on player Battery's.
 * @author Nick Ashton
 *
 */
public class Satellite extends Enemy {
	

	/**
	 * Constructor for Satellite object with 
	 * location specified at Point.
	 * @param location the Point specified.
	 */
	public Satellite(Point location) {
		super(location);
	}
	
	/**
	 * Default constructor for Satellite object.
	 */
	public Satellite()
	{
		super();
	}
	


}

