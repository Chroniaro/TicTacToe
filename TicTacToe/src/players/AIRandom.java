package players;

import shell.Board;
import shell.Player;
import shell.Point;

public class AIRandom extends Player
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 388812413464781293L;

	@Override
	public Point getNextMove(Board board, Point lastMove)
	{
		return new Point((int) (Math.random() * board.getWidth()), (int) (Math.random() * board.getHeight()));
	}
}
