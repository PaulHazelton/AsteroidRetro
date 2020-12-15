import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class HudItem
{
	public int xPos  = 0;
	public int yPos = 0;
	
	public int width = 0;
	public int height = 0;
	
	public boolean visible = true;
	
	public BufferedImage image = null;
	private String name;
	private String directory;
	
	public String color;
	public Color colColor;
	
	//Constructor for image
	public HudItem (String color, String name)
	{
		this.color = color;
		
		if (this.color == "Blue")
		{this.colColor = new Color(0x40D0F0);}
		else
		{this.colColor = new Color(0xFF0000);}
		
		this.name = name;

		this.visible = true;
		
		this.directory = "Resource/Images/HudItem/" + this.color + this.name + ".png";
		
		 //Getting Image
		try
		{
			this.image = ImageIO.read(new File(this.directory));
			
			this.width = image.getWidth();
			this.height = image.getHeight();
		}
		catch (IOException e)
		{
			System.out.println("File not found: " + this.directory);
		}
	}
	
	//Overload Constructor for scaled Images
	public HudItem (String color, String name, int x, int y, int width, int height)
	{
		this.color = color;
		
		if (this.color == "Blue")
		{this.colColor = new Color(0x40D0F0);}
		else
		{this.colColor = new Color(0xFF0000);}
		
		this.name = name;
		this.xPos = x;
		this.yPos = y;

		this.visible = true;
		
		this.directory = "Resource/Images/HudItem/" + this.color + this.name + ".png";
		
		try //getting Image
		{
			this.image = ImageIO.read(new File(this.directory));
			
			this.width = width;
			this.height = height;
		}
		catch (IOException e)
		{
			System.out.println("File not found: " + this.directory);
		}
	}

	//Overload Constructor for rectangles
	public HudItem (String color, int x, int y, int width, int height, Color colColor)
	{
		this.xPos = x;
		this.yPos = y;
		
		this.width = width;
		this.height = height;

		this.colColor = colColor;
	}
	//Overload Constructor for rectangles
	public HudItem (String color, int x, int y, int width, int height, int hexColor)
	{
		this.color = color;
		this.xPos = x;
		this.yPos = y;
		
		this.width = width;
		this.height = height;

		this.colColor = new Color(hexColor);
	}
	
	//GetBounds
	public Rectangle getBounds ()
	{return new Rectangle(this.xPos, this.yPos, this.width, this.height);}
}
