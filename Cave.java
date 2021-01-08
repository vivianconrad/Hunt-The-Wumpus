package conrad;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
/**
 * Class that models a caves for the "Hunt the Wumpus" game.
 * 
 * @author Vivian Conrad
 * 
 * @version 12/1/2020
 */
public class Cave
	{
		private int num;
		private ArrayList<Integer> adj;
		private boolean visited = false;
		private String name;
		private CaveContents contents = CaveContents.EMPTY;

		/**
		 * Constructs a new instance of cave.
		 *
		 * @param name The name of the cave
		 * @param num The cave's number
		 * @param adj A list of caves that are adjacent to the current cave
		 */
		
		public Cave(String name, int num, ArrayList<Integer> adj) {
			this.num = num;
			this.adj = adj;
			this.name = name;
		}
		
		/**
		 * Prints out the name, ID and contents of the cave to map out for testing
		 */
//		public String toString() {
//			return name + " " + num + " " + contents + "\n";
//		}

		/**
		 * Accesses the cave number of an adjacent cave down the specified tunnel. If no
		 * tunnel with that number exists, it returns -1.
		 * 
		 * @param tunnelNum the tunnel number (between 1 and number of caves, inclusive)
		 * 
		 * @return the number of the adjacent cave through that tunnel, or -1 if no such
		 *         tunnel
		 */
		public int getCaveNumDownTunnel(int tunnelNum)
			{
				if (0 < tunnelNum && tunnelNum < adj.size() + 1)
					{
						return adj.get(tunnelNum - 1);
					}
				return -1;
			}

		/**
		 * Accesses the name of the cave, or "unknown" if yet to be visited.
		 * 
		 * @return the cave name or "unknown"
		 */
		public String getVisibleName()
			{
				if (this.visited)
					{
						return this.name;
					}
				return "unknown";

			}

		/**
		 * Accesses the number of the cave.
		 * 
		 * @return the cave number
		 * 
		 */
		public int getCaveNumber()
			{
				return this.num;
			}

		/**
		 * Accesses the contents of the contents (CaveContents.EMPTY,
		 * CaveContents.WUMPUS, CaveContents.BATS, or CaveContents.PIT).
		 * 
		 * @return the cave contents in current cave
		 */
		public CaveContents getContents()
			{
				return this.contents;
			}
		
		/**
		 * Sets the contents of the cave (CaveContents.EMPTY, CaveContents.WUMPUS,
		 * CaveContents.BATS, or CaveContents.PIT).
		 * 
		 * @param contents - the new contents
		 */
		public void setContents(CaveContents contents)
			{
				this.contents = contents;
			}

		/**
		 * Accesses the number of caves adjacent to this one.
		 * 
		 * @return the number of adjacent caves
		 */
		public int getNumTunnels()
			{
				return adj.size();
			}
		
		/**
		 * Accesses the list of caves adjacent to current cave
		 *
		 * @return ArrayList containing the cave numbers of all caves adjacent to current cave
		 */
		public ArrayList<Integer> getAdjCaves() {
			return this.adj;
		}

		/**
		 * Marks the cave as having been visited. 
		 * The player has accessed the cave and knows the name
		 */
		public void markAsVisited()
			{
				this.visited = true;
			}

	}
