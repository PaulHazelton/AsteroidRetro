import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class AboutMenu extends JPanel
{
	public GameBoard mainBoard;
	
	public MenuItem backButton = new MenuItem("Back", "Back", 0, 0);
	private MenuItem itemLast;
	
	public JLabel lbl1 = new JLabel();
	
	private int x = 50;
	private int y = backButton.height + 50;
	private int w = GameBoard.boardWidth - (2 * x);
	private int h = GameBoard.boardHeight - y;
	
	public AboutMenu (GameBoard mainBoard)
	{
		this.mainBoard = mainBoard;
		
		//Label
		setLabelText();
		lbl1.setVisible(true);

		lbl1.setBounds(this.x, this.y, this.w, this.h);
		lbl1.setVerticalAlignment(JLabel.TOP);
		lbl1.setForeground(Color.WHITE);
		
		lbl1.revalidate();
		lbl1.repaint();
		
		//Adding to frame
		this.mainBoard.add(lbl1);
		
		this.revalidate();
		this.repaint();
		
		//Mouse stuff for back button
		this.addMouseMotionListener(new MouseAdapter()
		{
			@Override
			public void mouseMoved(MouseEvent e)
			{
				Point xy = e.getPoint();
				
				if (backButton.getBounds().contains(xy))
				{updateMenuTest(backButton);}
				
				else
				{updateMenuTest(null);}
			}
			
		});
		
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				Point xy = e.getPoint();
				
				if (backButton.getBounds().contains(xy))
				{back();}
			}
		});

	}
	
	public void setLabelText ()
	{
		lbl1.setText("<html>"
				+ "This game is loosely based on Asteroids the game, and was created on Eclipse software."
				+ " This is my first game. I used no graphics engine other than the default graphics on Eclipse. I created all the images and sounds,"
				+ " and I didn't copy any bulk code from the internet. I built this from the ground up."
				+ " I could not have done this without my programming teacher Mrs. Tolentino."
				+ " I started making this game as a learning project to help teach myself how to program in Java, but it became my FBLA project."
				+ " I could not have done this without the support from my parents and the motivation of being a part of FBLA. I hope you enjoy the game!"
					+ "</html>");
		
		lbl1.setFont(lbl1.getFont().deriveFont(18.0f));
	}
	
	//Update back button
	public void updateMenuTest (MenuItem item)
	{
		if (this.itemLast != item)
		{
			this.itemLast = item;

			this.backButton.unHighlight();
			
			//Highlighting next
			if (this.itemLast != null)
			{
				GameBoard.playSound(Menu.MenuSlide, 0);
				this.itemLast.highlight();
			}
			
			this.repaint();
		}
	}
	
	public void back()
	{
		mainBoard.LaunchMenu(0);
		mainBoard.remove(this);
		
		lbl1.setText("");
		
		GameBoard.playSound(Menu.Click2, 0);
	}
	
	@Override
	public void paint(Graphics g)
	{
		Graphics2D Menu = (Graphics2D)g;
		Menu.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//Back Button
		Menu.drawImage(backButton.image, backButton.xPos, backButton.yPos, backButton.width, backButton.height, null);
	}
}