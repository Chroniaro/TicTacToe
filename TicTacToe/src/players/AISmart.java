package players;

import java.util.Arrays;
import shell.Board;
import shell.Player;
import shell.Point;
import shell.RunInfo;

public class AISmart extends Player
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4822727032583858382L;

	Point myLastMove = null;

	@Override
	public boolean needsInput()
	{
		return false;
	}

	@Override
	public Point getNextMove(Board board, Point lastMove)
	{
		if ((lastMove == null || lastMove == pass) && (myLastMove == null || myLastMove == pass))
			return myLastMove = planNewAttack(board);

		RunInfo a = findStrongestRun(board, getVictorySize(), myLastMove);
		RunInfo d = findStrongestRun(board, getVictorySize(), lastMove);
		final boolean aw = isWinnable(board, a, getVictorySize()),
				dw = isWinnable(board, d, getVictorySize());
		if(aw && dw)
			return myLastMove = findDefense(board, d.getLength() > a.getLength() ? d : a);
		else if(aw && !dw)
			return myLastMove = findDefense(board, a);
		else if(dw)
			return myLastMove = findDefense(board, d);
		else if(a != null)
			return myLastMove = planNewAttack(board, a.getEnds());
		else if(d != null)
			return myLastMove = planNewAttack(board, d.getEnds());
		else
			return myLastMove = planNewAttack(board);
	}

	static RunInfo findStrongestRun(Board board, int victorySize, Point... locations)
	{
		int realLocations = locations.length;
		for (Point p : locations)
			if (p == null || p == Player.pass)
				realLocations--;

		RunInfo[] runs = new RunInfo[4 * realLocations];
		int fakes = 0;
		for (int a = 0; a < locations.length; a++)
			if (locations[a] != Player.pass && locations[a] != null)
				for (byte i = 0; i < 4; i++)
					runs[i + 4 * (a - fakes)] = board.getRun(locations[a], i, 1);
			else
				fakes++;

		Arrays.sort(runs, 0, runs.length, new BiasedComparator(board, victorySize));

		for (RunInfo run : runs)
		{
			if(run.getLength() == 1)
				return run;
			
			final int space = board.getExtensibleSpace(run, 1) + board.getExtensibleSpace(run, -1)
					+ run.getNumberOfGaps();

			if (space + run.getLength() < victorySize)
				continue;

			return run;
		}

		return null;
	}

	static boolean isWinnable(Board board, RunInfo run, int winSize)
	{
		if (run == null || board == null || run.getNumberOfGaps() > 1)
			return false;
		int openness = 0;
		if (board.isLegal(run.getExtension(1)))
			openness++;
		if (board.isLegal(run.getExtension(-1)))
			openness++;
		if (run.getNumberOfGaps() == 1)
			return 2 * (run.getLength() + 1) + openness >= 2 * winSize;
		return run.getLength() + openness >= winSize;
	}

	static Point findDefense(Board board, RunInfo run)
	{
		if (run.getNumberOfGaps() > 0)
			return run.getGap();
		
		Point attempt = run.getExtension(1);
		if (board.isLegal(attempt))
			return attempt;

		attempt = run.getExtension(-1);
		if (board.isLegal(attempt))
			return attempt;

		return planNewAttack(board, run.getEnds());
	}

	static Point planNewAttack(Board board, Point... relatives)
	{
		Point p;
		for (Point r : relatives)
			if (r != null && r != Player.pass)
				for (int dx = -1; dx <= 1; dx++)
					for (int dy = -1; dy <= 1; dy++)
						if (board.isLegal(p = new Point(r.x + dx, r.y + dy)))
							return p;

		if (board.isLegal(p = new Point(board.getWidth() / 2, board.getHeight() / 2)))
			return p;

		return new Point((int) (Math.random() * board.getWidth()), (int) (Math.random() * board.getHeight()));
	}
}

class BiasedComparator extends RunInfo.sizeComparator
{
	private Board board;
	private int victorySize;

	public BiasedComparator(Board board, int victorySize)
	{
		this.board = board;
		this.victorySize = victorySize;
	}

	@Override
	public int compare(RunInfo o1, RunInfo o2)
	{
		final boolean o1w = AISmart.isWinnable(board, o1, victorySize),
				o2w = AISmart.isWinnable(board, o2, victorySize);
		if (o2w ^ o1w)
			return o2w ? 1 : -1;
		return super.compare(o1, o2);
	}
}