import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/**
 * Main JFrame class that starts our Screen class,
 * which holds our game (Missile Command).
 * @author Maxwell Crawford, Nick Ashton, Kyle Anderson
 *
 */
@SuppressWarnings("serial")
public class GameWindow extends JFrame 
{
	protected Screen screen; //was backPanel
	
	private int width = 800;
	private int height = 600;
	
	
	/**
	 * The window contains the game Screen.
	 */
	public GameWindow() 
	{
		//Set base window properties:
		this.setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Missile Command");
		//this.setBackground(Color.BLACK);
		
		
		//Setup game screen/background:
		screen = new Screen(); //orig, flow layout centered
		//screen.setBackground(Color.BLACK);
		add(screen);
		
		
		//Set final properties:
		hideMouseCursor(); //show crosshair only!
		setResizable(false); //static size!
		setVisible(true);
	}
	
	/**
	 * Create a blank, transparent mouse cursor 
	 * so that only the crosshairs are shown.
	 */
	public void hideMouseCursor()
	{
		//Transparent dummy img:
		BufferedImage cursorImg = new BufferedImage(16,16, BufferedImage.TYPE_INT_ARGB);
		
		//Create the blank cursor:
		Cursor blankCursor = Toolkit.getDefaultToolkit()
				.createCustomCursor(cursorImg, new Point(0,0), "blankcursor");
		
		//Set JFrame cursor:
		this.getContentPane().setCursor(blankCursor);
	}
	
	
	/**
	 * Create the window.  
	 * @param args Ignored.
	 */
	public static void main(String[] args) 
	{
		GameWindow window = new GameWindow();
	}

}
