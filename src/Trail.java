import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

/**
 * Trail object, which is a yellow line that follows
 * behind fired missiles (player and enemy).
 * @author Maxwell Crawford
 *
 */
public class Trail 
{
	private Line2D.Double line = new Line2D.Double(0, 0, 0, 0);
	public int startX = 0;
	public int startY = 0;
	public int endX = 0;
	public int endY = 0;
	
	/**
	 * Constructor for missile Trail with start and 
	 * end points specified.
	 * @param sx Start x.
	 * @param sy Start y.
	 * @param ex End x.
	 * @param ey End y.
	 */
	public Trail(int sx, int sy, int ex, int ey)
	{
		this.startX = sx;
		this.startY = sy;
		this.endX = ex;
		this.endY = ey;
		line = new Line2D.Double(sx, sy, ex, ey);
	}
	
	/**
	 * Draw the line, with yellow color.
	 * @param g the graphics object to be redrawn
	 */
	public void draw(Graphics g)
	{
		//Gather graphics obj:
		Graphics2D g2 = (Graphics2D) g;
		
		//Set line color:
		g2.setColor(Color.YELLOW);
		
		//Draw line:
		g2.drawLine(startX, startY, endX, endY);
	}

	//-- Getters/Setters --
	/**
	 * @return the startX
	 */
	public int getStartX() {
		return startX;
	}

	/**
	 * @param startX the startX to set
	 */
	public void setStartX(int startX) {
		this.startX = startX;
	}

	/**
	 * @return the startY
	 */
	public int getStartY() {
		return startY;
	}

	/**
	 * @param startY the startY to set
	 */
	public void setStartY(int startY) {
		this.startY = startY;
	}

	/**
	 * @return the endX
	 */
	public int getEndX() {
		return endX;
	}

	/**
	 * @param endX the endX to set
	 */
	public void setEndX(int endX) {
		this.endX = endX;
	}

	/**
	 * @return the endY
	 */
	public int getEndY() {
		return endY;
	}

	/**
	 * @param endY the endY to set
	 */
	public void setEndY(int endY) {
		this.endY = endY;
	}
	
	
}
