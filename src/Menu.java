import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Menu extends JPanel implements Runnable
{
	static GameBoard mainBoard;
	public int tag;
	public static Point mxy = null;
	
	//Sign
	HImage sign;
	//Buttons
	public MenuItem[] Buttons;
	private int ButtLength = 0;
	
	//Active?
	public boolean active = false;
	
	//Last Hovered
	MenuItem itemLast = null;
	
	//Last Menu Clicked (for back button)
	public static int lastMenu = 0;
	public static String lastThing;
	
	private String lastAction = null;
	private int lastButton;
	
	//Scrolling
	private int scroll = 0;
	private int mScroll = 0;
	private int bottom = 0;
	private int[] butty;

	//Opening and closing
	public boolean slidingUp = false;
	public boolean slidingDown = false;
	public boolean catchingUp = false;
	public boolean catchingDown = false;
	
	private int slideSpeed = 0;
	private static int accelRate = 2;
	private boolean setYet = false;
	
	//Sound Files
	static File Click1;
	static File Click2;
	static File MenuSlide;

	//Constructor for normal menus
	public Menu (HImage sign, final MenuItem[] InButtons, final int tag, final GameBoard mainBoard)
	{
		//Transferring Arguments
		this.tag = tag;
		this.sign = sign;
		this.Buttons = InButtons;
		Menu.mainBoard = mainBoard;
		
		this.ButtLength = this.Buttons.length;
		this.butty = new int[this.ButtLength];
		
		//Aligning sign
		this.sign.xPos = (GameBoard.boardWidth/2) - (this.sign.width/2);
		this.sign.yPos = 0;
		//Aligning first button
		this.Buttons[0].xPos = (GameBoard.boardWidth/2) - (this.Buttons[0].width/2);
		this.Buttons[0].yPos = this.sign.yPos + this.sign.height;
		this.butty[0] = this.Buttons[0].yPos;
		
		//Aligning next buttons
		for (int i = 1; i < this.ButtLength; i++)
		{
			this.Buttons[i].xPos = (GameBoard.boardWidth/2) - (this.Buttons[i].width/2);
			this.Buttons[i].yPos = this.Buttons[i-1].yPos + this.Buttons[i-1].height;
			
			//Assigning butt y
			this.butty[i] = this.Buttons[i].yPos;
		}
		
		//Getting Sounds
		try
		{
	        Click1 = new File("Resource/Audio/Beeps/Click1.wav");
	        Click2 = new File("Resource/Audio/Beeps/Click2.wav");
	        MenuSlide = new File("Resource/Audio/MenuSounds/MenuSlide2.wav");
		}
	    catch (Exception e)
	    {
	    	System.out.println("Sound file not found: Menu: " + e.toString());
	    }
		
		//Mouse e mouse e
		this.addMouseMotionListener(new MouseAdapter()
		{
			@Override
			public void mouseMoved(MouseEvent e)
			{
				Point xy = e.getPoint();
				Menu.mxy = xy;

				boolean newButt = false;
				
				for (int i = 0; i < ButtLength; i++)
				{
					if 	(Buttons[i].getBounds().contains(xy))
					{
						updateMenuTest (Buttons[i]);
						newButt = true;
						break;
					}
				}
				
				if (newButt == false)
				{updateMenuTest(null);}
				
				scroll(xy);
			}
		});
		
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				Point xy = e.getPoint();

				//Choosing Menu Item
				for (int i = 0; i < ButtLength; i ++)
				{
					if 	(Buttons[i].getBounds().contains(xy))
					{
						startAction(i, Buttons[i].action);
					}	
				}
			}
		});
	}
	
	//Allowing other classes to see action
	public void sendAction(int i, String action)
	{this.startAction(i, action);}
	
	private void startAction (int i, String action)
	{
		if (this.active)
		{
			this.lastAction = action;
			this.lastButton = i;
			
			//anti highlighting
			updateMenuTest(null);
			
			//Decision structure
			if 		(action == "LaunchMenu")
			{
				this.slidingUp = true;
				this.active = false;
				
				GameBoard.playSound(Click1, 0);
			}
			
			else if (action == "LaunchArcade1")
			{
				this.slidingUp = true;
				this.active = false;
				GameBoard.playSound(Click1, 0);
			}
			
			else if (action == "LaunchHelp")
			{
				this.slidingUp = true;
				this.active = false;
				GameBoard.playSound(Click1, 0);
			}
			
			else if (action == "ExitGame")
			{
				this.slidingUp = true;
				this.active = false;
				GameBoard.playSound(Click2, 0);
			}
			
			
			else if (action == "ResumeGame")
			{
				this.slidingUp = true;
				this.active = false;
				
				GameBoard.playSound(Click1, 0);
			}
			
			
			else if (action == "RestartGame")
			{GameBoard.TheSoloSprites.restartGame(); GameBoard.playSound(Click2, 0);}
			
			
			else if (action == "QuitGame")
			{
				this.slidingUp = true;
				GameBoard.playSound(Click2, 0);
			}
			
			
			else if (action == "LaunchAbout")
			{
				this.slidingUp = true;
				this.active = false;

				GameBoard.playSound(Click1, 0);
			}
			

			else if (Buttons[i].action == "Back")
			{
				this.slidingDown = true;
				this.active = false;
				
				GameBoard.playSound(Click2, 0);
			}
			
			//In case I missed a button action
			else
			{
				System.out.println("Inactive Button: " + action);
			}
		}
	}
	
	//Finishing action
	public void finishAction ()
	{
		int i = this.lastButton;
		String action = this.lastAction;
	
		//anti highlighting
		updateMenuTest(null);
		
		//Decision structure
		if 		(action == "LaunchMenu")
		{
			int intmenu = Buttons[this.lastButton].intmenu;
			
			//Launching Next Menu
			if (this.lastAction == "LaunchMenu")
			{GameBoard.Menus[intmenu].catchingUp = true; GameBoard.Menus[intmenu].catchUp(); mainBoard.LaunchMenu(intmenu);}
			
		}
		
		else if (action == "LaunchArcade1")
		{mainBoard.LaunchNewSolo();}
		
		else if (action == "ExitGame")
		{mainBoard.ExitGame();}
		
		else if (action == "ResumeGame")
		{mainBoard.ResumeGame();}
		
		else if (action == "RestartGame")
		{GameBoard.TheSoloSprites.restartGame();}
		
		else if (action == "QuitGame")
		{
			mainBoard.QuitGame();
			GameBoard.Menus[0].catchingUp = true;
			GameBoard.Menus[0].catchUp();
		}
		
		else if (action == "LaunchAbout")
		{mainBoard.LaunchAbout();}
		
		else if (action == "LaunchHelp")
		{mainBoard.LaunchHelp();}

		else if (Buttons[i].action == "Back")
		{
			//Go Back
			if (Menu.lastThing == "Menu")
			{GameBoard.Menus[lastMenu].catchingDown = true; GameBoard.Menus[lastMenu].catchDown(); mainBoard.LaunchMenu(lastMenu);}
			
			if (Menu.lastThing == "Shop")
			{
				mainBoard.CloseMenus();
				mainBoard.ReturnShop();
			}
			
		}
		
		//In case I missed a button action
		else
		{
			System.out.println("Inactive Button: " + action);
			GameBoard.playSound(Click2, 0);
		}
	}
	
	//Re activating a menu
	public void reset()
	{
		//Aligning sign
		this.sign.xPos = (GameBoard.boardWidth/2) - (this.sign.width/2);
		this.sign.yPos = 0;
		//Aligning first button
		this.Buttons[0].xPos = (GameBoard.boardWidth/2) - (this.Buttons[0].width/2);
		this.Buttons[0].yPos = this.sign.yPos + this.sign.height;
		this.butty[0] = this.Buttons[0].yPos;
		
		//Aligning next buttons
		for (int i = 1; i < this.ButtLength; i++)
		{
			this.Buttons[i].xPos = (GameBoard.boardWidth/2) - (this.Buttons[i].width/2);
			this.Buttons[i].yPos = this.Buttons[i-1].yPos + this.Buttons[i-1].height;
			
			//Assigning butt y
			this.butty[i] = this.Buttons[i].yPos;
		}
		
		this.slideSpeed = 0;
		this.mScroll = 0;
		
		this.slidingUp = false;
		this.slidingDown = false;
		this.catchingUp = false;
		this.catchingDown = false;
		
		this.setYet = false;
	}

	public void updateMenuTest (MenuItem item)
	{
		if (this.active)
		{
			if (this.itemLast != item)
			{
				this.itemLast = item;
				
				//anti highlighting buttons
				for (int i = 0; i < this.ButtLength; i++)
				{this.Buttons[i].unHighlight();}
				
				//Highlighting next
				if (this.itemLast != null)
				{
					GameBoard.playSound(MenuSlide, 0);
					this.itemLast.highlight();
				}
				this.repaint();
			}
		}
	}
	
	public void scroll(Point xy)
	{
		if (!(this.slidingDown || this.slidingUp || this.catchingUp || this.catchingDown))
		{
			// y point
			int y;
			
			if (xy == null)
			{y = Menu.mxy.y;}
			
			else
			{y = xy.y;}
			
			//Range
			int h = GameBoard.boardHeight;
			double mh = this.sign.height;
			double r = h - mh;
			
			//Bottom button, bottom y
			int bb = this.butty[this.ButtLength - 1];
			this.bottom = bb + this.Buttons[this.ButtLength - 1].height;

			//Exceeding range top bound
			if (y < mh)
			{this.scroll = 0;}
			//Exceeding range bottom bound
			else if (y > mh + r)
			{this.scroll = this.bottom - h;}
			//Middle range
			else
			{
				//take y to lower bound
				double y2 = y - mh;
				double perc = y2/r;
				this.scroll = (int)(perc * (this.bottom - h));
			}
			
			//Adjusting Buttons
			for (int i = 0; i < this.ButtLength; i ++)
			{
				this.Buttons[i].yPos = this.butty[i] - this.scroll;
			}

			mainBoard.repaint();
		}
	}
	
	//Sliding to next menu
	public void slideUp()
	{
		this.slideSpeed += Menu.accelRate;
		
		this.scroll += this.slideSpeed;
		this.mScroll += this.slideSpeed;
		
		//Adjusting Buttons
		for (int i = 0; i < this.ButtLength; i ++)
		{
			this.Buttons[i].yPos = this.butty[i] - this.scroll;
			this.sign.yPos = 0 - this.mScroll;
		}
		
		//Ending slide up
		if (this.scroll >= this.bottom)
		{
			this.slidingUp = false;
			mainBoard.CloseMenu(this.tag);
			
			this.finishAction();
		}

		mainBoard.repaint();
	}
	
	//Sliding down to previous menu
	public void slideDown()
	{
		this.slideSpeed += Menu.accelRate;
		
		this.scroll -= this.slideSpeed;
		this.mScroll -= this.slideSpeed;

		//Adjusting Buttons
		for (int i = 0; i < this.ButtLength; i ++)
		{
			this.Buttons[i].yPos = this.butty[i] - this.scroll;
			this.sign.yPos = 0 - this.mScroll;
		}
		
		//Ending slide Down
		if (this.sign.yPos >= GameBoard.boardHeight)
		{
			this.finishAction();
			this.slidingDown= false;
			mainBoard.CloseMenu(this.tag);
		}

		mainBoard.repaint();
	}
	
	//Setting up for catch up
	public void setCatchUp ()
	{
		double time = Math.sqrt((2 * GameBoard.boardHeight) / Menu.accelRate);

		this.slideSpeed = (int)(time * Menu.accelRate);
		
		this.catchingUp = false;
		this.scroll(null);
		this.catchingUp = true;
		
		this.mScroll = GameBoard.boardHeight;
		this.scroll = GameBoard.boardHeight - this.scroll;
		
		this.scroll -= this.slideSpeed;
		this.mScroll -= this.slideSpeed;
	}
	
	//Sliding on screen
	public void catchUp()
	{
		if (this.setYet == false)
		{this.setCatchUp(); this.setYet = true;}

		this.slideSpeed -= Menu.accelRate;
		
		this.scroll -= this.slideSpeed;
		this.mScroll -= this.slideSpeed;
		
		//Adjusting Buttons
		for (int i = 0; i < this.ButtLength; i ++)
		{
			this.Buttons[i].yPos = this.butty[i] + this.scroll;
			this.sign.yPos = 0 + this.mScroll;
		}
		
		//Ending catch up
		if (this.slideSpeed <= 0)
		{
			this.catchingUp = false;
			this.mScroll = 0;
			this.sign.yPos = 0;

			this.reset();
			this.scroll(null);
		}

		mainBoard.repaint();
	}
	
	//Setting up for catch Down
	public void setCatchDown ()
	{
		double time = Math.sqrt((2 * this.bottom) / Menu.accelRate);

		this.slideSpeed = (int)(time * Menu.accelRate);
		
		this.catchingDown = false;
		this.scroll(null);
		this.catchingDown = true;
		
		this.mScroll = -this.bottom;
		this.scroll = -this.scroll - this.bottom;
		
		this.scroll += this.slideSpeed;
		this.mScroll += this.slideSpeed;
	}
	
	//Sliding on screen
	public void catchDown()
	{
		if (this.setYet == false)
		{this.setCatchDown(); this.setYet = true;}

		this.slideSpeed -= Menu.accelRate;
		
		this.scroll += this.slideSpeed;
		this.mScroll += this.slideSpeed;
		
		//Adjusting Buttons
		for (int i = 0; i < this.ButtLength; i ++)
		{
			this.Buttons[i].yPos = this.butty[i] + this.scroll;
			this.sign.yPos = 0 + this.mScroll;
		}
		
		//Ending catch up
		if (this.slideSpeed <= 0)
		{
			this.catchingDown = false;
			this.mScroll = 0;
			this.sign.yPos = 0;
			
			this.reset();
			this.scroll(null);
		}

		mainBoard.repaint();
	}

	//Paint
	public void paint(Graphics g)
	{
		Graphics2D Menu = (Graphics2D)g;
		Menu.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//Drawing buttons
		for (int i = 0; i < this.ButtLength; i ++)
		{Menu.drawImage(this.Buttons[i].image, this.Buttons[i].xPos, this.Buttons[i].yPos, this.Buttons[i].width, this.Buttons[i].height, null);}
		
		//Drawing Sign
		Menu.drawImage(this.sign.image, this.sign.xPos, this.sign.yPos, this.sign.width, this.sign.height, null);
		
		Menu.setColor(Color.MAGENTA);
		//Borders
		if (GameBoard.sBorders)
		{
			Menu.drawLine(0, GameBoard.boardHeight, GameBoard.boardWidth, GameBoard.boardHeight);
		}
		
		//HitBoxes
		if (GameBoard.sHitBoxes)
		{
			//Drawing buttons
			for (int i = 0; i < this.ButtLength; i ++)
			{Menu.draw(this.Buttons[i].getBounds());}
		}
	}
	
	//Running to slide
	@Override
	public void run()
	{
		if (this.slidingUp)
		{this.slideUp();}
		
		else if (this.slidingDown)
		{this.slideDown();}
		
		else if (this.catchingUp)
		{this.catchUp();}
		
		else if (this.catchingDown)
		{this.catchDown();}
	}
}
