import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Screen, a JPanel class which starts and shows the game
 * Missile Command on the screen. 
 * Uses listeners and timers to perform main game actions.
 * @author Maxwell Crawford, Nick Ashton, Kyle Anderson
 *
 */
@SuppressWarnings("serial")
public class Screen extends JPanel implements MouseListener, MouseMotionListener, KeyListener
{
	protected Weapon weapon = new Weapon();
	protected JLabel crosshair;
	private Battery battery = new Battery();
	private Missile missile = new Missile();
	private Enemy enemy = new Enemy();
	private Explosion explosion = new Explosion();
	private Trail missileTrail;
	private JLabel scoreboard = new JLabel(); //use for small score during levels
	private JLabel bigscore = new JLabel(); //used for score screen after level
	private Sound bgmusic = new Sound("src/titleloop.wav");
	private Sound bombersound = new Sound("src/bomberinit2.wav");
	private Sound missilesound = new Sound("src/missile2.wav");
	private Sound scoresound = new Sound("src/scorescreen.wav");
	private Sound expsound = new Sound("src/explosion.wav");
	private Sound citysound = new Sound("src/panic.wav");
	
	
	private javax.swing.Timer timer;
	private javax.swing.Timer etimer;
	public BufferedImage backgroundImg;
	public BufferedImage titleImg;
	public BufferedImage batteryImg;
	public BufferedImage batterydestroyedImg; //use isLeftDead etc flags
	private ArrayList<Missile> missileList = new ArrayList<Missile>();
	private ArrayList<Trail> trailList = new ArrayList<Trail>();
	private ArrayList<Explosion> explodeList = new ArrayList<Explosion>();
	private ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
	private ArrayList<City> cityList = new ArrayList<City>();
	private ArrayList<Integer> cityLoc = new ArrayList<Integer>(); //store list of int's for easy loop structure later!, credit to Kyle A.
	
	private boolean dragFired = false; //allow to be fired once upon drag
	private boolean hasSplit = false; //flag for if missile has split!
	
	private static final int width = 800; //windows size
	private static final int height = 600;
	private static final int battery1x = 133; //battery/firing positions; cities will be between 1 and 3
	private static final int battery2x = 400;
	private static final int battery3x = 666;
	private static final int battery_y = 560; //y-limit; no firing below this point
	private static final int battery_top = 490; //where top of batteryImg truly lies
	private static final int bwidth = 100; //width of battery image
	private static final int city1x = 22;
	private static final int city2x = 203;
	private static final int city3x = 280;
	private static final int city4x = 470;
	private static final int city5x = 547;
	private static final int city6x = 733;
	private static final int city_y = 510;
	private int mouseX = 0;
	private int mouseY = 0;
	private int score = 0; //scoring!
	
	private Random gen = new Random();
	
	//GAME STATUS VARS:
	private boolean titleOver = false; //Enter key hit, title screen over, go to game
	private boolean levelOver = false; //flag for end of level, go to score screen
	private boolean scoringOver = false; //flag for end of level score screen, player goes to next level
	private boolean gameOver = false; //whether game should keep going
	
	/**
	 * Construct the screen panel for GameWindow.
	 */
	public Screen()
	{
		//Get title screen:
		titleImg = getTitleImage();
		
		//Play title music:
		bgmusic.loop();
		
		//Get Background:
		backgroundImg = getBgImage();
		
		//Get Battery img:
		batteryImg = getBatteryImage();
		batterydestroyedImg = getBatteryDestImage();
		
		//Coordinates for cities:
		cityLoc.add(city1x);
		cityLoc.add(city2x);
		cityLoc.add(city3x);
		cityLoc.add(city4x);
		cityLoc.add(city5x);
		cityLoc.add(city6x);
		
		//Allow focus:
		this.setFocusable(true);
		
		//Add listeners for mouse events:
		addMouseListener(this);
		addMouseMotionListener(this);
		
		//Add listener for KEY events:
		addKeyListener(this);
		
		//Add crosshair from Weapon:
		crosshair = weapon.getImage();
		add(crosshair);
		
		//Add scoreboard to top of screen:
		scoreboard.setForeground(Color.ORANGE);
		scoreboard.setFont(new Font("Arial", Font.BOLD, 32));
		scoreboard.setLocation(400, 25);
		scoreboard.setText(String.valueOf(score));
		scoreboard.setVisible(false);
		add(scoreboard);
		
		//Add big scoreboard, but show later:
		bigscore.setForeground(Color.ORANGE);
		bigscore.setFont(new Font("Arial", Font.BOLD, 48));
		bigscore.setLocation(360, 350);
		bigscore.setText("<html>SCORE: " + String.valueOf(score) + "<br>Game Over !</html>");
		bigscore.setVisible(false);
		add(bigscore);
		
		//Set focus:
		this.requestFocus();
	
		//Start game timer:
		timer = new javax.swing.Timer(30, new TimerListener());
		//timer.start();
		
		//Start enemy timer:
		etimer = new javax.swing.Timer(3350, new EnemyTimer());
		//etimer.start();
	}
	
