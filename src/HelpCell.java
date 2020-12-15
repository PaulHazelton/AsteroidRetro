import java.awt.Color;

import javax.swing.JLabel;

public class HelpCell
{
	//Position
	public int x;
	public int y;
	
	public int w;
	public int h;
	
	public HImage sprite;
	public JLabel lbl1 = new JLabel();
	
	public HelpCell (int x, int y, int w, int h, String imageDir, String lbl)
	{
		this.x = x;
		this.y = y;
		
		this.w = w;
		this.h = h;
		
		//Sprite
		this.sprite = new HImage (x, y, w, h, imageDir);

		//Label
		lbl1.setText("<html>" + lbl + "</html>");
		lbl1.setVisible(true);

		lbl1.setBounds(this.x, this.y + this.h + 10, this.w, 125);
		lbl1.setVerticalAlignment(JLabel.TOP);
		lbl1.setForeground(Color.WHITE);
		
		//Adding to frame
		HelpMenu.mainBoard.add(lbl1);
	}
}