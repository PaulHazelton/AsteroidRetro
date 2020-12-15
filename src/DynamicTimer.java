public class DynamicTimer
{
	private int inc;
	private int max;
	
	public DynamicTimer (int max)
	{
		this.max = max;
		this.inc = 0;
	}
	
	public boolean tick ()
	{
		this.inc += 1;
		
		if (inc > this.max)
		{
			inc = 0;
			return true;
		}
		else
		{return false;}
	}
	
	public int cycle ()
	{
		this.inc += 1;
		if (this.inc == this.max){this.inc = 0;}
		
		return this.inc;
	}
	
	public void reset ()
	{this.inc = 0;}
	
	public int getInc ()
	{return this.inc;}
	
	public void setMax (int max)
	{this.max = max;}
}