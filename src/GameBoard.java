import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameBoard extends JFrame
{
	//Board Data_______________________________________________________________
	public static int boardWidth = 1720;
	public static int boardHeight = 755;// - 150;
	
	public static String gameMode;
	public static String audioResource = "Resource";
	public static String imageResource = "Resource";

	//Utilities
	static Random gen = new Random();
	public static boolean paused = false;

	//Settings
	public static boolean muting = true;
	public static String Res = "Resource";
	
	//Player 1
	public static boolean W = false;
	public static boolean A = false;
	public static boolean S = false;
	public static boolean D = false;
	
	public static boolean Space = false;
	public static boolean LShift = false;
	public static boolean Caps = false;

	//Miscellaneous
	public static boolean R = false;
	public static boolean P = false;

	//Panels
	public static SoloPanel TheSoloPanel = null;
	public static SoloSprites TheSoloSprites = null;
	
	//Menus
	public static Menu[] Menus;
	public static HImage[] MenuSigns;
	public static MenuItem[][] MenuButtons = new MenuItem[3][];
	
	//Developer Options
	public static boolean sHitBoxes = false;
	public static boolean sBorders = false;
	
	public static boolean sSprites = true;

	//Main
	public static void main(String[] args)
	{
		new GameBoard();
	}

	//Constructor for the whole Game (With Key Events)
	public GameBoard ()
	{
		this.setSize(boardWidth, boardHeight + 23);
		this.setResizable(false);
		this.setTitle("Asteroid Retro");
		this.setBackground(Color.BLACK);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Setting up main menu
		ConstructMenus();
		Menus[0].active = true;

		this.add(Menus[0]);
		this.setVisible(true);

		GameBoard.gameMode = "MainMenu";
		
		this.setFocusable(true);

	    
		addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				int key = e.getKeyCode();
				int location = e.getKeyLocation();
				
				//Muting
				if (key == KeyEvent.VK_M)
				{
					if (GameBoard.muting == true)
					{GameBoard.muting = false;}
					
					else
					{GameBoard.muting = true;}
				}
				
				if (GameBoard.gameMode == "Solo")
				{
					if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP)
					{GameBoard.W = true;}
					
					if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT)
					{GameBoard.A = true;}
					
					if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN)
					{GameBoard.S = true;}
					
					if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT)
					{GameBoard.D = true;}
					
					if (key == KeyEvent.VK_SPACE)
					{GameBoard.Space = true;}
					
					if (key == KeyEvent.VK_SHIFT && location == KeyEvent.KEY_LOCATION_LEFT && GameBoard.Caps == false)
					{GameBoard.LShift = true;}
					
					if (key == KeyEvent.VK_CAPS_LOCK)
					{GameBoard.LShift = true; GameBoard.Caps = true;}
					
					//Selectors
					if (key == KeyEvent.VK_1)
					{TheSoloSprites.setSelection(0);}
					
					if (key == KeyEvent.VK_2)
					{TheSoloSprites.setSelection(1);}
					
					if (key == KeyEvent.VK_3)
					{TheSoloSprites.setSelection(2);}
					
					if (key == KeyEvent.VK_4)
					{TheSoloSprites.setSelection(3);}
					
					if (key == KeyEvent.VK_5)
					{TheSoloSprites.setSelection(4);}
					
					//MOAR Selectors!
