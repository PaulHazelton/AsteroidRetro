import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ShopItem
{
	//Standard Bologna
	public int xPos;
	public int yPos;
	
	public int width;
	public int height;
	
	//Image
	public HImage Hi[] = new HImage[4];
	public BufferedImage image = null;
	
	private BufferedImage off;
	private BufferedImage on;
	
	public String name;
	private String directory1;
	private String directory2;
	private String itemImg;
	
	private String dirNum1;
	private String dirNum2;
	
	//ShopItem Info
	public int cost;
	public int itemId;
	
	public boolean visible = true;
	public boolean unlocked = true;
	
	//LastItem for reasons
	public static ShopItem itemLast = null;

	//Overload for no position
	public ShopItem (int cost, String num1, String num2, String name, int item, String itemImage)
	{
		//Attributes
		this.cost = cost;
		this.itemId = item;
		
		//Image
		this.name = name;
		this.directory1 = "Resource/Images/Shop/ShopBox1Off.png";
		this.directory2 = "Resource/Images/Shop/ShopBox1On.png";
		
		this.dirNum1 = "Numbers/A" + num1;
		this.dirNum2 = "Numbers/A" + num2;
		
		this.itemImg = itemImage;
	}
	
	//Resetting images
	public void setImages ()
	{
		HImage itemImage = new HImage(this.xPos + 30, this.yPos + 30, 50, 50, this.itemImg);

		//Getting Image
		try
		{
			//MainBox image
			this.off = ImageIO.read(new File(this.directory1));
			this.on = ImageIO.read(new File(this.directory2));

			this.image = this.off;

			this.width = this.image.getWidth();
			this.height = this.image.getHeight();
			
			this.Hi[0] = new HImage(this.image, this.xPos, this.yPos, this.width, this.height);
			
			//Other Images
			this.Hi[1] = itemImage;
			
			this.Hi[2] = new HImage(this.xPos + 140, this.yPos + 30, this.dirNum1);
			this.Hi[3] = new HImage(this.Hi[2].xPos + this.Hi[2].width + 10, this.yPos + 30, this.dirNum2);
		}
		catch (IOException e)
		{
			System.out.println("File not found: " + this.directory1 + " _ " + this.directory2 + " _ " + this.dirNum1 + " _ " + this.dirNum2);
		}
	}
	
	public void resetImages ()
	{
		this.Hi[0].yPos = this.yPos;
		this.Hi[1].yPos = this.yPos + 30;
		this.Hi[2].yPos = this.yPos + 30;
		this.Hi[3].yPos = this.yPos + 30;
	}
	
	//Mouse Over stuff
	public void unHighlight ()
	{this.Hi[0].image = this.off;}
	
	public void highlight ()
	{this.Hi[0].image = this.on;}
	
	public void hide ()
	{this.visible = false;}
	
	public void show ()
	{this.visible = true;}
	
	public Rectangle getBounds ()
	{return new Rectangle(this.xPos, this.yPos, this.width, this.height);}
}
