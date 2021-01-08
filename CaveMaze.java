package conrad;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.File;

/**
 * A class that models a maze of caves for the "Hunt the Wumpus" game.
 * 
 * @author Catie Baker, modified by Vivian Conrad
 * 
 * @version 10/30/2020, modified 12/1/2020
 */
public class CaveMaze
	{
		private Cave currentCave;
		private ArrayList<Cave> caves;
		private boolean able = true;
		private int wumpi;
		public int grenades = 0;

		private static Random randomNumGen = new Random();
		// same sequence for random number generator every time
		// (0) = same thing, take out 0 for random stuff
		// for testing

		/**
		 * Creates a StringBuilder to map out caves for testing works in conjunction
		 * with toString() in cave.java
		 */
		public String toString()
			{
				StringBuilder sb = new StringBuilder();
				for (Cave cave : caves)
					{
						sb.append(cave);
					}
				return sb.toString();
				// maps out the map
			}

		/**
		 * Constructs a CaveMaze from the data found in a file.
		 * 
		 * @param filename the name of the cave data file
		 */
		public CaveMaze (String filename) throws java.io.FileNotFoundException {
			Scanner infile = new Scanner(new File(filename));
			// reads cave.txt

			int numCaves = infile.nextInt();
			this.caves = new ArrayList<Cave>();
			for (int i = 0; i < numCaves; i++)
				{
					this.caves.add(null);
				}

			// imports cave data
			for (int i = 0; i < numCaves; i++)
				{
					int num = infile.nextInt();
					// room number
					int numAdj = infile.nextInt();
					// # adjacent rooms
					ArrayList<Integer> adj = new ArrayList<Integer>();
					// ID's of rooms
					for (int a = 0; a < numAdj; a++)
						{
							adj.add(infile.nextInt());
						}
					String name = infile.nextLine().trim();
					this.caves.set(num, new Cave(name, num, adj));
				}

			this.currentCave = this.caves.get(0);
			this.currentCave.markAsVisited();
			for (int i = 1; i < this.caves.size(); i++)
				{
					// int caveSet = (int) (Math.random() * 5);
					int caveSet = (randomNumGen.nextInt(5));
					if ( caveSet == 0 )
						{
							if ( !(this.wumpi > 2) )
								{
									this.caves.get(i).setContents(CaveContents.WUMPUS);
									this.wumpi++;
									// adds a wumpus to cave
									this.grenades += 3;
									// gives player 3 grenades per wumpus
								} else
								{
									this.caves.get(i).setContents(CaveContents.EMPTY);
								}

						} else if ( caveSet == 1 )
						{
							this.caves.get(i).setContents(CaveContents.BATS);
						} else if ( caveSet == 2 )
						{
							this.caves.get(i).setContents(CaveContents.PIT);
						} else
						{
							this.caves.get(i).setContents(CaveContents.EMPTY);
						}
				}
		}

		/**
		 * Moves the player from the current cave along the specified tunnel, marking
		 * the new cave as visited.
		 * 
		 * @param tunnel the number of the tunnel to be traversed (1-3)
		 */
		public String move(int tunnel)
			{
				if ( tunnel < 1 || tunnel > this.currentCave.getNumTunnels() )
					{
						return "There is no tunnel number " + tunnel;
					}

				int caveNum = this.currentCave.getCaveNumDownTunnel(tunnel);
				this.currentCave = this.caves.get(caveNum);
				this.currentCave.markAsVisited();

				String message = "";		
				if ( this.currentCave.getContents() == CaveContents.WUMPUS )
					{
						message += "A startled wumpus charges into your cave... CHOMP CHOMP CHOMP";
						this.able = false;
						return message;
					}
				if ( this.currentCave.getContents() == CaveContents.PIT )
					{
						message += "You fall into a bottomless pit. Never to be seen again.";
						this.able = false;
						return message;
					}
				if ( this.currentCave.getContents() == CaveContents.BATS )
					{
						// int randomNum = (int) Math.random() * this.caves.size();
						int randomNum = randomNumGen.nextInt(this.caves.size());
						message += "You stumble into a cave of bats, you are taken to a random cave.";
						this.currentCave = this.caves.get(randomNum);
						return message;
					} else
					{
						return "Moving down tunnel " + tunnel + "...";
					}
			}

		/**
		 * Attempts to toss a stun grenade into the specified tunnel, but currently no
		 * grenades.
		 * 
		 * @param tunnel the number of the tunnel to be bombed (1-3)
		 */
		public String toss(int tunnel)
			{
				if ( tunnel < 1 || tunnel > this.currentCave.getNumTunnels() )
					{
						return "There is no tunnel number " + tunnel;
					}

				int caveNum = this.currentCave.getCaveNumDownTunnel(tunnel);
				Cave sideCave = this.caves.get(caveNum);
				// adj cave to toss into

				if ( grenades <= 0 )
					{
						return "You have no stun grenades to throw!";
					}
				String message = "";
				boolean success = false;
				if ( sideCave.getContents() == CaveContents.WUMPUS )
					{
						this.wumpi--;
						message += "You have stunned and captured a wumpus";
						sideCave.setContents(CaveContents.EMPTY);
						success = true;
						grenades--;
					}
				for (int i = 1; i <= sideCave.getNumTunnels(); i++)
					{
						int otherCavesID = sideCave.getCaveNumDownTunnel(i);
						Cave otherCave = this.caves.get(otherCavesID);
						if ( otherCave.getContents() == CaveContents.WUMPUS )
							{
								int exitID = (int) (Math.random() * otherCave.getNumTunnels());
								if ( exitID == this.currentCave.getCaveNumber() )
									{
										message += "A startled wumpus charges into your cave... CHOMP CHOMP CHOMP";
										this.able = false;
										return message;
									}
								this.caves.get(exitID).setContents(otherCave.getContents());
								otherCave.setContents(CaveContents.EMPTY);
							}
					}
				if ( success )
					{
						return message;
					}
				return "Missed, dagnabit!";

			}

		/**
		 * Displays the current cave name and the names of adjacent caves. Caves that
		 * have not yet been visited are displayed as "unknown".
		 */

		public String showLocation()
			{
				String message = "You are currently in " + this.currentCave.getVisibleName();
				Cave adjCave = null;
				for (int i = 1; i <= this.currentCave.getNumTunnels(); i++)
					{
						int caveNum = this.currentCave.getCaveNumDownTunnel(i);
						adjCave = this.caves.get(caveNum);
						message += "\n    (" + i + ") " + adjCave.getVisibleName();
					}

				if ( adjCave.getContents() == CaveContents.WUMPUS )
					{
						message += "\nYou smell an awful stench coming from somewhere nearby.";
					} else if ( adjCave.getContents() == CaveContents.BATS )
					{
						message += "\nYou hear the flapping of wings close by.";
					} else if ( adjCave.getContents() == CaveContents.PIT )
					{
						message += "\nYou feel a cold draft coming from one of the tunnels.";
					}
				// message += this.currentCave.getContents();

				return message;
			}

		/**
		 * Reports whether the player is still able (healthy and mobile).
		 * 
		 * @return true
		 */
		public boolean stillAble()
			{
				return this.able;
			}

		/**
		 * Reports whether there are any wumpi remaining.
		 * 
		 * @return true
		 */
		public boolean stillWumpi()
			{
				return this.wumpi != 0;
			}
	}