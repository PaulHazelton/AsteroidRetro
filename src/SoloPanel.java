import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

	@SuppressWarnings("serial")
	public class SoloPanel extends JPanel implements Runnable
	{
		GameBoard mainBoard;
		private MenuItem itemLast = null;

		//Shopping data
		public static boolean shopping = false;
		
		//HUD ________________________________________________________________________________________________________________
		private Rectangle hudBox = new Rectangle(0, 0, 300, 120);
		
		private static HudItem Spawn = new HudItem("Blue", "Spawn", 10, 10, 70, 100);
		
		public static HudItem[] HudLife = new HudItem[5];
		public static HudItem[] HudHealth = new HudItem[10];
		public static HudItem[] HudCoin = new HudItem[20];
		
		public static HudItem[] hudItems = 
			{
				new HudItem(Spawn.color, "CaliberTop", 300, 10, 50, 50),
				new HudItem(Spawn.color, "LaserTop"),
				new HudItem(Spawn.color, "CannonTop"),
				new HudItem(Spawn.color, "MissileTop")
			};
		

		private HudItem[] HudSelector = new HudItem[hudItems.length];
		
		public MenuItem pauseBut = new MenuItem("Pause", "PauseGame");
		
		//Round Counter_______________________________________________________________________________________________________
		public static RoundCounter roundCounter = new RoundCounter();
		
		//Shop Items__________________________________________________________________________________________________________
			//Guns
		private HImage[] shopSigns = 
				{
				new HImage(0, Spawn.yPos + Spawn.height + 10, "Shop/GunSign"),
				new HImage("Resource/Images/Shop/LifeSign")
				};
		private int ssLength = shopSigns.length;
		
		public ShopItem[] shopItems = 
				{
				new ShopItem(10, "1", "0", "laser", 1, "HudItem/BlueLaserTop"),
				new ShopItem(16, "1", "6", "cannon", 2, "HudItem/BlueCannonTop"),
				new ShopItem(20, "2", "0", "missile", 3, "HudItem/BlueMissileTop"),
				new ShopItem(5, "0", "5", "life", 4, "Shop/LifePic")
				};
		private int butLength2 = shopItems.length;
		
		//ShopMenu____________________________________________________________________________________________________________
		private HImage sign = new HImage("Resource2/Images/MenuPics/ShopOn");
		
		//Buttons
		private int butLength = 0;
		private MenuItem[] Buttons = 
			{
				new MenuItem("Continue", "Continue"),
				//new MenuItem("Settings", "LaunchMenu", 1),
				new MenuItem("Restart", "RestartGame"),
				new MenuItem("Quit", "EndGame")
			};
		
		//Menu History
		private String lastAction = null;
		public boolean active = false;
		
		//Scrolling
		private int scroll = 0;
		private int mScroll = 0;
		private int bottom = 0;
		private int[] butty;
		
		//Scrolling2
		private int scroll2 = 0;
		private int[] mScroll2 = new int[ssLength];
		private int bottom2 = 0;
		private int[] butty2 = new int[butLength2];
		
		//Opening and closing
		public boolean slidingUp = false;
		public boolean slidingDown = false;
		public boolean catchingUp = false;
		public boolean catchingDown = false;
		
		private int slideSpeed = 0;
		private static int accelRate = 2;
		private boolean setYet = false;

		public SoloPanel (final GameBoard mainBoard)
		{
			 shopping = false;
			 this.mainBoard = mainBoard;
			 
			 //Pause Button
			 pauseBut.xPos = GameBoard.boardWidth - pauseBut.width;
			 pauseBut.yPos = 0;
			 
			 //Setting Shop items______________________________________________________________________________________
			 	//setting positions
			 	//Guns________________________________________
			 this.shopItems[0].xPos = 0;
			 this.shopItems[0].yPos = shopSigns[0].yPos + shopSigns[0].height;
			 this.shopItems[0].setImages();
			 	//setting rest
			 for (int i = 1; i < this.shopItems.length; i++)
			 {
				this.shopItems[i].xPos = 0;
				this.shopItems[i].yPos = this.shopItems[i-1].yPos + this.shopItems[i-1].height; 
				
				this.shopItems[i].setImages();
			 }
			 	//Lives_______________________________________
			 this.shopSigns[1].xPos = 0;
			 this.shopSigns[1].yPos = this.shopItems[2].yPos + this.shopItems[2].height;
			 
			 this.shopItems[3].xPos = 0;
			 this.shopItems[3].yPos = shopSigns[1].yPos + shopSigns[1].height;
			 this.shopItems[3].setImages();
			 
			 //Setting values for scrolling
			 for (int i = 0; i < this.ssLength; i++)
			 {this.mScroll2[i] = this.shopSigns[i].yPos;}
			 
			 for (int i = 0; i < this.butLength2; i++)
			 {this.butty2[i] = this.shopItems[i].yPos;}

			//Finishing HudItems _____________________________________________________________________________________
			for (int i = 1; i < hudItems.length; i++)
			{
				HudItem previous = hudItems[i - 1];
				
				hudItems[i].xPos = previous.xPos + previous.width + 10;
				hudItems[i].yPos = 10;
				
				hudItems[i].width = 50;
				hudItems[i].height = 50;
			}
			
			 //Setting Up shop menu____________________________________________________________________________________
			this.butLength = this.Buttons.length;
			this.butty = new int[this.butLength];
		 
			//Aligning sign
			this.sign.yPos = 0;
			//Aligning first button
			this.Buttons[0].xPos = (GameBoard.boardWidth/2) - (this.Buttons[0].width/2);
			this.Buttons[0].yPos = this.sign.yPos + this.sign.height;
			this.butty[0] = this.Buttons[0].yPos;
			
			//Aligning next buttons
			for (int i = 1; i < this.butLength; i++)
			{
				this.Buttons[i].xPos = (GameBoard.boardWidth/2) - (this.Buttons[i].width/2);
				this.Buttons[i].yPos = this.Buttons[i-1].yPos + this.Buttons[i-1].height;
				
				//Assigning butt y
				this.butty[i] = this.Buttons[i].yPos;
			}
			
			//Setting settings menu
			this.Buttons[1].menu = GameBoard.Menus[1];
			
			//Constructing Arrays _____________________________________________
			//Lives
			for (int i = 0; i < HudLife.length; i++)
			{HudLife[i] = new HudItem(Spawn.color, "Life", (Spawn.width + Spawn.xPos + 20) + (i * (40)), 10, 30, 30);}
			//Health
			for (int i = 0; i < HudHealth.length; i++)
			{HudHealth[i] = new HudItem(Spawn.color, (Spawn.width + Spawn.xPos + 20) + (i * (20)), HudLife[0].yPos + HudLife[0].height + 10, 10, 20, Spawn.colColor);}
			
			//GunSelectors
			for (int i = 0; i < HudSelector.length; i++)
			{
				HudSelector[i] = new HudItem(Spawn.color, "Selector", hudItems[i].xPos, hudItems[i].yPos + hudItems[i].height + 10, 50, 20); HudSelector[i].visible = false;
			}
			
			HudSelector[0].visible = true;

			//Coins (even then odd)
			for (int i = 0; i < HudCoin.length; i += 2)
			{
				HudCoin[i] = new HudItem("Yello", (Spawn.width + Spawn.xPos + 20) + (i * (10)), HudHealth[0].yPos + HudHealth[0].height + 10, 10, 10, 0xFFFF00);
				HudCoin[i].visible = false;
			}
			
			for (int i = 1; i < HudCoin.length; i+= 2)
			{HudCoin[i] = new HudItem("Yello", (Spawn.width + Spawn.xPos + 20) + (i * (10) - 10), HudHealth[0].yPos + HudHealth[0].height + 30, 10, 10, 0xFFFF00); HudCoin[i].visible = false;}
			
			setCoins(GameBoard.TheSoloSprites.BlueShip.getCoins());

			//Setting Defaults______________________________________________________
			hudItems[0].visible = GameBoard.TheSoloSprites.BlueShip.hasCal;
			hudItems[1].visible = GameBoard.TheSoloSprites.BlueShip.hasLas;
			hudItems[2].visible = GameBoard.TheSoloSprites.BlueShip.hasCan;
			hudItems[3].visible = GameBoard.TheSoloSprites.BlueShip.hasMis;

			//MouseListeners___________________________________________________
			this.addMouseMotionListener(new MouseAdapter()
			{
				@Override
				public void mouseMoved(MouseEvent e)
				{
					Point xy = e.getPoint();
					Menu.mxy = xy;
					
					if (shopping)
					{

						//Shop Items
						boolean newBut1 = false;
						
						for (int i = 0; i < shopItems.length; i++)
						{
							if (shopItems[i].getBounds().contains(xy))
							{
								updateShopTest(shopItems[i]);
								newBut1 = true;
								break;
							}
						}
						
						//Menu Items
						if (newBut1 == false)
						{updateShopTest(null);}
						
						//Menu Buttons
						boolean newBut2 = false;
						
						for (int i = 0; i < butLength; i++)
						{
							if 	(Buttons[i].getBounds().contains(xy))
							{
								updateMenuTest(Buttons[i]);
								newBut2 = true;
								break;
							}
						}
						
						if (newBut2 == false)
						{updateMenuTest(null);}
						
						scroll(xy);
						scroll2(xy);
					}
					
					else
					{
						//Pause Button
						if 		(pauseBut.getBounds().contains(xy))
						{updateMenuTest(pauseBut);}
						
						else
						{updateMenuTest(null);}
					}
				}
			});

			//Mouse Wheel Stuff
			this.addMouseWheelListener(new MouseWheelListener()
			{
				@Override
				public void mouseWheelMoved (MouseWheelEvent e)
				{
					int notch = e.getWheelRotation();
					GameBoard.TheSoloSprites.cycleSelection(notch);
				}
			});
			
			//Mouse Selection
			this.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mousePressed(MouseEvent e)
				{
					Point xy = e.getPoint();

					//Choosing Menu Item
					for (int i = 0; i < hudItems.length; i++)
					{
						if (hudItems[i].getBounds().contains(xy)) {GameBoard.TheSoloSprites.setSelection(i);}
					}
					
					//Choosing Menu Item
					if (shopping)
					{
						//ShopItems
						for (int i = 0; i < shopItems.length; i++)
						{
							if (shopItems[i].getBounds().contains(xy))
							{
								if (GameBoard.TheSoloSprites.BlueShip.purchase(i, shopItems[i].cost))
								{
									if (shopItems[i].itemId <= 3)
									{
										GameBoard.playSound(Menu.Click1, 0);
										hudItems[shopItems[i].itemId].visible = true;
										setCoins(GameBoard.TheSoloSprites.BlueShip.getCoins());
									}
									
									else
									{
										GameBoard.playSound(Menu.Click1, 0);
										setCoins(GameBoard.TheSoloSprites.BlueShip.getCoins());
										setLives(GameBoard.TheSoloSprites.BlueShip.getLives());
									}
								}
								else
								{GameBoard.playSound(Menu.Click2, 0);}
							}
						}
						
						//Menu
						for (int i = 0; i < Buttons.length; i++)
						{
							if (Buttons[i].getBounds().contains(xy))
							{startAction(Buttons[i].action);}
						}
					}
					
					else
					{
						if (pauseBut.getBounds().contains(xy))
						{startAction("PauseGame");}
					}
				}
			});
		}
		
		//Actions for button presses
		public void startAction (String action)
		{
			if (this.active)
			{
				this.active = false;
				this.lastAction = action;
				
				if (action == "Continue")
				{GameBoard.playSound(Menu.Click1, 0); this.slidingUp = true;}
				
				else if (action == "LaunchMenu")
				{GameBoard.playSound(Menu.Click1, 0); this.slidingUp = true;}
				
				else if (action == "RestartGame")
				{GameBoard.playSound(Menu.Click2, 0); this.slidingUp = true;}
				
				else if (action == "EndGame")
				{GameBoard.playSound(Menu.Click2, 0); this.slidingUp = true;}
			}
			
			if (action == "PauseGame")
			{
				GameBoard.playSound(Menu.Click2, 0);
				this.PauseGame();
			}
		}
		//Finishing action
		public void finishAction ()
		{
			if (this.lastAction == "Continue")
			{
				GameBoard.TheSoloSprites.startNewRound();
				this.reset();
				shopping = false;
				mainBoard.repaint();
			}
			
			else if (this.lastAction == "LaunchMenu")
			{
				mainBoard.LaunchMenuFromShop(1);
				
				mainBoard.remove(GameBoard.TheSoloPanel);
				
			}
			
			else if (this.lastAction == "RestartGame")
			{
				GameBoard.TheSoloSprites.restartGame();
				shopping = false;
				mainBoard.repaint();
			}
			
			else if (this.lastAction == "EndGame")
			{
				mainBoard.QuitGame();
				GameBoard.Menus[0].catchingUp = true;
				GameBoard.Menus[0].catchUp();
			}
		}
		
		//Re activating a menu
		public void reset()
		{
			//Aligning sign
			this.sign.xPos = (GameBoard.boardWidth/2) - (this.sign.width/2);
			this.sign.yPos = 0;
			//Aligning first button
			this.Buttons[0].xPos = (GameBoard.boardWidth/2) - (this.Buttons[0].width/2);
			this.Buttons[0].yPos = this.sign.yPos + this.sign.height;
			this.butty[0] = this.Buttons[0].yPos;
			
			//Aligning next buttons
			for (int i = 1; i < this.butLength; i++)
			{
				this.Buttons[i].xPos = (GameBoard.boardWidth/2) - (this.Buttons[i].width/2);
				this.Buttons[i].yPos = this.Buttons[i-1].yPos + this.Buttons[i-1].height;
				
				//Assigning butt y
				this.butty[i] = this.Buttons[i].yPos;
			}
			
			this.slideSpeed = 0;
			this.mScroll = 0;
			
			this.slidingUp = false;
			this.slidingDown = false;
			this.catchingUp = false;
			this.catchingDown = false;
			
			this.setYet = false;
		}
		
		//Sliding to next menu
		public void slideUp()
		{
			this.slideSpeed += SoloPanel.accelRate;
			
			this.scroll += this.slideSpeed;
			this.mScroll += this.slideSpeed;
			
			//Adjusting Buttons
			for (int i = 0; i < this.butLength; i ++)
			{
				this.Buttons[i].yPos = this.butty[i] - this.scroll;
				this.sign.yPos = 0 - this.mScroll;
			}
			
			//Ending slide up
			if (this.scroll >= this.bottom)
			{
				this.slidingUp = false;
				this.finishAction();
			}

			mainBoard.repaint();
		}
		
		//Setting up for catch Down
		public void setCatchDown ()
		{
			double time = Math.sqrt((2 * this.bottom) / SoloPanel.accelRate);

			this.slideSpeed = (int)(time * SoloPanel.accelRate);
			
			this.catchingDown = false;
			this.scroll(null);
			this.catchingDown = true;
			
			this.mScroll = -this.bottom;
			this.scroll = -this.scroll - this.bottom;
			
			this.scroll += this.slideSpeed;
			this.mScroll += this.slideSpeed;
		}
		
		//Sliding on screen
		public void catchDown()
		{
			if (this.setYet == false)
			{this.setCatchDown(); this.setYet = true;}

			this.slideSpeed -= SoloPanel.accelRate;
			
			this.scroll += this.slideSpeed;
			this.mScroll += this.slideSpeed;
			
			//Adjusting Buttons
			for (int i = 0; i < this.butLength; i ++)
			{
				this.Buttons[i].yPos = this.butty[i] + this.scroll;
				this.sign.yPos = 0 + this.mScroll;
			}
			
			//Ending catch up
			if (this.slideSpeed <= 0)
			{
				this.catchingDown = false;
				this.mScroll = 0;
				this.sign.yPos = 0;
				
				this.reset();
				this.scroll(null);
			}

			mainBoard.repaint();
		}

		//Update Shop
		public void updateShopTest (ShopItem item)
		{
			if (ShopItem.itemLast != item)
			{
				ShopItem.itemLast = item;
				
				//UnHighlighting All others
				for (int i = 0; i < this.shopItems.length; i++)
				{this.shopItems[i].unHighlight();}

				//Highlighting new
				if (item != null)
				{
					GameBoard.playSound(Menu.MenuSlide, 0);
					item.highlight();
				}
				mainBoard.repaint();
			}
		}
		//Update Menu
		public void updateMenuTest (MenuItem item)
		{
			if (this.active || shopping == false)
			{
				if (this.itemLast != item)
				{
					this.itemLast = item;
					
					//anti highlighting buttons
					for (int i = 0; i < this.butLength; i++)
					{this.Buttons[i].unHighlight(); this.pauseBut.unHighlight();}

					if (item != null)
					{
						GameBoard.playSound(Menu.MenuSlide, 0);
						item.highlight();
					}
					mainBoard.repaint();
				}
			}
		}
		
		//Scrolling Menu
		public void scroll(Point xy)
		{
			if (!(this.slidingDown || this.slidingUp || this.catchingUp || this.catchingDown))
			{
				// y point
				int y;
				
				if (xy == null)
				{y = Menu.mxy.y;}
				
				else
				{y = xy.y;}
				
				//Range
				int h = GameBoard.boardHeight;
				double mh = this.sign.height;
				double r = h - mh;
				
				//Bottom button, bottom y
				int bb = this.butty[this.butLength - 1];
				this.bottom = bb + this.Buttons[this.butLength - 1].height;

				//Exceeding range top bound
				if (y < mh)
				{this.scroll = 0;}
				//Exceeding range bottom bound
				else if (y > mh + r)
				{this.scroll = this.bottom - h;}
				//Middle range
				else
				{
					//take y to lower bound
					double y2 = y - mh;
					double perc = y2/r;
					this.scroll = (int)(perc * (this.bottom - h));
				}
				
				//Adjusting Buttons
				for (int i = 0; i < this.butLength; i ++)
				{
					this.Buttons[i].yPos = this.butty[i] - this.scroll;
				}

				mainBoard.repaint();
			}
		}
		
		//Scrolling Shop Menu
		public void scroll2(Point xy)
		{
			// y point
			int y;
			
			if (xy == null)
			{y = Menu.mxy.y;}
			
			else
			{y = xy.y;}
			
			//Range
			int h = GameBoard.boardHeight;
			double mh = this.hudBox.height;
			double r = h - mh;
			
			//Bottom button, bottom y
			int bb = this.butty2[this.butLength2 - 1];
			this.bottom2 = bb + this.shopItems[this.butLength2 - 1].height;
			
			if (this.bottom2 > GameBoard.boardHeight)
			{
				//Exceeding range top bound
				if (y < mh)
				{this.scroll2 = 0;}
				//Exceeding range bottom bound
				else if (y > mh + r)
				{this.scroll2 = this.bottom2 - h;}
				//Middle range
				else
				{
					//take y to lower bound
					double y2 = y - mh;
					double perc = y2/r;
					this.scroll2 = (int)(perc * (this.bottom2 - h));
				}
				
				//Adjusting Signs
				for (int i = 0; i < this.ssLength; i ++)
				{
					this.shopSigns[i].yPos = this.mScroll2[i] - this.scroll2;
				}
				
				//Adjusting Buttons
				for (int i = 0; i < this.butLength2; i ++)
				{
					this.shopItems[i].yPos = this.butty2[i] - this.scroll2;
					this.shopItems[i].resetImages();
				}
			}
		}
		
		//Setters______________________________________________________________
		public void setSelection (int o)
		{
			for (int i = 0; i < HudSelector.length; i++)
			{HudSelector[i].visible = false;}
			
			HudSelector[o].visible = true;
			
			this.repaint();
		}
		
		public static void setHealth (int o)
		{
			for (int i = 0; i < HudHealth.length; i++)
			{if (i < o){HudHealth[i].visible = true;} else{HudHealth[i].visible = false;}}
		}
		
		public static void setLives (int o)
		{
			for (int i = 0; i < HudLife.length; i++)
			{if (i < o){HudLife[i].visible = true;} else{HudLife[i].visible = false;}}
		}
		
		public static void setCoins (int o)
		{
			for (int i = 0; i < HudCoin.length; i++)
			{if (i < o){HudCoin[i].visible = true;} else{HudCoin[i].visible = false;}}
		}
		
		public static void setRound (int i)
		{
			roundCounter.setRound(i);
		}
		
		//Pause Game
		public void PauseGame ()
		{
			if (!shopping)
			{
				GameBoard.paused = true;
				SoloSprites.gameRunning = false;
				
				int i = 2;
				
				mainBoard.CloseMenus();
				
				//Launching new menu
				GameBoard.Menus[i].active = true;
				GameBoard.Menus[i].scroll(null);
				GameBoard.Menus[i].catchingDown = true;
				GameBoard.Menus[i].catchDown();
				
				System.out.println("Current active menu is: " + i);
	
				//Removing Game
				mainBoard.remove(GameBoard.TheSoloPanel);
				mainBoard.remove(GameBoard.TheSoloSprites);
				//Adding Menu
				mainBoard.add(GameBoard.Menus[i]);
				mainBoard.setVisible(true);
				mainBoard.repaint();
			}
		}
		
		//paint
		@Override
		public void paint(Graphics g)
		{
			Graphics2D Hud = (Graphics2D)g;
			Hud.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			//Round Counter
			for (int i = 0; i < roundCounter.images.length; i++)
			{
				HImage t = roundCounter.images[i];
				
				Hud.drawImage(t.image, t.xPos, t.yPos, t.width, t.height, null);
			}
			
			//Shop
			if (shopping)
			{
				//Crap too Buy!!
				for (int i = 0; i < shopSigns.length; i ++)
				{
					Hud.drawImage(shopSigns[i].image, shopSigns[i].xPos, shopSigns[i].yPos, shopSigns[i].width, shopSigns[i].height, null);
				}

				for (int i = 0; i < this.shopItems.length; i++)
				{
					ShopItem tempItem = this.shopItems[i];
					for (int j = 0; j < tempItem.Hi.length; j++)
					{
						HImage tempImage = tempItem.Hi[j];
						Hud.drawImage(tempImage.image, tempImage.xPos, tempImage.yPos, tempImage.width, tempImage.height, null);
					}
				}

				//Drawing buttons
				for (int i = 0; i < this.butLength; i ++)
				{Hud.drawImage(this.Buttons[i].image, this.Buttons[i].xPos, this.Buttons[i].yPos, this.Buttons[i].width, this.Buttons[i].height, null);}
				
				//Shop Menu
				Hud.drawImage(sign.image, sign.xPos, sign.yPos, sign.width, sign.height, null);
				
				Hud.setColor(Color.MAGENTA);
				//HitBoxes
				if (GameBoard.sHitBoxes)
				{
					//Drawing buttons
					for (int i = 0; i < this.butLength; i ++)
					{Hud.draw(this.Buttons[i].getBounds());}
					
					//ShopItems
					for (int i = 0; i < this.shopItems.length; i++)
					{Hud.draw(this.shopItems[i].getBounds());}
				}
			}
			
			else
			{
				//Pause Button
				Hud.drawImage(pauseBut.image, pauseBut.xPos, pauseBut.yPos, pauseBut.width, pauseBut.height, null);
				
				//Developer Options
				Hud.setColor(Color.magenta);
				if (GameBoard.sHitBoxes)
				{
					Hud.draw(pauseBut.getBounds());
				}
			}
			
			//HUD
			//Back rectangle
			Hud.setColor(Color.BLACK);
			Hud.fillRect(hudBox.x, hudBox.y, hudBox.width, hudBox.height);
			
			//Spawn
			Hud.drawImage(Spawn.image, Spawn.xPos, Spawn.yPos, Spawn.width, Spawn.height, null);
			
			//Lives
			for (int i = 0; i < HudLife.length; i++)
			{if (HudLife[i].visible) { Hud.drawImage(HudLife[i].image, HudLife[i].xPos, HudLife[i].yPos, HudLife[i].width, HudLife[i].height, null);}}
			
			//Health
			Hud.setColor(HudHealth[0].colColor);
			for (int i = 0; i < HudHealth.length; i++)
			{if (HudHealth[i].visible) { Hud.fillRect(HudHealth[i].xPos, HudHealth[i].yPos, HudHealth[i].width, HudHealth[i].height);}}
			
			//Coins
			Hud.setColor(HudCoin[0].colColor);
			for (int i = 0; i < HudCoin.length; i++)
			{if (HudCoin[i].visible) {Hud.fillRect(HudCoin[i].xPos, HudCoin[i].yPos, HudCoin[i].width, HudCoin[i].height);}}
			
			//GunSlection
			for (int i = 0; i < hudItems.length; i++)
			{
				HudItem hi = hudItems[i];
				
				if (hi.visible)
				{Hud.drawImage(hi.image, hi.xPos, hi.yPos, hi.width, hi.height, null);}
			}
			
			//Selectors
			for (int i = 0; i < HudSelector.length; i++)
			{if (HudSelector[i].visible) { Hud.drawImage(HudSelector[i].image, HudSelector[i].xPos, HudSelector[i].yPos, HudSelector[i].width, HudSelector[i].height, null);}}
			
		}

		
		@Override
		public void run()
		{
			if (this.slidingUp)
			{this.slideUp();}
			
			if (this.catchingDown)
			{this.catchDown();}
		}

	}
	