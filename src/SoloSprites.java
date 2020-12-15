import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

 	@SuppressWarnings("serial")
	public class SoloSprites extends JPanel implements Runnable
	{
		static GameBoard mainBoard;
		static Random gen = new Random();
		
		private int numOfAsters = 0;
		private int roundNum = 0;
		
		public static boolean gameRunning = false;

		//Ship
		public Ship BlueShip = new Ship("Blue", 10, 10, 70, 70);
		
		//Spawn Box
		Rectangle SpawnBox = new Rectangle(10, 10, 70, 100);

		//Bullets
		public ArrayList<Bullet> Caliper = new ArrayList<Bullet>();
		public ArrayList<Bullet> Laser 	= new ArrayList<Bullet>();
		public ArrayList<Bullet> Cannon 	= new ArrayList<Bullet>();
		public ArrayList<Bullet> Missile = new ArrayList<Bullet>();
		
		//Animations
		public ArrayList<Animation> anims = new ArrayList<Animation>();

		//Asteroids
		public ArrayList<Asteroid> aster = new ArrayList<Asteroid>();
		
		//Coins
		public ArrayList<Coin> coins = new ArrayList<Coin>();

		//Constructor
		public SoloSprites(GameBoard newMainBoard)
		{
			mainBoard = newMainBoard;

			this.roundNum = 0;
			this.startNewRound();
		}

		//Painting Stuff
		@Override
		public void paint(Graphics g)
		{
			try
			{
				Graphics2D Sprite = (Graphics2D)g;
				Sprite.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				if (GameBoard.sSprites)
				{
					//Asteroids
					for (Asteroid aster : aster)
					{Sprite.drawImage(aster.image, aster.xPos, aster.yPos, aster.width, aster.height, null);}
					
					//Bullets___________________________________________________________________________________________________
					Sprite.setColor(new Color(BlueShip.hexColor));
					
					for (Bullet CurrBullet: Caliper)
					{Sprite.drawImage(CurrBullet.image, CurrBullet.xPos, CurrBullet.yPos, CurrBullet.width, CurrBullet.height, null);}
					
					for (Bullet CurrBullet: Laser)
					{Sprite.drawImage(CurrBullet.image, CurrBullet.xPos, CurrBullet.yPos, CurrBullet.width, CurrBullet.height, null);}
					
					for (Bullet CurrBullet: Cannon)
					{Sprite.drawImage(CurrBullet.image, CurrBullet.xPos, CurrBullet.yPos, CurrBullet.width, CurrBullet.height, null);}
					
					for (Bullet CurrBullet: Missile)
					{Sprite.drawImage(CurrBullet.image, CurrBullet.xPos, CurrBullet.yPos, CurrBullet.width, CurrBullet.height, null);}
	
					//Ship
					if (BlueShip.visible)
					{Sprite.drawImage(BlueShip.image, BlueShip.xPos, BlueShip.yPos, BlueShip.width, BlueShip.height, null);}
	
					//Coins
					for (Coin coin : coins)
					{Sprite.drawImage(coin.image, coin.xPos, coin.yPos, coin.width, coin.height, null);}
					
					//Animations
					for (Animation anim : anims)
					{Sprite.drawImage(anim.image, anim.xPos, anim.yPos, anim.width, anim.height, null);}
				}
				
				Sprite.setColor(Color.MAGENTA);
				//Borders
				if (GameBoard.sBorders)
				{
					Sprite.drawLine(0, GameBoard.boardHeight, GameBoard.boardWidth, GameBoard.boardHeight);
				}
				
				//Hit Boxes
				if (GameBoard.sHitBoxes)
				{
					//Asteroids
					Sprite.setColor(Color.MAGENTA);
					for (Asteroid aster : aster)
					{Sprite.draw(aster.getBounds());}
					
					//Bullets
					Sprite.setColor(Color.RED);
					for (Bullet CurrBullet: Caliper)
					{Sprite.draw(CurrBullet.getBounds());}
					
					for (Bullet CurrBullet: Laser)
					{Sprite.draw(CurrBullet.getBounds());}
					
					for (Bullet CurrBullet: Cannon)
					{Sprite.draw(CurrBullet.getBounds());}
					
					for (Bullet CurrBullet: Missile)
					{Sprite.draw(CurrBullet.getBounds());}
					
					//Ship
					Sprite.setColor(Color.CYAN);
					Sprite.draw(BlueShip.getBounds());
					
					//Coins
					Sprite.setColor(Color.YELLOW);
					for (Coin coin : coins)
					{Sprite.draw(coin.getBounds());}
				}
			}
			catch (Exception e)
			{System.out.println("Paint Error: " + e.toString());}
		}
		
		
		public void updateSprites ()
		{
			updateShip();
			updateBullets();
			updateAnims();
			updateAsteroids();
			updateCoins();
			
			if (roundEndTest() && gameRunning == true)
			{endRound();}
			
			mainBoard.repaint();
		}
		
		private void updateShip ()
		{
			//Updating Blue Ship_______________________________________________
			if (BlueShip.hasControl)
			{
				if (GameBoard.W){BlueShip.accelerate(0);}
				if (GameBoard.A){BlueShip.accelerate(3);}
				if (GameBoard.S){BlueShip.accelerate(2);}
				if (GameBoard.D){BlueShip.accelerate(1);}
				
				if (GameBoard.LShift){BlueShip.slow();}
			}
			
			BlueShip.update();
			
			//SpawnBox Thing
			if (SpawnBox.intersects(BlueShip.getBounds()))
			{BlueShip.clip = false;}
			
			else
			{BlueShip.clip = true;}
			
			//HudUpDate
			if (BlueShip.HudUpDate)
			{
				SoloPanel.setHealth(BlueShip.getHealth());
				SoloPanel.setLives(BlueShip.getLives());
				BlueShip.HudUpDate = false;
			}

			//Death Up Date
			if (BlueShip.DeathUpDate)
			{
				BlueShip.DeathUpDate = false;
				
				GameBoard.W = false;
				GameBoard.A = false;
				GameBoard.S = false;
				GameBoard.D = false;
				
				GameBoard.Space = false;
				GameBoard.LShift = false;
				
				//Adding Animations
				if (BlueShip.ALH.size > 1)
				{anims.add(new Animation(BlueShip.xPos, BlueShip.yPos, BlueShip.width, BlueShip.height, BlueShip.ALH.xSpeed, BlueShip.ALH.ySpeed, 2, 0, BlueShip.direction, 6));}
				
				else
				{anims.add(new Animation(BlueShip.xPos, BlueShip.yPos, BlueShip.width, BlueShip.height, BlueShip.xSpeed, BlueShip.ySpeed, 2, 1, BlueShip.direction, 6));}
				
				//Resetting for continuation of play
				if (BlueShip.getLives() > 0)
				{BlueShip.resetShip();}
				
				//Testing for game over
				else
				{
					BlueShip.hasControl = false;
					BlueShip.visible = false;
					BlueShip.clip = false;
					
					mainBoard.remove(GameBoard.TheSoloPanel);
					mainBoard.LaunchMenu(3);
				}
			}

		}
		
		private void updateBullets ()
		{
			Bullet.updateClass();
			
			//Adding Bullets___________________________________________________
			if (GameBoard.Space && BlueShip.hasControl && BlueShip.clip)
			{
				Bullet tempBullet = BlueShip.fire();
				if (!(tempBullet == null))
				{
					if (tempBullet.type == 0)
					{Caliper.add(tempBullet);}
					
					if (tempBullet.type == 1)
					{Laser.add(tempBullet);}
					
					if (tempBullet.type == 2)
					{Cannon.add(tempBullet);}
					
					if (tempBullet.type == 3)
					{Missile.add(tempBullet);}
				}
			}
			
			//Updating Caliper
			for (int i = 0; i < Caliper.size(); i++)
			{
				Bullet temp;
				temp = Caliper.get(i);
				temp.update();

				//Asteroid hit removal
				for (Asteroid rock : aster)
				{
					if (temp.hit(rock))
					{
						rock.health -= temp.damage;
						Caliper.remove(temp);
						
						GameBoard.playSound(Bullet.gunBoomSound[0], 3);
						anims.add(new Animation(temp.xPos, temp.yPos, 30, 30, rock.xSpeed, rock.ySpeed, 0, temp.type, temp.direction, 4));
					}
				}
				//Timing Out
				if (temp.timedOut)
				{Caliper.remove(temp);}
			}
			//Updating Laser
			for (int i = 0; i < Laser.size(); i++ )
			{
				Bullet temp;
				temp = Laser.get(i);
				temp.update();

				//Asteroid hit removal
				for (Asteroid rock : aster)
				{
					if (temp.hit(rock))
					{
						rock.health -= temp.damage;
						Laser.remove(temp);
						
						GameBoard.playSound(Bullet.gunBoomSound[1], 3);
						anims.add(new Animation(temp.xPos, temp.yPos, 30, 30, rock.xSpeed, rock.ySpeed, 0, temp.type, temp.direction, 4));
					}
				}
				//Timing Out
				if (temp.timedOut)
				{Laser.remove(temp);}
			}
			//Updating Cannon
			for (int i = 0; i < Cannon.size(); i++ )
			{
				Bullet temp;
				temp = Cannon.get(i);
				temp.update();

				//Asteroid hit removal
				for (Asteroid rock : aster)
				{
					if (temp.hit(rock))
					{
						rock.health -= temp.damage;
						Cannon.remove(temp);
						
						GameBoard.playSound(Bullet.gunBoomSound[2], 3);
						anims.add(new Animation(temp.xPos, temp.yPos, 50, 50, rock.xSpeed, rock.ySpeed, 0, temp.type, temp.direction, 4));
					}
				}
				//Timing Out
				if (temp.timedOut)
				{Cannon.remove(temp);}
			}
			//Updating Missile
			for (int i = 0; i < Missile.size(); i++ )
			{
				Bullet temp;
				temp = Missile.get(i);
				temp.update();

				//Asteroid hit removal
				for (Asteroid rock : aster)
				{
					if (temp.hit(rock))
					{
						rock.health -= temp.damage;
						
						//Splash Damage
						Rectangle splashBox = new Rectangle(temp.xPos - 175, temp.yPos - 175, 400, 400);

						if (splashBox.intersects(BlueShip.getBounds()) || splashBox.contains(BlueShip.getBounds()))
						{BlueShip.setHealth((int)(BlueShip.getHealth() - temp.damage / 10));}

						for (Asteroid rockSpl : aster)
						{
							Rectangle rockBox = rockSpl.getBounds();
							
							if (rockBox.intersects(splashBox) || splashBox.contains(rockBox))
							{rockSpl.health -= (int) temp.damage / 2;}
						}

						Missile.remove(temp);

						GameBoard.playSound(Bullet.gunBoomSound[3], 3);

						anims.add(new Animation(temp.xPos, temp.yPos, 50, 50, rock.xSpeed, rock.ySpeed, 1, 0, temp.direction, 6));
					}
				}
				//Timing Out
				if (temp.timedOut)
				{Missile.remove(temp);}
			}
			
		}
		
		private void updateAnims ()
		{
			//Updating Animations
			ArrayList<Animation> tempAnims = new ArrayList<Animation>();
			tempAnims = anims;
			
			for (int i = 0; i < anims.size(); i++)
			{
				Animation tempAnim = anims.get(i);
				if (tempAnim.cycle())
				{tempAnims.remove(tempAnim);}
			}
			anims = tempAnims;
		}
		
		private void updateAsteroids ()
		{
			//Updating Asteroids_______________________________________________
			ArrayList<Asteroid> tempAsterList = new ArrayList<Asteroid>();
			tempAsterList = aster;
			Asteroid.Asters = aster;
			
			for (int i = 0; i < aster.size(); i++)
			{
				Asteroid theAster = tempAsterList.get(i);
				theAster.collisionChk(BlueShip);
				theAster.update();
				Asteroid.Asters = tempAsterList;
				
				if (theAster.deathChk() && theAster.size > 1)
				{
					GameBoard.playSound(Asteroid.Boom, 0);
					anims.add(new Animation(theAster.xPos, theAster.yPos, theAster.width, theAster.height, theAster.xSpeed, theAster.ySpeed, 3, 0, theAster.dir, 2));
					
					//Adding Coins
					if (ran(1, Coin.coinPrb) == 1)
					{coins.add(new Coin(BlueShip.intColor, theAster.xPos, theAster.yPos, 0));}

					Asteroid tempAster = theAster.split();

					tempAsterList.add(tempAster);
					Asteroid.Asters = tempAsterList;
				}
				
				if (theAster.deathChk() && theAster.size <= 1)
				{	
					GameBoard.playSound(Asteroid.Boom, 0);
					anims.add(new Animation(theAster.xPos, theAster.yPos, theAster.width, theAster.height, theAster.xSpeed, theAster.ySpeed, 3, 0, theAster.dir, 2));

					//Adding Coins
					if (ran(1, Coin.coinPrb) == 1)
					{coins.add(new Coin(BlueShip.intColor, theAster.xPos, theAster.yPos, 3));}
					
					tempAsterList.remove(theAster);
					Asteroid.Asters = tempAsterList;
				}
			}
			
			aster = tempAsterList;
			Asteroid.Asters = aster;
		}
		
		private void updateCoins ()
		{
			//Updating Coins___________________________________________________
			ArrayList<Coin> tempCoins = new ArrayList<Coin>();
			tempCoins = coins;
			for (int i = 0; i < coins.size(); i++)
			{
				Coin tempCoin = coins.get(i);
				if (tempCoin.update())
				{coins.remove(i);}
				
				if (tempCoin.collChk(BlueShip) && BlueShip.clip)
				{
					if (tempCoin.type == 1 || tempCoin.type == 2)
					{BlueShip.setCoins(BlueShip.getCoins() + tempCoin.value); SoloPanel.setCoins(BlueShip.getCoins()); mainBoard.repaint();}
					
					else if (tempCoin.type == 3)
					{BlueShip.setHealth(BlueShip.getHealth() + tempCoin.value); SoloPanel.setHealth(BlueShip.getHealth()); mainBoard.repaint();}
					
					GameBoard.playSound(Coin.Sound[tempCoin.type - 1], 0);
					tempCoins.remove(tempCoin);
				}
			}
			
			coins = tempCoins;
		}

		public void setSelection (int o)
		{
			//Updating Selection
			BlueShip.setSelection(o);
			GameBoard.TheSoloPanel.setSelection(BlueShip.getSelection());
		}
		
		public void cycleSelection (int o)
		{
			BlueShip.cycleSelection(o);
			GameBoard.TheSoloPanel.setSelection(BlueShip.getSelection());
		}
		
		public void restartGame ()
		{
			//Resetting Game_______________________________________
			
			BlueShip.resetShip();
			BlueShip.setHealth(10);
			SoloPanel.setHealth(10);
			
			//Resetting Bullets
			Caliper.clear();
			Laser.clear();
			Cannon.clear();
			Missile.clear();
			
			coins.clear();
			BlueShip.setCoins(0);
			SoloPanel.setCoins(0);
			
			BlueShip.setLives(5);
			SoloPanel.setLives(5);
			
			//Resetting Asteroids
			aster.clear();
			Asteroid.Asters.clear();
			
			numOfAsters = 2;
			roundNum = 1;
			gameRunning = true;
			
			for (int i = 0; i < numOfAsters; i++)
			{aster.add(new Asteroid());}

			Asteroid.Asters = aster;
			
			//Outside classes
			mainBoard.CloseMenus();
			mainBoard.ResumeGame();

			//Repaint
			mainBoard.repaint();	
		}
		
		public boolean roundEndTest ()
		{
			if (aster.size() == 0)
			{return true;}
			
			else
			{return false;}
		}
		
		public void endRound ()
		{
			this.BlueShip.setHealth(10);
			SoloPanel.setHealth(10);

			//Resetting Asteroids
			aster.clear();
			Asteroid.Asters.clear();

			SoloPanel.shopping = true;
			GameBoard.TheSoloPanel.active = true;
			
			GameBoard.TheSoloPanel.scroll(null);

			GameBoard.TheSoloPanel.catchingDown = true;
			GameBoard.TheSoloPanel.catchDown();
			
			gameRunning = false;
			
			SoloPanel.shopping = true;
			Menu.lastThing = "Shop";
			
			//End round bonus
			BlueShip.setCoins(BlueShip.getCoins() + (2 * roundNum)); SoloPanel.setCoins(BlueShip.getCoins()); mainBoard.repaint();
			
			SoloPanel.setRound(roundNum + 1);
		}
		
		public void startNewRound ()
		{
			//Resetting Stuff
			Caliper.clear();
			Laser.clear();
			Cannon.clear();
			Missile.clear();
			
			coins.clear();
			
			//Round
			roundNum ++;
			gameRunning = true;
			
			//Redoing Asteroids
			numOfAsters = roundNum * 2;
			if (numOfAsters > 20)
			{numOfAsters = 20;}
			
			for (int i = 0; i < numOfAsters; i++)
			{aster.add(new Asteroid());}

			Asteroid.Asters = aster;

			//Redoing Coins
			if (roundNum >= 10)
			{Coin.coinPrb = 5;}
			
			else
			{Coin.coinPrb = 2;}

			System.out.println("roundNum = " + roundNum);
			
			SoloPanel.setRound(roundNum);
		}

		@Override
		public void run()
		{
			//Running Game
			if (GameBoard.paused == false)
			{this.updateSprites();}
			
			//Pausing game
			else if (GameBoard.paused == true && gameRunning == true)
			{
				gameRunning = false;
				GameBoard.TheSoloPanel.startAction("PauseGame");
			}
			
			//Resume Game
			if (GameBoard.paused == false && gameRunning == false && SoloPanel.shopping == false)
			{
				gameRunning = true;
				GameBoard.Menus[2].sendAction(0, "ResumeGame");
			}
		}
		
		//Randomizer z!
		private static int ran (int min, int max)
		{return gen.nextInt((max - min) + 1) + min;}
	}