	/**
	 * Retrieves the Title screen image from file.
	 * @return our title screen img.
	 */
	public BufferedImage getTitleImage()
	{
		try
		{
			titleImg = ImageIO.read(new File("src/titlebg4.png"));
		}
		
		catch (IOException e)
		{
			System.err.println("Image file for Title screen not found!");
		}
		
		return titleImg;
	}
	
	/**
	 * Retrieves the Background image from file.
	 * @return our basic background.
	 */
	public BufferedImage getBgImage()
	{
		try
		{
			backgroundImg = ImageIO.read(new File("src/back.png"));
		}
		
		catch (IOException e)
		{
			System.err.println("Image file for background not found!");
		}
		
		return backgroundImg;
	}
	
	/**
	 * Retrieves the image for Battery's, from which
	 * the missiles fire.
	 * @return the battery image, as transparent PNG.
	 */
	public BufferedImage getBatteryImage()
	{
		try
		{
			batteryImg = ImageIO.read(new File("src/missilebattery2.png"));
		}
		
		catch (IOException e)
		{
			System.err.println("Image file for battery not found!");
		}
		
		return batteryImg;
	}
	
	/**
	 * Retrieves the image for destroyed Battery's.
	 * @return the destroyed battery image, as transparent PNG.
	 */
	public BufferedImage getBatteryDestImage()
	{
		try
		{
			batterydestroyedImg = ImageIO.read(new File("src/missilebattery2d.png"));
		}
		
		catch (IOException e)
		{
			System.err.println("Image file for destroyed battery not found!");
		}
		
		return batterydestroyedImg;
	}
	

	//The timer listener for enemies firing missiles/bombs at intervals.
	private class EnemyTimer implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			//Fire from enemy/bomber once every 5 seconds, if on screen:
			//Check list of enemies for updates:
			if (!enemyList.isEmpty())
			{
				for (int i=0; i<enemyList.size(); i++)
				{
					Enemy e = enemyList.get(i);
					
					if (e.isDone()) //Enemy is done/offscreen, remove
					{
						enemyList.remove(i);
						hasSplit = false; //reset enemy split
					}
					
					else //Fire enemy missile/bombs
					{
						fireEnemy(e);		
					} //end fire enemy missile/bomb
				} //end enemy list
			} //end enemy check
			
