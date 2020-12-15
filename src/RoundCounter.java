
public class RoundCounter
{
	public HImage[] images = new HImage[3];
	
	private static HImage[] numbers = new HImage[10];
	
	public int x;
	public int y;
	public int w;
	public int h;
	
	private int round;
	
	public RoundCounter ()
	{
		try
		{
			String path = "Resource2/Images/MenuPics/RoundCounter";
	
			this.images[0] = new HImage(path);
			
			//Size
			this.w = this.images[0].width;
			this.h = this.images[0].height;
			//Position
			this.x = GameBoard.boardWidth - this.w;
			this.y = GameBoard.boardHeight - this.h;

			//Setting numbers
			for (int i = 0; i < 10; i++)
			{
				String tDir = "A_" + Integer.toString(i);
				
				numbers[i] = new HImage("Resource2/Images/Numbers/" + tDir);
			}
			
			//Initializing other images
			this.images[1] = new HImage("Resource2/Images/Numbers/A_0");
			this.images[2] = new HImage("Resource2/Images/Numbers/A_0");
			
			//Setting round numbers
			this.round = 0;
	
			if (this.round < 10)
			{
				this.images[1].image = numbers[0].image;
				this.images[2].image = numbers[this.round].image;
			}
			
			else
			{
				int num1 = (int)Math.floor(this.round / 10);
				int num2 = this.round - (num1 * 10);
				
				this.images[1].image = numbers[num1].image;
				this.images[2].image = numbers[num2].image;
				
				System.out.println("Num1 = " + num1);
				System.out.println("Num2 = " + num2);
			}
			
			this.images[0].xPos = this.x;
			this.images[0].yPos = this.y;
			
			this.images[1].xPos = this.x + 27 * 8;
			this.images[1].yPos = this.y + 24;
			
			this.images[2].xPos = this.images[1].xPos + this.images[1].width + 8;
			this.images[2].yPos = this.y + 24;
			
		}
		catch (Exception e)
		{
			System.out.println("Round Counter construction failed");
			System.out.println(e.toString());
		}
	}
	
	public void setRound (int i)
	{
		this.round = i;
		
		if (this.round < 10)
		{
			this.images[1].image = numbers[0].image;
			this.images[2].image = numbers[this.round].image;
		}
		
		else
		{
			int num1 = (int)Math.floor(this.round / 10);
			int num2 = this.round - (num1 * 10);
			
			this.images[1].image = numbers[num1].image;
			this.images[2].image = numbers[num2].image;
		}
	}
}
