package org.petekinnecom.sequenced;

public class BoardQPair
{
	Square[][] grid;
	Square[] queue;
	
	BoardQPair(Square[][] g, Square[] q)
	{
		/**
		 * Deep copy!
		 */
		grid = new Square[g.length][g[0].length];
		for(int i=0;i<g.length;i++)
		{
			for(int j=0;j<g[0].length;j++)
			{
				grid[i][j] = g[i][j];
			}
		}
		
		queue = new Square[q.length];
		for(int i=0;i<q.length;i++)
		{
			queue[i] = q[i];
		}
	}
}
