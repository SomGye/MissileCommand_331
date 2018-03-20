import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

/**
 * Class for creating Explosions, which are
 * ellipses that grow and shrink and change color.
 * @author Maxwell Crawford
 *
 */
public class Explosion
{
	private boolean isDone = false; //flag for completion
	private boolean riseDone = false; //flag for explosion growth done
	private boolean fallDone = false; //flag for explosion shrinkage done
	private Ellipse2D.Double explodeCirc = new Ellipse2D.Double(0,0,0,0); //will change during explode()
	private int cycle = 1; //cycles 1-10 total, 1-5=rise, 6-9=fall, 10=done
	private Point location = new Point(400, 300); //center default
	private int width = 5;
	private int height = 5;

	/**
	 * Perform explosion at coordinates,
	 * using an Ellipse2D.Double object,
	 * growing, flashing colors, and then shrinking.
	 * @param loc the given location Point.
	 */
	public void explode(Point loc)
	{
		this.location = loc;
		explodeCirc = new Ellipse2D.Double(loc.getX(), loc.getY(), width, height);
		isDone = false;
	}
	
	/**
	 * Manual draw op's for explosions: Rise, fall cycles;
	 * will change size and color, then flag completion.
	 * @param g the graphics object to be redrawn
	 */
	public void draw(Graphics g)
	{
		//Gather graphics obj:
		Graphics2D g2 = (Graphics2D) g;
		explodeCirc = new Ellipse2D.Double(location.getX(), location.getY(), width, height);
		
		//Get cycle:
		if (this.cycle < 40) //was 10
		{
			if (this.cycle % 2 == 0)
				g2.setColor(Color.YELLOW);
			else
				g2.setColor(Color.RED);
		}
		
		g2.fill(explodeCirc);
		g2.draw(explodeCirc);
	}
	
	/**
	 * Update and move the explosion thru rise and fall cycles.
	 */
	public void move()
	{
		//Get cycle:
		if (this.cycle <= 20) //RISE
		{
			width += 3;
			height += 3;
			this.location.x -= 1;
			this.location.y -= 1;
			cycle += 1;
		}

		else if ((this.cycle > 20) && (this.cycle < 40)) //FALL
		{
			width -= 3;
			height -= 3;
			this.location.x += 2;
			this.location.y += 2;
			cycle += 1;
		}

		else if (this.cycle >= 40) //END
		{
			//RESET
			isDone = true;
			cycle = 1;
			width = 1;
			height = 1;
		}
	}
	
	/**
	 * @return the isDone
	 */
	public boolean isDone() {
		return isDone;
	}


	/**
	 * @param isDone the isDone to set
	 */
	public void setDone(boolean isDone) {
		this.isDone = isDone;
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
