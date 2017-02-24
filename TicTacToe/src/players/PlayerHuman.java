package players;

import shell.Board;
import shell.Player;
import shell.Point;

public final class PlayerHuman extends Player
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -978278867456604523L;

	@Override
	public boolean needsInput()
	{
		return true;
	}

	@Override
	public Point getNextMove(Board board, Point lastMove)
	{
		Point input = getInputLocation();
		return input;
	}
}
