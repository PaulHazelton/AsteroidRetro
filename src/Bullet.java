import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bullet
{	
	public int xPos;
	public int yPos;
	
	private int xSpeed = 0;
	private int ySpeed = 0;
	
	public int width;
	public int height;
	
	public int direction;
	public int maxSpeed;
	public int damage;
	
	public boolean timedOut = false;
	private int ticks = 0;
	
	public BufferedImage image = null;

	public int type;
	
	//Bullet Data______________________________________________________________
	
	//Widths and heights
	private static int[][] sizes = 
		{{10, 10}, {10, 30}, {30, 30}, {30, 50}};
	
	//CoolDown Timers
	private static int[] coolTicks = {20, 15, 20, 30};

	//Timers to regulate fire speed
	private static DynamicTimer[] tmrCool = 
		{
			new DynamicTimer(coolTicks[0]),
			new DynamicTimer(coolTicks[1]),
			new DynamicTimer(coolTicks[2]),
			new DynamicTimer(coolTicks[3])
		};

	//Speed
	private static int[] speeds = {20, 40, 25, 45};

	//Damage
	private static int[] damageVals = {25, 20, 35, 30};
	
	//Gun Cool down booleans
	private static boolean[] gunCool = new boolean[4];

	//END OF BULLET DATA_______________________________________________________
	
	//Files____________________________________________________________________
	//Images
	public static BufferedImage[][] BulletImgs = new BufferedImage[4][4];

	//Sounds
	public static File[] fireSound = new File[4];
	public static File[] gunBoomSound = new File[4];
	
	//Initialize Files
	public static void InitializeFiles ()
	{
		String directory = null;
		
		//Images
		try
		{
			for (int i = 0; i < 4; i++)
			{
				//Bullets of type and direction
				directory = "Resource2/Images/Bullet/Caliper" + i + ".png";
				BulletImgs[0][i] = ImageIO.read(new File(directory));
				
				directory = "Resource2/Images/Bullet/Laser" + i + ".png";
				BulletImgs[1][i] = ImageIO.read(new File(directory));
				
				directory = "Resource2/Images/Bullet/Cannon" + i + ".png";
				BulletImgs[2][i] = ImageIO.read(new File(directory));
				
				directory = "Resource2/Images/Bullet/Missile" + i + ".png";
				BulletImgs[3][i] = ImageIO.read(new File(directory));
			}
		}
		catch (IOException e)
		{System.out.println("File not found, BulletImage: " + directory);}

		//Sounds
		try
		{
			fireSound[0] = new File("Resource/Audio/Guns/CaliberFireSound.wav");
			fireSound[1] = new File("Resource/Audio/Guns/LaserBlastSound.wav");
			fireSound[2] = new File("Resource/Audio/Guns/CannonFire.wav");
			fireSound[3] = new File("Resource/Audio/Guns/MissileLaunchSound.wav");
	        
			gunBoomSound[0] = new File("Resource/Audio/Booms/CaliberBoom.wav");
			gunBoomSound[1] = new File("Resource/Audio/Booms/LaserBoom.wav");
			gunBoomSound[2] = new File("Resource/Audio/Booms/CannonBoomSound.wav");
	        gunBoomSound[3] = new File("Resource/Audio/Booms/MissileBoomSound.wav");
	    }
	    catch (Exception e)
	    {
	    	System.out.println("Sound file not found: " + e.toString());
	    }
	}
	
	//Constructor
	public Bullet (int t, int dir, int x, int y, int w, int h)
	{
		this.type = t;
		
		this.damage = damageVals[this.type];
		this.maxSpeed = speeds[this.type];
		
		this.direction = dir;

		this.setSpeeds();
		
		this.xPos = (int)((x + (w / 2)) - (this.width / 2));
		this.yPos = (int)((y + (h / 2)) - (this.height / 2));

		//Setting Image
		this.image = BulletImgs[this.type][this.direction];

		tmrCool[this.type].reset();
		gunCool[this.type] = false;
		
		GameBoard.playSound(Bullet.fireSound[this.type], 0);
	}
	
	private void setSpeeds ()
	{
		int dir = this.direction;
		
		//Setting Speeds
		if (dir == 0)
		{
			this.ySpeed = -this.maxSpeed;
			
			this.width = sizes[this.type][0];
			this.height = sizes[this.type][1];
		}
		
		else if (dir == 1)
		{
			this.xSpeed = this.maxSpeed;
			
			this.width = sizes[this.type][1];
			this.height = sizes[this.type][0];
		}
		
		else if (dir == 2)
		{
			this.ySpeed = this.maxSpeed;
			
			this.width = sizes[this.type][0];
			this.height = sizes[this.type][1];
		}
		
		else
		{
			this.xSpeed = -this.maxSpeed;
			
			this.width = sizes[this.type][1];
			this.height = sizes[this.type][0];
		}
	}

	//Update Manager
	public void update ()
	{
		this.ticks ++;
		
		this.move();
		this.timeOutChk();
		this.OffScreenChk();
	}
	
	public static void updateClass ()
	{
		bulletCool();
	}
	
	//Bullet CoolDown
	public static void bulletCool ()
	{for (int i = 0; i < tmrCool.length; i++) {if (tmrCool[i].tick()){gunCool[i] = true;}}}
	
	//Cool test for fire
	public static boolean cool (int t)
	{return gunCool[t];}
	
	public static void resetCools ()
	{for (int i = 0; i < tmrCool.length; i++) {tmrCool[i].reset();}}
	
	//Moving Bullet
	public void move ()
	{
		this.xPos += this.xSpeed;
		this.yPos += this.ySpeed;
	}
	
	//Off Screen Check
	public boolean OffScreen ()
	{
		if (this.xPos > GameBoard.boardWidth) {return true;}
		else if (this.xPos < 0 - this.width) {return true;}

		else if (this.yPos > GameBoard.boardHeight) {return true;}
		else if (this.yPos < 0 - this.height) {return true;}
		
		else {return false;}
	}
	
	//OffScreneCheck
	public void OffScreenChk ()
	{
		if (this.xPos > GameBoard.boardWidth) {this.xPos = 0 - this.width;}
		if (this.yPos > GameBoard.boardHeight) {this.yPos = 0 - this.height;}
		
		if (this.xPos < 0 - this.width) {this.xPos = GameBoard.boardWidth;}
		if (this.yPos < 0 - this.height) {this.yPos = GameBoard.boardHeight;}
	}
	
	//Time out Check
	public void timeOutChk()
	{
		int maxDis = (int) (GameBoard.boardWidth + GameBoard.boardHeight) / 2;
		
		if (this.maxSpeed * this.ticks >= maxDis)
		{this.timedOut = true;}
	}
	
	//Hitting An Asteroid
	public boolean hit (Asteroid rock)
	{
		Rectangle rockBox = rock.getBounds();
		Rectangle BulletBox = this.getBounds();
		
		if (rockBox.intersects(BulletBox))
		{return true;} else {return false;}
	}
	
	//Getting Bounds
	public Rectangle getBounds ()
	{
		return new Rectangle(this.xPos, this.yPos, this.width, this.height);
	}
}
