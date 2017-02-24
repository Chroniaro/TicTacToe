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
	public Point getNextMove(Board board, Point lastMove)
	{	
		if ((lastMove == null || lastMove == pass) && (myLastMove == null || myLastMove == pass))
			return(myLastMove = planNewAttack(board));

		Point p = findStrongestDefense(board, myLastMove, lastMove);
		if (p == null)
			p = planNewAttack(board, myLastMove, lastMove);
		myLastMove = p;
		return myLastMove;
	}

	Point findStrongestDefense(Board board, Point... locations)
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

		Arrays.sort(runs, 0, runs.length, new RunInfo.sizeComparator());

		for (int i = 0; i < runs.length; i++)
		{
			Point p = findDefense(board, runs[i]);
			if (p != null)
				return p;
		}

		return null;
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

		return null;
	}

	Point planNewAttack(Board board, Point... relatives)
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
