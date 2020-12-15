import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Ship
{
	//Team
	public int intColor;
	
	//HudUpDate
	public boolean HudUpDate = false;

	//Death Protocol
	public boolean DeathUpDate	= false;
	public boolean runDeathP 	= false;
	
	private DynamicTimer tmrDeathCool = new DynamicTimer(90);
	private DynamicTimer tmrBlink = new DynamicTimer(15);
	
	//Movement & Position _____________________________________________________
	//Control
	public boolean hasControl = true;
	//Slowing
	private boolean slowing = false;
	private boolean slowTogg = false;
	//Position
	public int xPos;
	public int yPos;
	//Speed
	public int xSpeed = 0;
	public int ySpeed = 0;
	//Direction
	public int direction = 0;
	//Speed units
	private int maxSpeed = 16;
	private int speedInc = 1;
	//Spawn Location
	private int xSpawn;
	private int ySpawn;
	//Transparency
	public boolean clip = false;
	public boolean visible = true;
	//Mass in AHU
	public int size;
	
	//Appearance ______________________________________________________________
	public BufferedImage[] Off = new BufferedImage[4];
	public BufferedImage[] On = new BufferedImage[4];
	
	public BufferedImage image = null;
	
	public String color;
	public int hexColor;
	private static String directory;

	public int width;
	public int height;
	
	//Sound______________________________________________________________
	private static File boomSound;
	public 	static File bounceSound;
	
	private static File AlarmSound;
	private DynamicTimer tmrAlarm = new DynamicTimer(15);
	
	//Speed regulation
	DynamicTimer tmrEng = new DynamicTimer(15);
	DynamicTimer tmrSlow = new DynamicTimer(4);

	//Inventory______________________________________________________________
	//Guns
	public boolean hasCal = true;
	public boolean hasLas = false;// false
	public boolean hasCan = false;
	public boolean hasMis = false;
	
	//Selection
	private int Selection = 0;
	//HudStuff
	private int health;
	private int lives;
	private int coins = 0;
	
	//Asteroid Last hit for death type
	public Asteroid ALH;
	
	//Getting Files
	public void InitializeFiles (String color)
	{
		//Getting Images
		//Off
		for (int i = 0; i < 4; i++)
		{
			this.Off[i] = null;
			Ship.directory = "Resource/Images/Ship/" + "Ship" + this.color + i + "Off.png";
			
			try
			{this.Off[i] = ImageIO.read(new File(Ship.directory));}
			
			catch (IOException e)
			{System.out.println("File not found: " + Ship.directory);}
		}
		//On
		for (int i = 0; i < 4; i++)
			{
			this.On[i] = null;
			Ship.directory = "Resource/Images/Ship/" + "Ship" + this.color + i + "On.png";
			
			try
			{this.On[i] = ImageIO.read(new File(Ship.directory));}
			
			catch (IOException e)
			{System.out.println("File not found: " + Ship.directory);}

		}
		
		//Getting Sounds
	    try
	    {
	        boomSound = new File("Resource/Audio/Booms/ShipBoomSound.wav");
	        bounceSound = new File("Resource/Audio/Booms/ShipCrash2.wav");
	        
	        AlarmSound = new File("Resource/Audio/Beeps/AlarmBeep.wav");
	    }
	    catch (Exception e)
	    {
	    	System.out.println("Sound file not found: " + e.toString());
	    }
	}

	//Constructor
	public Ship (String color, int x, int y, int width, int height)
	{
		this.color = color;
		if (this.color == "Blue")
		{this.hexColor = 0x40D0F0;}
		
		else
		{this.hexColor = 0xFF0000;}
		
		this.width = width;
		this.height = height;
		
		this.xPos = x;
		this.yPos = y;
		
		this.size = 2;
		
		this.xSpawn = x;
		this.ySpawn = y;
		
		this.health = 10;
		this.lives = 5;
		
		//Initialize Files
		this.InitializeFiles(color);
		
		//Default Image
		this.image = this.Off[0];
	}
	
	//Update Manager
	public void update ()
	{
		this.move();
		this.fix();
		this.OffScreenChk();
		this.resetEngines();
		this.alarm();
		
		//Conditionals
		if (runDeathP)
		{this.deathProtocol();}
		
		if (this.slowing || this.slowTogg)
		{this.slow();}
	}

	//Accelerate
	public void accelerate (int dir)
	{
		this.direction = dir;
		
		if (dir == 0)
		{if (this.ySpeed > -this.maxSpeed) {this.ySpeed -= speedInc;}}
		
		if (dir == 1)
		{if (this.xSpeed < this.maxSpeed) {this.xSpeed += speedInc;}}
		
		if (dir == 2)
		{if (this.ySpeed < this.maxSpeed) {this.ySpeed += speedInc;}}
		
		if (dir == 3)
		{if (this.xSpeed > -this.maxSpeed) {this.xSpeed -= speedInc;}}
		
		this.image = this.On[dir];
		this.tmrEng.reset();
	}

	//Slow
	public void slow ()
	{
		if (tmrSlow.tick())
		{
			if (this.xSpeed < 0){this.xSpeed += speedInc;}
			if (this.xSpeed > 0){this.xSpeed -= speedInc;}
			
			if (this.ySpeed < 0){this.ySpeed += speedInc;}
			if (this.ySpeed > 0){this.ySpeed -= speedInc;}
		}
	}
	
	//Move Ship
	public void move ()
	{
		this.xPos += this.xSpeed;
		this.yPos += this.ySpeed;
	}
	
	public void alarm ()
	{
		if (this.health < 4 && this.tmrAlarm.tick())
		{GameBoard.playSound(AlarmSound, 0);}
		
		if (this.health == 3)
		{this.tmrAlarm.setMax(18);}
		if (this.health == 2)
		{this.tmrAlarm.setMax(10);}
		if (this.health == 1)
		{this.tmrAlarm.setMax(5);}
	}
	
	//Fixing Hidden Ship
	private void fix ()
	{
		if (this.xSpeed == 0 && this.xPos < 0)
		{this.xPos += 1;}
		
		else if (this.xSpeed == 0 && this.xPos + this.width > GameBoard.boardWidth)
		{this.xPos -= 1;}
		
		if (this.ySpeed == 0 && this.yPos < 0)
		{this.yPos += 1;}
		
		else if (this.ySpeed == 0 && this.yPos + this.height > GameBoard.boardHeight)
		{this.yPos -= 1;}
	}

	//OffScreneCheck
	public void OffScreenChk ()
	{
		if (this.xPos > GameBoard.boardWidth) {this.xPos = 0 - this.width;}
		if (this.yPos > GameBoard.boardHeight) {this.yPos = 0 - this.height;}
		
		if (this.xPos < 0 - this.width) {this.xPos = GameBoard.boardWidth;}
		if (this.yPos < 0 - this.height) {this.yPos = GameBoard.boardHeight;}
	}
	
	//Reset Engines
	public void resetEngines ()
	{if (tmrEng.tick()){this.image = this.Off[this.direction];}}

	//Fire a Bullet
	public Bullet fire ()
	{
		if (Bullet.cool(this.Selection))
		{return new Bullet(this.Selection, this.direction, this.xPos, this.yPos, this.width, this.height);}
		
		else
		{return null;}
	}

	//Reset Ship
	public void resetShip ()
	{
		this.xSpeed = 0;
		this.ySpeed = 0;
		
		this.xPos = this.xSpawn;
		this.yPos = this.ySpawn;
		
		this.direction = 0;
		this.image = this.Off[0];
		
		//Starting Death Protocol
		this.runDeathP = true;
		this.tmrDeathCool.reset();
		this.tmrBlink.reset();
	}
	
	public void deathProtocol ()
	{
		this.hasControl = false;
		
		if (tmrBlink.tick())
		{this.visible = this.switchBln(this.visible);}
		
		if (tmrDeathCool.tick()){this.runDeathP = false;}
		
		if (!this.runDeathP)
		{this.visible = true; this.hasControl = true;}
	}
	
	//Getting Bounds
	public Rectangle getBounds()
	{return new Rectangle (this.xPos, this.yPos, this.width, this.height);}
	
	//Purchasing Items
	public boolean purchase(int id, int cost)
	{
		if (this.coins >= cost)
		{
			if 		(id == 0 && this.hasLas == false)
			{this.hasLas = true; this.coins -= cost; return true;}
			
			else if (id == 1 && this.hasCan == false)
			{this.hasCan = true;  this.coins -= cost;return true;}
			
			else if (id == 2 && this.hasMis == false)
			{this.hasMis = true;  this.coins -= cost;return true;}
			
			else if (id == 3 && this.lives < 5)
			{this.lives += 1; this.coins -= cost;return true;}
			
			else
			{return false;}
		}
		
		else
		{return false;}
	}
	
	//Change Selection
	public void setSelection (int o)
	{
		Bullet.resetCools();
		
		if (o == 0 && this.hasCal)
		{this.Selection = 0;}
		
		else if (o == 1 && this.hasLas)
		{this.Selection = 1;}
		
		else if (o == 2 && this.hasCan)
		{this.Selection = 2;}
		
		else if (o == 3 && this.hasMis)
		{this.Selection = 3;}
	}
	//Cycling Selection
	public void cycleSelection (int o)
	{
		Bullet.resetCools();
		
		this.Selection += o;
		
		if (this.Selection < 0)
		{this.Selection = 3;}
		
		if (this.Selection > 3)
		{this.Selection = 0;}
		
		//Finding nearest next
		if (this.Selection == 0 && this.hasCal)
		{this.Selection = 0;}
		
		else if (this.Selection == 1 && this.hasLas)
		{this.Selection = 1;}
		
		else if (this.Selection == 2 && this.hasCan)
		{this.Selection = 2;}
		
		else if (this.Selection == 3 && this.hasMis)
		{this.Selection = 3;}
		
		else
		{this.Selection = 0;}
	}
	
	//Getting Selection
	public int getSelection ()
	{return this.Selection;}
	
	//Getting stuff
	public int getMaxSpeed ()
	{return this.maxSpeed;}
	
	public int getHealth()
	{return this.health;}

	public void setHealth(int health)
	{
		this.health = health;
		this.HudUpDate = true;

		if (this.health <= 0)
		{
			this.LooseLife();
			this.DeathUpDate = true;
			GameBoard.playSound(boomSound, 0);
		}
	
		else if (this.health > 10)
		{this.health = 10;}
	}
	
	public void LooseLife()
	{
		this.lives -= 1;
		this.health = 10;
	}
	//Setter for lives
	public void setLives (int o)
	{this.lives = o;}
	//getter for lives
	public int getLives()
	{return this.lives;}
	
	//Coins
	public void setCoins (int o)
	{
		this.coins = o;
		
		if (this.coins > 20)
		{this.coins = 20;}
	}
	
	public int getCoins ()
	{return this.coins;}

	//Utilities
	private boolean switchBln (boolean bln)
	{if (bln == true){bln = false;} else {bln = true;} return bln;}
}