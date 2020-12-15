import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Random;

public class Asteroid
{
	//Movement & Position
	//Position
	public int xPos;
	public int yPos;
	//Speed
	public int xSpeed = 0;
	public int ySpeed = 0;
	//Speed Regulation
	public int maxSpeed;
	
	//Size
	public int size;
	
	public int width;
	public int height;
	
	//Rotation
	public int dir;
	private int rSpeed;
	private DynamicTimer rotate;
	private DynamicTimer intRotate = new DynamicTimer(4);
	
	//Health and Damage
	public int health;
	private int damage;
	
	//Image
	public BufferedImage image = null;
	private static BufferedImage[] dirImage = new BufferedImage[4];
	
	//Sounds
	private static File Bounce;
	public static File Boom;

	//Other Asteroids for collision
	public static ArrayList<Asteroid> Asters = new ArrayList<Asteroid>();

	//Utilities
	Random gen = new Random();
	
	//Getting Files
	public static void InitializeFiles ()
	{
		String directory;
		//Getting Images
		for (int i = 0; i < 4; i++)
		{
			Asteroid.dirImage[i] = null;
			
			directory = "Resource/Images/Asteroid/" + "Asteroid" + i + ".png";
			
			try
			{Asteroid.dirImage[i] = ImageIO.read(new File(directory));}
			
			catch (IOException e)
			{System.out.println("File not found: " + directory);}
		}
		
		//Getting Sounds
		try
		{
	        Bounce = new File("Resource/Audio/Booms/AsterBounce.wav");
	        Boom = new File("Resource/Audio/Booms/AsterBoomSound.wav");
		}
	    catch (Exception e)
	    {
	    	System.out.println("Sound file not found: Asteroid");
	    }
	}
	
	//Initial Constructor
	public Asteroid ()
	{
		//Size
		this.size = 3;
		
		this.width = 35 * this.size;
		this.height = this.width;
		//Health
		this.health = 20 * this.size;
		
		//Position
		this.xPos = ran(0, GameBoard.boardWidth - this.width);
		this.yPos = ran(120, GameBoard.boardHeight - this.height);
		
		//Speeds
		this.maxSpeed = 16 - this.size * 3;
		
		this.xSpeed = ran(1, this.maxSpeed);
		this.ySpeed = ran(1, this.maxSpeed);
		
		if (ranBln()){this.xSpeed *= -1;}
		if (ranBln()){this.xSpeed *= -1;}
		
		//Rotation
		this.dir = ran(0, 3);
		
		this.rSpeed = ran(1, 6 - this.size);
		this.rotate = new DynamicTimer(34 - this.rSpeed ^ 2);
		//Setting Default image
		this.image = Asteroid.dirImage[this.dir];
	}
	
	//Split Constructor
	public Asteroid (int size, int x, int y, int damage)
	{
		//Size
		this.size = size;
		
		this.width = 35 * this.size;
		this.height = this.width;
		
		this.health = 20 * this.size;
		this.health += damage;
		
		//Position
		this.xPos = x + this.width + 1;
		this.yPos = y + this.height + 1;
		
		this.dir = ran(0, 3);
		
		//Speeds
		this.maxSpeed = 16 - this.size * 3;
		
		this.xSpeed = ran(1, this.maxSpeed);
		this.ySpeed = ran(1, this.maxSpeed);
		
		if (ranBln()){this.xSpeed *= -1;}
		if (ranBln()){this.xSpeed *= -1;}
		
		//Rotation
		this.rSpeed = ran(1, 6 - this.size);
		this.rotate = new DynamicTimer(34 - this.rSpeed ^ 2);

		//Setting Default image
		this.image = Asteroid.dirImage[this.dir];
	}
	
	//Splitting
	public Asteroid split ()
	{
		if (this.deathChk())
		{
			//Negative previous health
			int damage = this.health;
			
			//Size
			this.size -= 1;
			
			this.width = 35 * this.size;
			this.height = this.width;
			
			this.health = 20 * this.size;
			this.health += damage;
			
			this.dir = ran(0, 3);
			
			//Speeds
			this.maxSpeed = 16 - this.size * 3;
			
			this.xSpeed = ran(1, this.maxSpeed);
			this.ySpeed = ran(1, this.maxSpeed);
			
			if (ranBln()){this.xSpeed *= -1;}
			if (ranBln()){this.xSpeed *= -1;}
			
			//Rotation
			this.rSpeed = ran(1, 6 - this.size);
			this.rotate = new DynamicTimer(34 - this.rSpeed ^ 2);
			
			//Setting Default image
			this.image = Asteroid.dirImage[this.dir];
			
			return new Asteroid(this.size, this.xPos, this.yPos, damage);
		}
		
		else
		{return null;}
	}
	
