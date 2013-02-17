package org.petekinnecom.sequenced;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

import android.util.Log;
import android.view.View;

public class GameModel
{
	private static final String TAG = "GameModel";

	private Square[][] grid;
	private Square[] queue;
	private int score;
	private int width;
	private int maxNumber;
	private static final int QUEUE_SIZE = 4;

	public GameModel()
	{
		width = C.WIDTH;
		maxNumber = C.MAX_NUMBER;
		score = 0;
		grid = new Square[width][width];
		queue = new Square[QUEUE_SIZE];
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < width; j++)
			{
				grid[i][j] = new Square();
			}

		}
		for (int i = 0; i < QUEUE_SIZE; i++)
		{
			queue[i] = new Square(maxNumber);

		}
		if (C.DEBUG)
			Log.d(TAG, "init gamemodel");

		undoQ = new Stack<BoardQPair>();

	}

	public int getWidth()
	{
		return width;
	}

	public int getQSize()
	{
		return QUEUE_SIZE;
	}

	public Square getSquare(int i, int j)
	{
		return grid[i][j];
	}

	public Square getQSquare(int i)
	{
		return queue[i];
	}

	public void qSelect(int activate)
	{
		for (int i = 0; i < QUEUE_SIZE; i++)
		{
			queue[i].selected = false;
		}
		if (queue[activate].filled)
			queue[activate].selected = true;

	}

	int activeQMember;

	/**
	 * Returns an integer representing how many squares have been removed from
	 * the queue. This will be used to activate buttons.
	 */

	public int boardSelect(int x, int y)
	{
		/**
		 * Check if this square is already populated. If so, it's a misclick,
		 * just return.
		 */
		if (grid[x][y].filled)
			return qCount();

		/**
		 * Check to see if we have an active queue member.
		 */
		activeQMember = -1;
		for (int i = 0; i < QUEUE_SIZE; i++)
		{
			if (queue[i].selected)
				activeQMember = i;
		}
		if (activeQMember < 0)
		{
			/**
			 * Nothings active, so just return.
			 */
			return qCount();
		}

		/**
		 * At this point, we have a selected Q member and a place to put it, so
		 * let's make the switch!
		 */
		/** save undo info */
		undoQ.push(new BoardQPair(grid, queue));

		grid[x][y] = queue[activeQMember];
		grid[x][y].selected = false;
		grid[x][y].reset = true;
		queue[activeQMember] = new Square();

		return qCount();
	}

	public int qCount()
	{
		int k = 0;
		for (int i = 0; i < QUEUE_SIZE; i++)
		{
			if (!queue[i].filled)
				k++;
		}
		return k;
	}

	public int boardCount()
	{
		int k = width * width;
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < width; j++)
			{
				if (grid[i][j].filled)
					k--;
			}
		}
		return k;
	}

	/**
	 * returns true if queue is not empty.
	 */
	Stack<BoardQPair> undoQ;

	public boolean resetMove()
	{

		if (undoQ.isEmpty() && C.DEBUG)
		{
			Log.d(TAG, "resetMove: undoQ is empty.  Illegal button state.");
			return false;
		}
		BoardQPair bq = undoQ.pop();
		grid = bq.grid;
		queue = bq.queue;
		if (undoQ.isEmpty())
			return false;
		return true;
	}

	public void nextMove()
	{
		if (qCount() < 2)
			Log.d(TAG, "qCount too low, button in bad state.");
		if (boardCount() == 0)
		{
			Log.d(TAG, "boardCount too low, button in bad state.");
		}

		/**
		 * new Queue
		 */
		for (int i = 0; i < QUEUE_SIZE; i++)
		{
			queue[i] = new Square(maxNumber);
		}
		/**
		 * no resetables
		 */
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < width; j++)
			{
				grid[i][j].reset = false;
				grid[i][j].active = false;
			}
		}

	}

	public int getScore()
	{
		return score;
	}

	ArrayList<ArrayList<Pair>> sequences;
	ArrayList<Pair> tmp;

	int currentSequence;

	/*
	 * returns true if there's more returns false if no more.
	 */
	public boolean hiliteSequences()
	{
		for(int i=0;i<C.WIDTH;i++)
		{
			for(int j=0;j<C.WIDTH;j++)
			{
				grid[i][j].selected = false;
			}
		}
		for(ArrayList<Pair> a : sequences)
		{
			for(Pair p : a)
			{
				grid[p.x][p.y].selected = true;
			}
		}
		return true;
	}

	private void findVerticalSequences(int length)
	{
		int diff;
		boolean linear;

		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j <= width - length; j++)
			{
				linear = true;
				diff = grid[i][j].val - grid[i][j + 1].val;
				tmp = new ArrayList<Pair>();
				for (int k = 0; k < length - 1; k++)
				{
					if (grid[i][j + k].val - grid[i][j + k + 1].val != diff)
					{
						linear = false;

					}
					tmp.add(new Pair(i, j + k));
				}
				tmp.add(new Pair(i, j + length - 1));
				if (linear)
				{
					sequences.add(tmp);
					// if(C.DEBUG)
					// Log.d(TAG, "Vertical sequence found.");

				}

			}
		}

	}

	private void findHorizontalSequences(int length)
	{
		int diff;
		boolean linear;

		for (int i = 0; i <= width - length; i++)
		{
			for (int j = 0; j < width; j++)
			{
				linear = true;
				diff = grid[i][j].val - grid[i + 1][j].val;
				tmp = new ArrayList<Pair>();
				for (int k = 0; k < length - 1; k++)
				{
					if (grid[i + k][j].val - grid[i + k + 1][j].val != diff)
					{
						linear = false;

					}
					tmp.add(new Pair(i + k, j));
				}
				tmp.add(new Pair(i + length - 1, j));
				if (linear)
				{
					sequences.add(tmp);
					// if(C.DEBUG)
					// Log.d(TAG, " Horizontal sequence found.");

				}

			}
		}

	}

	private void findDownwardDiagonalSequences(int length)
	{
		/**
		 * This one is tricky...
		 */
		int diff;
		boolean linear;

		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < width; j++)
			{
				if ((i + (length - 1) < width) && j + (length - 1) < width)
				{
					/**
					 * There is a valid upward diagonal from here.
					 */
					// if(C.DEBUG)
					// Log.d(TAG, "checking from: "+i+", "+j);

					linear = true;
					diff = grid[i][j].val - grid[i + 1][j + 1].val;
					if (C.DEBUG)
					{
						// Log.d(TAG, "diff = "+grid[i][j].val +
						// " - "+grid[i+1][j+1].val);
					}
					tmp = new ArrayList<Pair>();
					for (int k = 0; k < length - 1; k++)
					{
						if (grid[i + k][j + k].val
								- grid[i + k + 1][j + k + 1].val != diff)
						{
							linear = false;
							if (C.DEBUG)
							{
								// Log.d(TAG,
								// "grid("+(i+k)+", "+(j+k)+") - grid("+(i+k+1)+", "+(j+k+1));
								// Log.d(TAG, grid[i+k][j+k].val + " - " +
								// grid[i+k+1][j+k+1] +"!= diff");
								// Log.d(TAG, "\n\n");
							}
						}
						tmp.add(new Pair(i + k, j + k));
					}
					tmp.add(new Pair(i + length - 1, j + length - 1));
					if (linear)
					{
						sequences.add(tmp);
						if (C.DEBUG)
							Log.d(TAG, " Upward Diagnal sequence found.");

					}
				}
			}
		}

	}

	private void findUpwardDiagonalSequences(int length)
	{
		/**
		 * This one is tricky...
		 */
		int diff;
		boolean linear;

		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < width; j++)
			{
				if ((i + (length - 1) < width) && j - (length - 1) >= 0)
				{
					/**
					 * There is a valid upward diagonal from here.
					 */
					// if(C.DEBUG)
					// Log.d(TAG, "checking from: "+i+", "+j);

					linear = true;
					diff = grid[i][j].val - grid[i + 1][j - 1].val;
					if (C.DEBUG)
					{
						// Log.d(TAG, "diff = "+grid[i][j].val +
						// " - "+grid[i+1][j-1].val);
					}
					tmp = new ArrayList<Pair>();
					for (int k = 0; k < length - 1; k++)
					{
						if (grid[i + k][j - k].val
								- grid[i + (k + 1)][j - (k + 1)].val != diff)
						{
							linear = false;
							if (C.DEBUG)
							{
								// Log.d(TAG,
								// "grid("+(i+k)+", "+(j-k)+") - grid("+(i+(k+1))+", "+(j-(k+1)));
								// Log.d(TAG, grid[i+k][j-k].val + " - " +
								// grid[i+k+1][j-k-1].val +"!= diff");
								// Log.d(TAG, "\n\n");
							}
						}
						tmp.add(new Pair(i + k, j - k));
					}
					tmp.add(new Pair(i + (length - 1), j - (length - 1)));
					if (linear)
					{
						sequences.add(tmp);
						// if(C.DEBUG)
						// Log.d(TAG, " Upward Diagnal sequence found.");

					}
				}
			}
		}
	}

	public int findSequences()
	{

		if (C.DEBUG)
			Log.d(TAG, "findSequences()");
		sequences = new ArrayList<ArrayList<Pair>>();

		for (int i = 3; i <= width; i++)
		{
			findVerticalSequences(i);
			findHorizontalSequences(i);
			findDownwardDiagonalSequences(i);
			findUpwardDiagonalSequences(i);
		}

		/**
		 * Queue up the first sequence.
		 */
		currentSequence = 0;

		/*
		 * Send back an immediate score, so that we can add to the high scores
		 * if necessary.
		 */
		int tmpScore = 0;
		for (ArrayList<Pair> a : sequences)
		{
			for (Pair p : a)
			{
				tmpScore++;
			}
		}
		return tmpScore;
	}

	public void endGame()
	{
		for (int i = 0; i < C.WIDTH; i++)
		{
			for (int j = 0; j < C.WIDTH; j++)
			{
				grid[i][j].active = false;
			}
		}
		findSequences();
		hiliteSequences();

	}

}
