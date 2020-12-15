import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Animation
{	
	//AnimData
	private int type1, type2;
	private int direction;
	private int frames;
	//Images
	private static BufferedImage[][][][] AnimDataBase = new BufferedImage[4][][][];
	
	private static BufferedImage[][][] 	bulletAnims	 = new BufferedImage[3][4][1];
	private static BufferedImage[][][] 	specialAnims = new BufferedImage[1][4][2];
	private static BufferedImage[][][] 	shipAnims	 = new BufferedImage[2][4][3];
	private static BufferedImage[][][] 	asterAnims	 = new BufferedImage[1][4][4];
	
	private BufferedImage[] frame;
	public BufferedImage image;
	//Time
	private DynamicTimer tmr;
	private DynamicTimer cyc;
	
	//Position
	public int xPos;
	public int yPos;
	
	public int width;
	public int height;
	
	private int xSpeed;
	private int ySpeed;
	
	//Initialize Files
	public static void InitializeFiles ()
	{
		String directory = null;

		try
		{
			//BulletAnims
			for (int i = 0; i < 4; i++)
			{directory = "Resource/Images/Animation/CaliberBoom" + i + ".png"; bulletAnims[0][i][0] = ImageIO.read(new File(directory));}
			
			for (int i = 0; i < 4; i++)
			{directory = "Resource/Images/Animation/LaserBoom" + i + ".png"; bulletAnims[1][i][0] = ImageIO.read(new File(directory));}
			
			for (int i = 0; i < 4; i++)
			{directory = "Resource/Images/Animation/CannonBoom" + i + ".png"; bulletAnims[2][i][0] = ImageIO.read(new File(directory));}
			
			//Specials
			for (int i = 0; i < 4; i++)
			{
				for (int j = 0; j < 2; j++)
				{directory = "Resource/Images/Animation/MissileBoom" + i + (j + 1) + ".png"; specialAnims[0][i][j] = ImageIO.read(new File(directory));}
			}
			
			//Ship Crash
			for (int i = 0; i < 4; i++)
			{
				for (int j = 0; j < 3; j++)
				{directory = "Resource/Images/Animation/SbCrash" + i + (j + 1) + ".png"; shipAnims[0][i][j] = ImageIO.read(new File(directory));}
			}
			//Ship Boom
			for (int i = 0; i < 4; i++)
			{
				for (int j = 0; j < 3; j++)
				{directory = "Resource/Images/Animation/SbBoom" + i + (j + 1) + ".png"; shipAnims[1][i][j] = ImageIO.read(new File(directory));}
			}
			
			//Asteroids
			for (int i = 0; i < 4; i++)
			{
				for (int j = 0; j < 4; j++)
				{directory = "Resource/Images/Animation/AsterBoom" + i + (j + 1) + ".png"; asterAnims[0][i][j] = ImageIO.read(new File(directory));}
			}
			
		}
		catch (IOException e)
		{
			System.out.println("File not found: Animation: " + directory);
		}
		
		AnimDataBase[0] = bulletAnims;
		AnimDataBase[1] = specialAnims;
		AnimDataBase[2] = shipAnims;
		AnimDataBase[3] = asterAnims;
	}

	//Constructor
	public Animation (int x, int y, int w, int h, int xS, int yS, int type1, int type2, int dir, int tickS)
	{
		//Position
		this.xPos = x;
		this.yPos = y;
		
		this.xSpeed = xS;
		this.ySpeed = yS;
		//Size
		this.width = w;
		this.height = h;
		
		//type direction
		this.type1 = type1;
		this.type2 = type2;
		this.direction = dir;
		
		this.frames = AnimDataBase[this.type1][this.type2][this.direction].length;
		this.frame = new BufferedImage[this.frames];
		
		//Which Animation
		for (int i = 0; i < this.frames; i++)
		{
			this.frame[i] = AnimDataBase[this.type1][this.type2][this.direction][i];
		}

		//Initializing
		this.tmr = new DynamicTimer(tickS);
		this.cyc = new DynamicTimer(this.frames);
		this.image = this.frame[0];
	}

	//Cycling Animation
	public boolean cycle ()
	{
		//Moving it
		this.move();
		
		//Cycling Animation
		if (tmr.tick())
		{
			int frm = cyc.cycle();
			
			if (frm == 0)
			{return true;}
			
			else
			{
				this.image = this.frame[frm];
				return false;
			}
		}
		
		else
		{return false;}
	}
	
	public void move ()
	{
		this.xPos += this.xSpeed;
		this.yPos += this.ySpeed;
		
	}
}