//					if (key == KeyEvent.VK_LEFT)
//					{SoloSprites.cycleSelection(-1);}
//					
//					if (key == KeyEvent.VK_RIGHT)
//					{SoloSprites.cycleSelection(1);}
					
					//Pausing
					if (key == KeyEvent.VK_P)
					{
						if (GameBoard.paused)
						{GameBoard.paused = false;}
						else
						{GameBoard.paused = true;}
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				int key = e.getKeyCode();
				int location = e.getKeyLocation();
				
				if (GameBoard.gameMode == "Solo")
				{
					if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP)
					{GameBoard.W = false;}
					
					if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT)
					{GameBoard.A = false;}
					
					if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN)
					{GameBoard.S = false;}
					
					if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT)
					{GameBoard.D = false;}
					
					if (key == KeyEvent.VK_SPACE)
					{GameBoard.Space = false;}
					
					if (key == KeyEvent.VK_SHIFT && location == KeyEvent.KEY_LOCATION_LEFT && GameBoard.Caps == false)
					{GameBoard.LShift = false;}
					
					if (key == KeyEvent.VK_CAPS_LOCK)
					{GameBoard.LShift = false; GameBoard.Caps = false;}
				}
			}

			@Override
			public void keyTyped(KeyEvent e)
			{

			}
		});
	}
	
	//Constructing All Menus
	public void ConstructMenus()
	{
		//Signs
		HImage[] TempSigns =
			{
				new HImage("Resource2/Images/MenuPics/Logo1On"),
				new HImage("Resource2/Images/MenuPics/SettingsOn"),
				new HImage("Resource2/Images/MenuPics/PausedOn"),
				new HImage("Resource2/Images/MenuPics/GameOverOn")
			};

		MenuSigns = TempSigns;
		
		//Buttons
		MenuItem[][] TempButts = 
			{
				//Main Menu - 0
				{
					new MenuItem("Play"	, "LaunchArcade1"),
					//new MenuItem("Settings"	, "LaunchMenu", 1),
					new MenuItem("Help"		, "LaunchHelp"),
					new MenuItem("About"	, "LaunchAbout"),
					new MenuItem("Quit"		, "ExitGame")
				},
				//Settings - 1
				{
					new MenuItem("Back"		, "Back"),
					new MenuItem("Audio"	, "Unknown"),
					new MenuItem("Video"	, "Unknown"),
					new MenuItem("Textures"	, "Unknown"),
					new MenuItem("Controls"	, "Unknown"),
					
				},
				//Pause - 2 (ArcadeMode)
				{
					new MenuItem("Resume", "ResumeGame"),
					//new MenuItem("Settings", "LaunchMenu", 1),
					new MenuItem("Restart", "RestartGame"),
					new MenuItem("Quit", "QuitGame"),
				},
				//GameOver - 3 (AcradeMode)
				{
					new MenuItem("Restart", "RestartGame"),
					new MenuItem("Quit", "QuitGame"),
				}
			};
		
		MenuButtons = TempButts;
		
		//Menus
		Menu[] TempMenu = 
			{
				new Menu(MenuSigns[0], MenuButtons[0], 0, this),
				new Menu(MenuSigns[1], MenuButtons[1], 1, this),
				new Menu(MenuSigns[2], MenuButtons[2], 2, this),
				new Menu(MenuSigns[3], MenuButtons[3], 3, this)
			};
		
		Menus = TempMenu;
		
		//Finishing Buttons
		MenuButtons[0][2].menu = Menus[1];
		
		MenuButtons[2][1].menu = Menus[1];
		
		//Adding timers to buttons
		for (int i = 0; i < Menus.length; i++)
		{
			ScheduledThreadPoolExecutor Timer1 = new ScheduledThreadPoolExecutor(5);
			Timer1.scheduleAtFixedRate(Menus[i], 20L, 20L, TimeUnit.MILLISECONDS);
		}

	}

	//Launchers________________________________________________________________
	public void LaunchNewSolo ()
	{
		GameBoard.gameMode = "Solo";
		this.CloseMenus();

		this.remove(Menus[0]);
		
		Asteroid.InitializeFiles();
		Bullet.InitializeFiles();
		Coin.InitializeFiles();
		Animation.InitializeFiles();

		TheSoloSprites = new SoloSprites(this);
		TheSoloPanel = new SoloPanel(this);

		this.add(TheSoloSprites);
		this.setVisible(true);

		this.add(TheSoloPanel);
		this.setVisible(true);
		
		this.setFocusable(true);
		
		this.ResumeGame();

		ScheduledThreadPoolExecutor Timer1 = new ScheduledThreadPoolExecutor(5);
		Timer1.scheduleAtFixedRate(TheSoloSprites, 20L, 20L, TimeUnit.MILLISECONDS);
		
		ScheduledThreadPoolExecutor Timer2 = new ScheduledThreadPoolExecutor(5);
		Timer2.scheduleAtFixedRate(TheSoloPanel, 20L, 20L, TimeUnit.MILLISECONDS);
	}
	
	//Menu Launcher
	public void LaunchMenu (int i)
	{
		//Launching new menu
		Menus[i].active = true;
		Menus[i].scroll(null);
		
		this.add(Menus[i]);
		this.setVisible(true);
	}
	
	//Help
	public void LaunchHelp ()
	{
		Menus[0].active = false;
		
		this.remove(Menus[0]);
		
		HelpMenu helper = new HelpMenu(this);

		this.add(helper);
		this.setVisible(true);
	}
	
	//About Page
	public void LaunchAbout()
	{
		Menus[0].active = false;
		
		this.remove(Menus[0]);
		
		AboutMenu abouter = new AboutMenu(this);

		this.add(abouter);
		this.setVisible(true);
	}
	
	public void CloseMenus ()
	{
		//Close any active menus
		for (int j = 0; j < Menus.length; j ++)
		{
			if (Menus[j].active)
			{
				Menus[j].reset();
				
				this.remove(Menus[j]);
				Menu.lastMenu = j;
				Menu.lastThing = "Menu";
			}
			Menus[j].active = false;
		}
	}
	
	//Closing a menu with sliding
	public void CloseMenu(int i)
	{
		this.remove(Menus[i]);
		Menus[i].active = false;
		Menus[i].reset();
		
		Menu.lastMenu = i;
		Menu.lastThing = "Menu";
	}
	
	public void LaunchMenuFromShop(int i)
	{
		System.out.println("Launching Menu From Shop");
		
		//Setting last thing
		Menu.lastThing = "Shop";
		
		//Launching new menu
		Menus[i].active = true;
		Menus[i].scroll(null);
		
		System.out.println("Current active menu is: " + i);
		
		Menus[i].catchingUp = true;
		Menus[i].catchUp();
		
		TheSoloPanel.reset();
		
		this.remove(TheSoloPanel);
		
		this.add(Menus[i]);
		this.setVisible(true);
		this.repaint();
	}
	
	public void ReturnShop()
	{
		TheSoloPanel.scroll(null);
		TheSoloPanel.catchingDown = true;

		TheSoloPanel.catchDown();
		TheSoloPanel.active = true;
		
		this.add(TheSoloPanel);
		this.setVisible(true);
		this.repaint();
	}

	//Resume game
	public void ResumeGame ()
	{
		//Close all menus
		for (int j = 0; j < Menus.length; j ++)
		{
			if (Menus[j].active)
			{this.remove(Menus[j]);}
			Menus[j].active = false;
		}
		
		//Playing game
		if (gameMode == "Solo")
		{
			this.add(TheSoloSprites);
			this.add(TheSoloPanel);
			
			SoloSprites.gameRunning = true;
			SoloPanel.shopping = false;

			GameBoard.paused = false;
		}
	}

	//Quit Game
	public void QuitGame ()
	{
		if (GameBoard.gameMode == "Solo")
		{
			TheSoloSprites.restartGame();
			
			this.remove(TheSoloPanel);
			this.remove(TheSoloSprites);

			TheSoloPanel.removeAll();
			TheSoloSprites.removeAll();
			
			TheSoloPanel = null;
			TheSoloSprites = null;
			
			this.CloseMenus();
			
			Menus[0].active = true;
			this.add(Menus[0]);
			this.setVisible(true);
		}
	}
	
	//Exit Game
	public void ExitGame()
	{
		System.exit(0);
	}
	
	//Playing Sounds
	public static void playSound(File theSound, int v)
	{	
		if (GameBoard.muting == false)
		{
			try
			{
				Clip clip = null;
				
				clip = AudioSystem.getClip();
				
				AudioInputStream inputStream;
				
				inputStream = AudioSystem.getAudioInputStream(theSound);
				
				clip.open(inputStream);
				
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(0.0f);
				
				clip.start();
			}
			catch (Exception e)
			{
				e.toString();
				System.out.println("Sound Error: ");
			}
		}
	}
}