package shell;

import java.io.Serializable;
import java.util.Comparator;

public class Board implements Cloneable, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2548459640454564607L;

	public static final byte Key_X = 1, Key_O = 2;

	private int width, height;
	private byte[][] board;
	private int emptySquares;

	public Board(int width, int height)
	{
		this.width = width;
		this.height = height;
		board = new byte[width][height];
		emptySquares = width * height;
	}

	public Board(byte[][] board)
	{
		this(board.length, board[0].length);

		for (int x = 0; x < board.length; x++)
		{
			if (board[x].length != board[0].length)
				throw new InvalidBoardException("Board must be constructed from rectangular array.");

			for (int y = 0; y < board[x].length; y++)
			{
				this.board[x][y] = board[x][y];
			}
		}
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public boolean place(byte val, Point location)
	{
		if (isLegal(location))
			board[location.x][location.y] = val;
		else
			return false;

		emptySquares--;
		return true;
	}

	public byte getTile(int x, int y)
	{
		return board[x][y];
	}

	public byte[][] getBoard()
	{
		return board;
	}

	@Override
	public Board clone()
	{
		Board b = new Board(board);
		b.emptySquares = this.emptySquares;
		return b;
	}

	public int getEmptySquares()
	{
		return emptySquares;
	}

	public void recalculateEmptySquares()
	{
		emptySquares = 0;

		for (int x = 0; x < getWidth(); x++)
			for (int y = 0; y < getHeight(); y++)
			{
				if (getTile(x, y) == 0)
					emptySquares++;
			}
	}

	public RunInfo getRun(Point reference, byte dir)
	{
		return getRun(reference, dir, 0);
	}
	
	public RunInfo getLongestRun(Point reference, int maxGaps)
	{
		RunInfo[] runs = new RunInfo[4];
		for(byte i = 0; i < 4; i++)
			runs[i] = getRun(reference, i, maxGaps);

		Comparator<RunInfo> comp = new RunInfo.sizeComparator();
		int max = 0;
		for(byte i = 1; i < 4; i++)
			if(comp.compare(runs[max], runs[i]) > 0)
				max = i;
		
		return(runs[max]);
	}

	public boolean isLegal(Point move)
	{
		if(move == Player.pass)
			return false;
		
		return isLegal(move.x, move.y);
	}
	
	public boolean isLegal(int x, int y)
	{
		try
		{
			return board[x][y] == 0;
		}
		catch (ArrayIndexOutOfBoundsException | NullPointerException e)
		{
			return false;
		}
	}

	public RunInfo checkVictory(Point reference, int size)
	{
		for (byte i = 0; i < 4; i++)
		{
			RunInfo run = getRun(reference, i, 0);
			if (run.getLength() >= size) { return run; }
		}

		return null;
	}

	public RunInfo getRun(Point reference, byte dir, int maxGaps)
	{
		int dx, dy;

		dx = (dir & 2) < 2 ? 1 : -(dir & 1);
		dy = (dir & 1) | ((dir & 2) >> 1);

		RunInfo fore = checkDir(reference, dx, dy, maxGaps), 
					back = checkDir(reference, -dx, -dy, maxGaps);
		
		return new RunInfo(fore.getEnds()[1], back.getEnds()[1], getTile(reference), Point.merge(fore.getHoles(), back.getHoles()));
	}

	private RunInfo checkDir(Point initial, int dx, int dy, int maxGaps)
	{
		final byte key = getTile(initial);

		int x = initial.x, y = initial.y;
		
		int gapCount = 0;
		Point[] gaps = new Point[maxGaps];
		
		int tmpx = x, tmpy = y, tmpGC = 0;
		while (tmpx + dx >= 0 && tmpx + dx < getWidth() && tmpy + dy >= 0 && tmpy + dy < getHeight())
		{
			tmpx += dx;
			tmpy += dy;

			if (getTile(tmpx, tmpy) != key)
				if (isLegal(tmpx, tmpy))
					if (tmpGC < maxGaps)
						gaps[tmpGC++] = new Point(tmpx, tmpy);
					else 
						break;
				else 
					break;
			else
			{
				gapCount = tmpGC;
				x = tmpx;
				y = tmpy;
			}
		}
		
		if(tmpGC > maxGaps || gaps.length > maxGaps)
			throw new Error("Found more gaps than expected.");
		
		Point[] compressedGaps = new Point[gapCount];
		for(int i = 0; i < gapCount; i++)
			compressedGaps[i] = gaps[i];
		
		return new RunInfo(initial, new Point(x, y), key, compressedGaps);
	}
	
	/**
	 * 
	 * @param dir This should be a positive number to calculate empty space in the direction of p1,
	 * and negative to do so in the direction of p2. The actual value has no significance. The direction
	 * for either sign is the same as the direction that getExtension will work in given the same value.
	 * 
	 * @param info This is the particular run whose spatial constraints you want to calculate.
	 * 
	 * @return The amount of open squares (determined by isLegal) in the direction described.
	 *  Null runs or length-1 runs will return 0, as will a direction of 0.
	 */
	public int getExtensibleSpace(RunInfo info, int dir)
	{
		if(dir == 0)
			return 0;
		
		if(info == null || info.getLength() <= 1)
			return 0;
		
		int k = 0;
		while(isLegal(info.getExtension((int)Math.signum(dir) * (k + 1))))
			k++;
		return k;
	}

	public byte getTile(Point tile)
	{
		return getTile(tile.x, tile.y);
	}
}

class InvalidBoardException extends Error
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8151886230569708273L;

	public InvalidBoardException(String message)
	{
		super(message);
	}
}