	//Update manager
	public void update ()
	{
		this.move();
		this.fix();
		this.offScreenChk();
		this.rotate();
		Asteroid.collisionChk();
	}
	
	//Move
	public void move ()
	{
		this.xPos += this.xSpeed;
		this.yPos += this.ySpeed;
	}
	
	//Fixing Hidden Asteroids
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

	//Asteroid Asteroid Crash Check
	public static void collisionChk ()
	{
		int l1 = Asteroid.Asters.size();
		for (int i = 0; i < l1; i++)
		{
			Asteroid rock1 = Asteroid.Asters.get(i);
			Rectangle rockBox1 = rock1.getBounds();
			
			for (int j = i + 1; j < l1; j++)
			{
				Asteroid rock2 = Asteroid.Asters.get(j);
				Rectangle rockBox2 = rock2.getBounds();
				
				if (rockBox1.intersects(rockBox2))
				{
					rock1.bounce(rock2);
					rock1.separateFrom(rock2);
				}
			}
		}
	}
	
	//Asteroid Ship Crash Check
	public void collisionChk (Ship ship)
	{
		Rectangle shipBox = ship.getBounds();

		Rectangle rockBox = this.getBounds();
		
		if (rockBox.intersects(shipBox) && ship.clip == true)
		{
			//Defining momentums
			int Px1 = ship.xSpeed * ship.size;
			int Py1 = ship.ySpeed * ship.size;
			
			int Px2 = this.xSpeed * this.size;
			int Py2 = this.ySpeed * this.size;
			
			//Subtracting Health
			this.setDamage();
			
			this.health -= (int) (Math.abs(Px1) + Math.abs(Py1) / 4);
			ship.setHealth(ship.getHealth() - this.damage);
			
			GameBoard.playSound(Ship.bounceSound, 0);
			
			ship.ALH = this;
			
			//Exchanging momentums
			int PxTemp = Px1;
			int PyTemp = Py1;
			
			Px1 = Px2;
			Py1 = Py2;
			
			Px2 = PxTemp;
			Py2 = PyTemp;

			//Resetting Speeds 
			ship.xSpeed = ((int) Px1 / ship.size);
			ship.ySpeed = ((int) Py1 / ship.size);
			
			//Maximizing Speeds
			if (ship.xSpeed > ship.getMaxSpeed())
			{ship.xSpeed = ship.getMaxSpeed();}
			
			if (ship.xSpeed < -ship.getMaxSpeed())
			{ship.xSpeed = -ship.getMaxSpeed();}
			
			if (ship.ySpeed > ship.getMaxSpeed())
			{ship.ySpeed = ship.getMaxSpeed();}
			
			if (ship.ySpeed < -ship.getMaxSpeed())
			{ship.ySpeed = -ship.getMaxSpeed();}
			
			//Resetting Speeds
			this.xSpeed = ((int) Px2 / this.size);
			this.ySpeed = ((int) Py2 / this.size);
			
			//Maximizing speeds
			if (this.xSpeed > this.maxSpeed)
			{this.xSpeed = this.maxSpeed;}
			
			if (this.xSpeed < -this.maxSpeed)
			{this.xSpeed = -this.maxSpeed;}
			
			if (this.ySpeed > this.maxSpeed)
			{this.ySpeed = this.maxSpeed;}
			
			if (this.ySpeed < -this.maxSpeed)
			{this.ySpeed = -this.maxSpeed;}
			
			this.separateFrom(ship);
		}
	}
	
	//Bouncing Asteroids
	public void bounce (Asteroid rock)
	{
		GameBoard.playSound(Asteroid.Bounce, 1);
		
		//Defining momentums
		int Px1 = this.xSpeed * this.size;
		int Py1 = this.ySpeed * this.size;
		
		int Px2 = rock.xSpeed * rock.size;
		int Py2 = rock.ySpeed * rock.size;
		
		//Subtracting Health
		if (this.size > 1)
		{this.health -= (int) rock.size;}
		
		if (rock.size > 1)
		{rock.health -= (int) this.size;}

		//Exchanging momentums
		int PxTemp = Px1;
		int PyTemp = Py1;
		
		Px1 = Px2;
		Py1 = Py2;
		
		Px2 = PxTemp;
		Py2 = PyTemp;
		
		//Resetting Speeds
		this.xSpeed = ((int) Px1 / this.size);
		this.ySpeed = ((int) Py1 / this.size);
		
		//Maximizing Speeds
		if (this.xSpeed > this.maxSpeed)
		{this.xSpeed = this.maxSpeed;}
		
		if (this.xSpeed < -this.maxSpeed)
		{this.xSpeed = -this.maxSpeed;}
		
		if (this.ySpeed > this.maxSpeed)
		{this.ySpeed = this.maxSpeed;}
		
		if (this.ySpeed < -this.maxSpeed)
		{this.ySpeed = -this.maxSpeed;}
		
		//Resetting Speeds
		rock.xSpeed = ((int) Px2 / rock.size);
		rock.ySpeed = ((int) Py2 / rock.size);
		
		if (rock.xSpeed > rock.maxSpeed)
		{rock.xSpeed = rock.maxSpeed;}
		
		if (rock.xSpeed < -rock.maxSpeed)
		{rock.xSpeed = -rock.maxSpeed;}
		
		if (rock.ySpeed > rock.maxSpeed)
		{rock.ySpeed = rock.maxSpeed;}
		
		if (rock.ySpeed < -rock.maxSpeed)
		{rock.ySpeed = -rock.maxSpeed;}
	}

