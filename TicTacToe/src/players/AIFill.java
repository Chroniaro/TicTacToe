package players;

import shell.Board;
import shell.Player;
import shell.Point;

public class AIFill extends Player
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4903289846133600246L;

	@Override
	public Point getNextMove(Board board, Point lastMove)
	{
		for(int y = 0; y < board.getHeight(); y++)
			for(int x = 0; x < board.getWidth(); x++)
			{
				Point p = new Point(x, y);
				if(board.isLegal(p))
					return p;
			}
		
		return pass;
	}
}