			else //No current enemies
			{
				//Bring another enemy on screen
				enemy = new Enemy();
				addEnemy(enemy);
			}
		
		} //end enemy actions
	}
	
	//The main timer listener for all other moving actions.
	public class TimerListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			//Re-position crosshair:
			setCrosshair(mouseX, mouseY);
			
			//Check list of enemies for updates:
			if (!enemyList.isEmpty())
			{
				for (int i=0; i<enemyList.size(); i++)
				{
					Enemy e = enemyList.get(i);
					
					if (e.isDone())
					{
						enemyList.remove(i);
						hasSplit = false;
					}
					
					else
					{
						e.move();
					}
				}
			}
			
			//Check list of missiles, to update/move, or remove:
			if (!missileList.isEmpty())
			{
				
				
				for (int i=0; i<missileList.size(); i++)
				{
					Missile m = missileList.get(i);
					
					//Check for if reached target:
					if (m.isDone)
					{
						//Missile explosion!:
						explode((int)m.getLocation().getX(), (int)m.getLocation().getY());
						
						/**
						 * COLLISION CODE
						 * 
						 */
						//Player missile to enemy collision:
						if (!m.isEnemy) //ENEMY CANT SHOOT ITSELF OR OTHER ENEMIES
						{
							int mxrad = m.WIDTH; //use missile img width/height + explosion start radius(5)
							int myrad = m.HEIGHT;
							int exrad = 74;
							int eyrad = 40 + 10;
							
							for (int index=0; index<enemyList.size(); index++)
							{
								Enemy e = enemyList.get(index);
								int mx = m.getLocation().x;
								int my = m.getLocation().y;
								int ex = e.getLocation().x;
								int ey = e.getLocation().y;
								
								//Check Enemy X AND Y is within missile radius:
								if ( (mx >= ex) && 
									((ex + exrad) >= (mx + mxrad)) )
								{
									if ( (my >= ey) && 
											((ey + eyrad) >= (my + myrad)) )
									{
										collide(e); //send current Enemy to collision handler
									}
								}
								
							} //end enemy loop
						} //end player missile
						
						//Check ENEMY missile collisions:
						else if (m.isEnemy)
						{
							int mxrad = m.WIDTH+6; //use missile img width/height + explosion start radius(10)
							int myrad = m.HEIGHT+6;
							int ex = m.getLocation().x; //enemy
							int ey = m.getLocation().y;
							
							//ENEMY to CITY collision check:
							for (int ci=0; ci<cityList.size(); ci++)
							{
								City c = cityList.get(ci);
								if (!c.isDestroyed) //can't double destroy
								{
									if ( ((ex) >= (cityLoc.get(ci)-5) 
											&& (ex+mxrad) <= (cityLoc.get(ci)+c.width+5)) )
									{
										if ( ((ey) >= (city_y) 
												&& (ey+myrad) <= (city_y+c.width+5)) )
										{
											collide(c);
										}
									}
								}
								
							}
							
							//ENEMY to BATTERY collision check:				
							if (!battery.isLeftDead)
							{
								if ( ( (ex) >= (battery1x-bwidth/2))
										&& ((ex+mxrad) <= (battery1x+bwidth/2)))
								{
									if ( ( (ey) >= (battery_top))
											&& ((ey+myrad) <= (battery_top+bwidth)))
									{
										collide(ex,ey);
										battery.setLeftDead(true);
									}
								}
							}
							
							if (!battery.isCenterDead)
							{
								if ( ( (ex) >= (battery2x-bwidth/2))
										&& ((ex+mxrad) <= (battery2x+bwidth/2)))
								{
									if ( ( (ey) >= (battery_top))
											&& ((ey+myrad) <= (battery_top+bwidth)))
									{
										collide(ex,ey);
										battery.setCenterDead(true);
									}
								}
							}
							
							if (!battery.isRightDead)
							{
								if ( ( (ex) >= (battery3x-bwidth/2))
										&& ((ex+mxrad) <= (battery3x+bwidth/2)))
								{
									if ( ( (ey) >= (battery_top))
											&& ((ey+myrad) <= (battery_top+bwidth)))
									{
										collide(ex,ey);
										battery.setRightDead(true);
									}
								}
							}
							
							//Check if ALL BATTERIES ARE DEAD,
							// if so, end level!
							if ((battery.isLeftDead) && (battery.isCenterDead) 
									&& (battery.isRightDead))
							{
								levelOver = true; //flag level end
								levelEnd();
								break;
							}
							
							//ENEMY to PLAYER missile collision check:
							for (int mi=0; mi<missileList.size(); mi++)
							{
								Missile m2 = missileList.get(mi);
								
								if (!m2.isEnemy) //check each player missile!
								{
									int mx = m2.getLocation().x; //player
									int my = m2.getLocation().y;
									
									//Check Enemy missile X AND Y is within player missile radius:
									if ( ((mx) >= (ex-mxrad))
											&& ((mx) <= (ex+mxrad*2)))
									{
										if ( ((my) >= (ey-myrad))
												&& ((my) <= (ey+myrad*2)))
										{
											collide(m); //send current Enemy to collision handler
										}
									}
									
								}
							}
						} //end missile to missile collision
						
						missileList.remove(i); //remove the missile from list/screen
						trailList.remove(i); //missile done=trail done
					} //end done mode
						
					else //UPDATE mode:  trail then move missile
					{
						/**
						 * COLLISION CODE
						 * 
						 */
						//Player missile to enemy collision:
						boolean mCollided = false; //if missile collides, stop moving it and remove!!
						
						if (!m.isEnemy) //ENEMY CANT SHOOT ITSELF OR OTHER ENEMIES
						{
							int mxrad = m.WIDTH; //use missile img width/height + explosion start radius(5)
							int myrad = m.HEIGHT;
							int exrad = 74;
							int eyrad = 40 + 10;
							
							for (int index=0; index<enemyList.size(); index++)
							{
								Enemy e = enemyList.get(index);
								int mx = m.getLocation().x;
								int my = m.getLocation().y;
								int ex = e.getLocation().x;
								int ey = e.getLocation().y;
								
								//Check Enemy X AND Y is within missile radius:
								if ( (mx >= ex) && 
									((ex + exrad) >= (mx + mxrad)) )
								{
									if ( (my >= ey) && 
											((ey + eyrad) >= (my + myrad)) )
									{
										collide(e); //send current Enemy to collision handler
										mCollided = true;
									}
								}
								
							} //end enemy loop
						}
						
						//Check ENEMY missile collisions:
						else if (m.isEnemy)
						{
							//Split var's
							if (!hasSplit)
							{
								int firenum = gen.nextInt(11);
								int ystart = 230; //dont want splits above this
								int ylimit = 400; //dont want splits below this
								
								//SPLIT MISSILE
								if (firenum >= 6) //60% chance
								{
									if ((m.getLocation().y <= ylimit) 
											&& m.getLocation().y > ystart)
									{
										fireSplit(m);
										hasSplit = true;
									}
										
								}
							}
							
							
							//Collision var's
							int mxrad = m.WIDTH+6; //use missile img width/height + explosion start radius(6)
							int myrad = m.HEIGHT+6;
							int ex = m.getLocation().x; //enemy
							int ey = m.getLocation().y;
							
							//ENEMY to CITY collision check:
							for (int ci=0; ci<cityList.size(); ci++)
							{
								City c = cityList.get(ci);
								if (!c.isDestroyed) //can't double destroy!
								{
									if ( ((ex) >= (cityLoc.get(ci)-5) 
											&& (ex+mxrad) <= (cityLoc.get(ci)+c.width+5)) )
									{
										if ( ((ey) >= (city_y) 
												&& (ey+myrad) <= (city_y+c.width+5)) )
										{
											collide(c);
											mCollided = true;
										}
									}
								}
								
							}
							
							//ENEMY to BATTERY collision check:
							if (!battery.isLeftDead)
							{
								if ( ( (ex) >= (battery1x-bwidth/2))
										&& ((ex+mxrad) <= (battery1x+bwidth/2)))
								{
									if ( ( (ey) >= (battery_top))
											&& ((ey+myrad) <= (battery_top+bwidth)))
									{
										collide(ex,ey);
										mCollided = true;
										battery.setLeftDead(true);
									}
								}
							}
							
							if (!battery.isCenterDead)
							{
								if ( ( (ex) >= (battery2x-bwidth/2))
										&& ((ex+mxrad) <= (battery2x+bwidth/2)))
								{
									if ( ( (ey) >= (battery_top))
											&& ((ey+myrad) <= (battery_top+bwidth)))
									{
										collide(ex,ey);
										mCollided = true;
										battery.setCenterDead(true);
									}
								}
							}
							
							if (!battery.isRightDead)
							{
								if ( ( (ex) >= (battery3x-bwidth/2))
										&& ((ex+mxrad) <= (battery3x+bwidth/2)))
								{
									if ( ( (ey) >= (battery_top))
											&& ((ey+myrad) <= (battery_top+bwidth)))
									{
										collide(ex,ey);
										mCollided = true;
										battery.setRightDead(true);
									}
								}
							}
							
							//Check if ALL BATTERIES ARE DEAD,
							// if so, end level!
							if ((battery.isLeftDead) && (battery.isCenterDead) 
									&& (battery.isRightDead))
							{
								levelOver = true; //flag level end
								levelEnd();
								break;
							}
							
							//ENEMY to PLAYER missile collision check:
							for (int mi=0; mi<missileList.size(); mi++)
							{
								Missile m2 = missileList.get(mi);
								
								if (!m2.isEnemy) //check each player missile!
								{
									int mx = m2.getLocation().x; //player
									int my = m2.getLocation().y;
									
									//Check Enemy missile X AND Y is within player missile radius:
									if ( ((mx) >= (ex-mxrad))
											&& ((mx) <= (ex+mxrad*2)))
									{
										if ( ((my) >= (ey-myrad))
												&& ((my) <= (ey+myrad*2)))
										{
											collide(m); //send current Enemy to collision handler
											mCollided = true;
										}
									}
									
								}
							}
						} //end enemy missile collision
						
						
						//Missile is not done; need to check for collision and either move or remove:
						if (!mCollided)
						{
							missileTrail = new Trail(m.getStartPt().x, m.getStartPt().y, 
									m.getLocation().x, m.getLocation().y); //generate new trail from start to current missile loc.
							trailList.set(i, missileTrail); //replace
							m.move(); //move the current missile
						}
						
						else
						{
							m.isDone = true; //manual flag
							missileList.remove(i); //remove the missile from list/screen
							trailList.remove(i); //missile done=trail done
						}
						
					} //end else, update missile/trail/collision check
				} //end missile array
			} //end missile check
			
			//Check list of explosions for updates:
			if (!explodeList.isEmpty())
			{
				for (int i=0; i<explodeList.size(); i++)
				{
					Explosion e = explodeList.get(i);
					
					if (e.isDone())
					{
						explodeList.remove(i);
					}
					
					else
					{
						e.move();
					}
				}
			}
			
			repaint(); //force paintComponent			
		}
		
	}
	
	/**
	 * Setup end of level and display big scoring.
	 */
	private void levelEnd()
	{
		timer.stop();
		etimer.stop();
		scoreboard.setVisible(false); //hide small score
		bigscore.setVisible(true); //show big scoreboard
		enemyList.clear();
		missileList.clear();
		trailList.clear();
		scoresound.play();
	}
	
	/**
	 * Reset level parameters and variables.
	 */
	private void levelReset()
	{
//		//Reset level attributes:
//		battery.refillAmmo();
//		battery.setLeftDead(false);
//		battery.setCenterDead(false);
//		battery.setRightDead(false);
//		for (int i=0; i<cityList.size(); i++)
//		{
//			City c = cityList.get(i);
//			c.setDestroyed(false);
//		}
//		scoreboard.setVisible(true);
//		levelOver = false; //reset level
//		timer.start();
//		etimer.start();
//		
//		//Add first enemy to screen:
//		addEnemy(enemy);
	}
	
	// Prevent mouse position from going below bottom fifth of screen:
	private int fixY(int currentY)
	{
		//Check mouse position Y and replace if needed:
		int newY = currentY;
		if (newY > (height - (height/5)))
		{
			newY = (height - (height/5));
		}
		
		return newY;
	}
	
	/**
	 * Get mouse location and determine where to fire from,
	 * and where to fire the weapon.
	 * @param mouse the MouseEvent that gives position of mouse.
	 */
	public void fireWeapon(MouseEvent mouse)
	{
		Missile missile = new Missile();
		
		//Check mouse position Y (bottom of screen):
		mouseY = fixY(mouse.getY());
		
		//Check mouse position X (thirds of screen):
		if (mouse.getX() <= (width / 3.0))
		{
			battery.setLeft(true);
			if (!battery.leftEmpty)
			{
				battery.fireLeft();
				missile = new Missile(new Point(battery1x,battery_y), new Point(mouse.getX(), mouseY));
				missileTrail = new Trail(missile.getStartPt().x, missile.getStartPt().y, 
						missile.getLocation().x, missile.getLocation().y); //generate new trail from start to current missile loc.
				addMissile(missile);
				addTrail(missileTrail);
				
				//Play missile sound:
				missilesound.play();
			}
		}

		else if ((mouse.getX() > (width / 3.0)) && 
				(mouse.getX() <= (2.0 * width / 3.0)) )
		{
			battery.setCenter(true);
			if (!battery.centerEmpty)
			{
				battery.fireCenter();
				missile = new Missile(new Point(battery2x,battery_y), new Point(mouse.getX(), mouseY), true); //faster
				missileTrail = new Trail(missile.getStartPt().x, missile.getStartPt().y, 
						missile.getLocation().x, missile.getLocation().y); //generate new trail from start to current missile loc.
				addMissile(missile);
				addTrail(missileTrail);
				
				//Play missile sound:
				missilesound.play();
			}
		}

		else if (mouse.getX() > (2.0 * width / 3.0))
		{
			battery.setRight(true);
			if (!battery.rightEmpty)
			{
				battery.fireRight();
				missile = new Missile(new Point(battery3x,battery_y), new Point(mouse.getX(), mouseY));
				missileTrail = new Trail(missile.getStartPt().x, missile.getStartPt().y, 
						missile.getLocation().x, missile.getLocation().y); //generate new trail from start to current missile loc.
				addMissile(missile);
				addTrail(missileTrail);
				
				//Play missile sound:
				missilesound.play();
			}
		}

	}
	
	/**
	 * Fire an "enemy" missile from the given Enemy,
	 * which shoots at one of the 3 batteries.
	 * The battery chosen is randomly generated (1-3).
	 * @param e the current enemy which fires the bomb/missile
	 */
	public void fireEnemy(Enemy e)
	{
		int target = 1 + gen.nextInt(6); //1-3 are battery hits, else random miss
		Point targetPt = new Point(0,0);
		Point startPt = new Point(e.getLocation().x + 37, e.getLocation().y + 40); //enemy img = 74x40
		Missile missile = new Missile();
		
		if (target == 1) //BATTERY LEFT
			targetPt = new Point(battery1x,battery_y);
		
		else if (target == 2) //BATTERY CENTER
			targetPt = new Point(battery2x,battery_y);
		
		else if (target == 3) //BATTERY RIGHT
			targetPt = new Point(battery3x,battery_y);
		else
			targetPt = new Point(100 + gen.nextInt(650), battery_y); //random miss!
		
		missile = new Missile(startPt, targetPt); //shoot from below bomber to battery!
		missileTrail = new Trail(missile.getStartPt().x, missile.getStartPt().y, 
				missile.getLocation().x, missile.getLocation().y); //generate new trail from start to current missile loc.
		missile.setEnemy(true);
		addMissile(missile);
		addTrail(missileTrail);
	}
	
	/**
	 * Get current enemy missile and split another missile off,
	 * which goes to another target.
	 * @param m enemy missile from which new missile splits off of.
	 */
	private void fireSplit(Missile m)
	{
		Point origTarget = new Point(m.getEndPt());
		int ox = origTarget.x;
		int oy = origTarget.y;
		int newx = 0;
		if (ox < 400)
			newx = width - ox;
		else
			newx = ox - 400;
		Point targetPt = new Point(newx,oy); //mirror x
		Point startPt = new Point(m.getLocation().x, m.getLocation().y); //enemy img = 74x40
		Missile missile = new Missile(startPt, targetPt); //from orig enemy missile to mirrored x target
		missileTrail = new Trail(missile.getStartPt().x, missile.getStartPt().y, 
				missile.getLocation().x, missile.getLocation().y); //generate new trail from start to current missile loc.
		missile.setEnemy(true);
		addMissile(missile);
		addTrail(missileTrail);
	}
	
	// Set of methods for adding obj's to lists
	private void addMissile(Missile missile)
	{
		//Set missile properties:
		double newAngle = missile.getNewAngle();
		missile.setAngle(newAngle);
		missileList.add(missile);
	}
	
	private void addTrail(Trail trail)
	{
		trailList.add(trail);
	}
	
	private void addEnemy(Enemy e)
	{
		enemyList.add(e);
		bombersound.play(); //play bomber sound
	}
	
	private void addCity(City c)
	{
		cityList.add(c);
	}
	
	
	//Set of mouse event listeners:
	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		//Fire weapon, if mouse is in game window:
		if (weapon.isMouseInWindow())
		{
			if (titleOver)
				fireWeapon(arg0);
			mouseX = arg0.getX();
			mouseY = arg0.getY();
			
			//Prevent shooting the bottom of the screen:
			mouseY = fixY(arg0.getY());
			
			setCrosshair(mouseX, mouseY);
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		if ((timer != null) && (titleOver) && (!levelOver))
		{
			timer.start(); //resume moving gameplay
			etimer.start();
		}
			
		weapon.setMouseInWindow(true);
		crosshair.setVisible(true);
		this.requestFocus(); //force focus back on window
	}

	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		if ((timer != null) && (!levelOver))
		{
			timer.stop(); //pause moving gameplay
			etimer.stop();
		}
			
		weapon.setMouseInWindow(false);
		crosshair.setVisible(false);
	}

	@Override
	public void mousePressed(MouseEvent arg0) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		dragFired = false; //release flag upon mouse btn release
	}
		
	

	@Override
	public void mouseDragged(MouseEvent arg0) 
	{
		//Allow firing ONCE during drag, then set flag:
		if (!dragFired)
		{
			//Make sure mouse is in window:
			if (weapon.isMouseInWindow())
			{
				if (titleOver)
					fireWeapon(arg0);
				dragFired = true; //set drag flag
				
				mouseX = arg0.getX();
				mouseY = arg0.getY();
				
				//Prevent shooting the bottom of the screen:
				mouseY = fixY(arg0.getY());
				
				setCrosshair(mouseX, mouseY);
			}
		} //end drag fire

	}

	@Override
	public void mouseMoved(MouseEvent arg0) 
	{
		if (weapon.isMouseInWindow())
		{
			mouseX = arg0.getX();
			mouseY = arg0.getY();
			
			//Prevent shooting the bottom of the screen:
			mouseY = fixY(arg0.getY());
			
			setCrosshair(mouseX, mouseY);
		}

	}
	
	//Set of KeyListener methods (TEST)
	@Override
	public void keyPressed(KeyEvent e) 
	{
		int keycode = e.getKeyCode();
		
		//Check keyboard presses:
		if (keycode == KeyEvent.VK_ESCAPE)
		{
			//Exit program:
			System.exit(0); //ROUGH EXIT, may need to replace! TEST
		}
		
		if (keycode == KeyEvent.VK_R)
		{
			//Refill all ammo (TEST):
			battery.refillAmmo();
		}
		
		if (keycode == KeyEvent.VK_BACK_SPACE)
		{
			//Bring back enemy (TEST):
			enemy = new Enemy();
			addEnemy(enemy);
		}
		
		if (keycode == KeyEvent.VK_ENTER)
		{
			//Enter game from Title Screen:
			if (!titleOver)
			{
				titleOver = true; //flag!
				scoreboard.setVisible(true);
				timer.start();
				etimer.start();
				
				//Add first enemy to screen:
				addEnemy(enemy);
				
				//Add cities:
				for (int i=0; i<cityLoc.size(); i++)
				{
					int x = cityLoc.get(i);
					City city = new City(new Point(x, city_y));
					addCity(city);
				}
			}
			
			//Re-enter game/next level:
//			if ((titleOver) && (levelOver))
//			{
//				//Reset level attributes:
//				battery.refillAmmo();
//				battery.setLeftDead(false);
//				battery.setCenterDead(false);
//				battery.setRightDead(false);
//				for (int i=0; i<cityList.size(); i++)
//				{
//					City c = cityList.get(i);
//					c.setDestroyed(false);
//				}
//				scoreboard.setVisible(true);
//				levelOver = false; //reset level
//				timer.start();
//				etimer.start();
//				
//				//Add first enemy to screen:
//				addEnemy(enemy);
//			}
			
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	
	//Re-set crosshair position:
	private void setCrosshair(int posx, int posy)
	{
		int xOffset = 21;
		int yOffset = 23;
		crosshair.setLocation(posx-xOffset, posy-yOffset); //use radial offset for perfect positioning
	}
	
	//Explode at location:
	private void explode(int x, int y)
	{
		Explosion ex = new Explosion();
		ex.explode(new Point(x-7,y-11)); //where missile ends
		explodeList.add(ex);
		
		//Play explosion sound:
		expsound.play();
	}
	
	// -- SET OF COLLISION METHODS --
	//NOTE: Points need to be added,
	// depending on what enemy was destroyed:
	private void collide(Enemy e)
	{
		//Add points to score for enemy:
		// --
		score += 250; //bomber worth 250 pts
		scoreboard.setText(String.valueOf(score));
		
		//Create multi-explosion here!
		int ex = e.getLocation().x + gen.nextInt(74);
		int ey = e.getLocation().y + gen.nextInt(40);
		explode(ex, ey);
		explode(e.getLocation().x+37, e.getLocation().y+20);
		ex = e.getLocation().x + gen.nextInt(74);
		ey = e.getLocation().y + gen.nextInt(40);
		explode(ex, ey);
		
		//Remove enemy from screen:
		enemyList.remove(e);
		
	}
	
	public void collide(Missile m) //collision with enemy missile
	{
		//Add points to score for enemy missile/bomb:
		// --
		score += 50; //enemy missile worth 50 pts
		scoreboard.setText(String.valueOf(score));
		
		//Create multi-explosion here!
		int mx = m.getLocation().x + gen.nextInt(74);
		int my = m.getLocation().y + gen.nextInt(40);
		explode(mx, my);
		explode(m.getLocation().x+37, m.getLocation().y+20);
		mx = m.getLocation().x + gen.nextInt(74);
		my = m.getLocation().y + gen.nextInt(40);
		explode(mx, my);
		
		//MISSILE WILL BE REMOVED IN LOOP
	}
	
	public void collide(City c) //collision with player city!
	{
		//Create dual explosion:
		int cx = c.getLocation().x + gen.nextInt(45);
		int cy = c.getLocation().y + gen.nextInt(45);
		explode(cx, cy);
		explode(c.getLocation().x, c.getLocation().y);
		
		c.setDestroyed(true); //flag city to draw destroyed vers.
		
		//Play panic sounds!
		citysound.play();
	}
	
	public void collide(int x, int y) //collision with battery position!
	{
		//Create multi-explosion here!
		int mx = (x-bwidth/2) + gen.nextInt(bwidth);
		int my = battery_top + gen.nextInt(bwidth);
		explode(mx, my);
		mx = (x-bwidth/2) + gen.nextInt(bwidth);
		my = battery_top + gen.nextInt(bwidth);
		explode(mx, my);
		mx = (x-bwidth/2) + gen.nextInt(bwidth);
		my = battery_top + gen.nextInt(bwidth);
		explode(mx, my);
	}
	
	//Manual override of paintcomponent!
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		//Draw title screen if flag value false,
		//else draw everything else:
		if (!titleOver)
			g.drawImage(titleImg, 0,0,800,600,null);
		else
		{
			//Draw background img:
			g.drawImage(backgroundImg, 0,0, 800, 600,null);
			
			//Draw batteries:
			if (!battery.isLeftDead)
				g.drawImage(batteryImg, battery1x-50,490, 100, 100,null); //133,400,666 are missile start positions! (50=radius of img)
			else
				g.drawImage(batterydestroyedImg, battery1x-50,490, 100, 100,null); //133,400,666 are missile start positions! (50=radius of img)
			if (!battery.isCenterDead)
				g.drawImage(batteryImg, battery2x-50,490, 100, 100,null);
			else
				g.drawImage(batterydestroyedImg, battery2x-50,490, 100, 100,null);
			if (!battery.isRightDead)
				g.drawImage(batteryImg, battery3x-50,490, 100, 100,null);
			else
				g.drawImage(batterydestroyedImg, battery3x-50,490, 100, 100,null);
				
			
			//Draw Cities:
			if (!cityList.isEmpty())
			{
				for (City c : cityList)
				{
					c.draw(g);
				}
			}
			
			//Draw and update missiles: ...
			if (!missileList.isEmpty())
			{
				for (Missile m : missileList)
				{
					m.draw(g);
				}

			}
			
			//Draw and update missile trails:
			if (!trailList.isEmpty())
			{
				for (Trail t : trailList)
				{
					t.draw(g);
				}
			}
			
			//Draw and update explosions (let finish even at level end):
			if (!explodeList.isEmpty())
			{
				for (Explosion e : explodeList)
				{
					e.draw(g);
				}
			}
			
			//Draw and update enemies:
			if (!enemyList.isEmpty())
			{
				for (Enemy e : enemyList)
				{
					e.draw(g);
				}
			}
		}
		
	
	} //end paint
	
}