	//Separating Asteroids from Asteroids
	public void separateFrom (Asteroid rock)
	{
		Rectangle rock1 = this.getBounds();
		
		Rectangle rock2 = rock.getBounds();
		
		int j = 0;
		
		if (rock1.intersects(rock2))
		do
		{	
			this.move();
			rock.move();
			
			rock1 = this.getBounds();
			rock2 = rock.getBounds();
			
			j++;
		}
		while (rock1.intersects(rock2) && j < 5);
		
		if (j >= 5)
		{
			do
			{	
				if (this.xPos < rock.xPos)
				{this.xPos -= 1; rock.xPos += 1;}
				
				else
				{this.xPos += 1; rock.xPos -= 1;}
				
				if (this.yPos < rock.yPos)
				{this.yPos -= 1; rock.yPos += 1;}
				
				else
				{this.yPos += 1; rock.yPos -= 1;}
				
				rock1 = this.getBounds();
				rock2 = rock.getBounds();
			}
			while (rock1.intersects(rock2));
		}
		
	}
	
	//Separating Asteroids from Ship
	public void separateFrom (Ship ship)
	{
		Rectangle rockBox = this.getBounds();
		Rectangle shipBox = ship.getBounds();
		
		int j = 0;
		
		do
		{	
			this.move();
			ship.move();
			
			rockBox = this.getBounds();
			shipBox = ship.getBounds();
			
			j++;
		}
		while (shipBox.intersects(rockBox) && j < 5);
		
		if (j >= 5)
		{
			do
			{	
				if (this.xPos < ship.xPos)
				{this.xPos -= 1; ship.xPos += 1;}
				
				else
				{this.xPos += 1; ship.xPos -= 1;}
				
				if (this.yPos < ship.yPos)
				{this.yPos -= 1; ship.yPos += 1;}
				else
				{this.yPos += 1; ship.yPos -= 1;}
				
				rockBox = this.getBounds();
				shipBox = ship.getBounds();
			}
			while (shipBox.intersects(rockBox));
		}
		
	}
	
	//Getting Bounds of ANY Asteroid
	public Rectangle getBounds ()
	{
		int i = this.size * 3;
		
		return new Rectangle(this.xPos + (i), this.yPos + (i), this.width - (i * 2), this.height - (i * 2));
	}
	
	//Rotate
	public void rotate ()
	{
		if (rotate.tick())
		{
			this.dir = this.intRotate.cycle();
			this.image = Asteroid.dirImage[this.dir];
		}
	}
	
	//OffScreneCheck
	public void offScreenChk ()
	{
		if (this.xPos > GameBoard.boardWidth) {this.xPos = 0 - this.width;}
		if (this.yPos > GameBoard.boardHeight) {this.yPos = 0 - this.height;}
		
		if (this.xPos < 0 - this.width) {this.xPos = GameBoard.boardWidth;}
		if (this.yPos < 0 - this.height) {this.yPos = GameBoard.boardHeight;}
	}
	
	//Damage algorithm
	private void setDamage ()
	{
		int speed;
		
		//Setting speed to greatest speed
		if (Math.abs(this.xSpeed) > Math.abs(this.ySpeed))
		{speed = Math.abs(this.xSpeed);}
		
		else
		{speed = Math.abs(this.ySpeed);}
		
		//If speed is slow, med, or fast
		if (speed < this.maxSpeed / 3)
		{speed = 1;}
		
		else if (speed < this.maxSpeed * (2/3))
		{speed = 2;}
		
		else
		{speed = 3;}
		
		//Setting Damage
		this.damage = (int) (speed / this.size);
		
		if (this.size == 1)
		{this.damage -= 1;}
	}
	
	//Death Check
	public boolean deathChk ()
	{
		if (this.health <= 0)
		{return true;}
		
		else
		{return false;}
	}

	//Randomizer z!
	private int ran (int min, int max)
	{return gen.nextInt((max - min) + 1) + min;}
	
	private boolean ranBln ()
	{if (ran(0, 1) == 0){return true;} else{return false;}}
}