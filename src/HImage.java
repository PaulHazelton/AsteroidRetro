import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class HImage
{
	public int xPos;
	public int yPos;
	
	public int width;
	public int height;
	
	public BufferedImage image = null;
	private String name;
	public String directory;
	
	public boolean visible = true;
	
	//Constructor for forced dimensions
	public HImage (int x, int y, int w, int h, String name)
	{
		this.xPos = x;
		this.yPos = y;
		
		this.width = w;
		this.height = h;
		
		this.name = name;
		
		this.directory = "Resource/Images/" + this.name + ".png";
		
		try
		{
			this.image = ImageIO.read(new File(this.directory));
		}
		catch (IOException e)
		{
			System.out.println("File not found: " + this.directory);
		}
	}
	
	//Overloaded constructor for default dimensions
	public HImage (int x, int y, String name)
	{
		this.xPos = x;
		this.yPos = y;

		this.name = name;
		
		this.directory = "Resource/Images/" + this.name + ".png";
		
		try
		{
			this.image = ImageIO.read(new File(this.directory));
			
			this.width = this.image.getWidth();
			this.height = this.image.getHeight();
		}
		catch (IOException e)
		{
			System.out.println("File not found: " + this.directory);
		}
	}
	
	//Overload for only yPos
	public HImage (int y, String name)
	{
		this.yPos = y;

		this.name = name;
		
		this.directory = "Resource/Images/" + this.name + ".png";
		
		try
		{
			this.image = ImageIO.read(new File(this.directory));
			
			this.width = this.image.getWidth();
			this.height = this.image.getHeight();
			
			this.xPos = (int) (GameBoard.boardWidth / 2) - (this.width / 2);
		}
		catch (IOException e)
		{
			System.out.println("File not found: " + this.directory);
		}
	}
	
	//Overload for no position
	public HImage (String name)
	{
		this.name = name;
		
		this.directory = this.name + ".png";
		
		try
		{
			this.image = ImageIO.read(new File(this.directory));
			
			this.width = this.image.getWidth();
			this.height = this.image.getHeight();
			
			this.xPos = (int) (GameBoard.boardWidth / 2) - (this.width / 2);
		}
		catch (IOException e)
		{
			System.out.println("File not found: " + this.directory);
		}
	}
	
	//Overload for condensing images
	public HImage (BufferedImage i, int x, int y, int w, int h)
	{
		this.xPos = x;
		this.yPos = y;
		
		this.width = w;
		this.height = h;

		this.image = i;
	}
	
	public void hide ()
	{this.visible = false;}
	
	public void show ()
	{this.visible = true;}
	
	public Rectangle getBounds ()
	{return new Rectangle(this.xPos, this.yPos, this.width, this.height);}
}