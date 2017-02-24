package players;

import shell.Board;
import shell.Player;
import shell.Point;

public class AIPass extends Player
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 844931062753441010L;

	@Override
	public Point getNextMove(Board board, Point lastMove)
	{
		return pass;
	}

}
